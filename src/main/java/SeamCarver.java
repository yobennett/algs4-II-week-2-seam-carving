import java.awt.*;
import java.util.*;
import java.util.Stack;

public class SeamCarver {

    private static final double MAX_ENERGY = 195075.0;

    private Picture picture;
    private Color[][] colors;
    private double[] weights;
    private double[] distTo;
    private int[] edgeTo;
    private boolean transposed;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.colors = colors(picture);
        this.transposed = false;
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture().width();
    }

    // height of current picture
    public int height() {
        return picture().height();
    }

    private boolean isTransposed() {
        return transposed;
    }

    // energy of pixel at column and row
    public double energy(int col, int row) {
        validateCoordinates(col, row);
        if (isEdgePixel(col, row)) {
            return MAX_ENERGY;
        }
        return xGradientSquare(col, row) + yGradientSquare(col, row);
    }

    private double xGradientSquare(int x, int y) {
        Color c1, c2;
        c1 = colors[y][x - 1];
        c2 = colors[y][x + 1];
        return gradientSquare(c1, c2);
    }

    private double yGradientSquare(int x, int y) {
        Color c1, c2;
        c1 = colors[y - 1][x];
        c2 = colors[y + 1][x];
        return gradientSquare(c1, c2);
    }

    private double gradientSquare(Color c1, Color c2) {
        int rCentralDiff, gCentralDiff, bCentralDiff;
        rCentralDiff = Math.abs(c1.getRed() - c2.getRed());
        gCentralDiff = Math.abs(c1.getGreen() - c2.getGreen());
        bCentralDiff = Math.abs(c1.getBlue() - c2.getBlue());
        return Math.pow(rCentralDiff, 2) + Math.pow(gCentralDiff, 2) + Math.pow(bCentralDiff, 2);
    }

    private void validateCoordinates(int x, int y) {
        if (!isValidCoordinates(x, y)) {
            throw new IndexOutOfBoundsException("x must be less than " + width() + " and y must be less than " + height());
        }
    }

    // valid column x and row y
    private boolean isValidCoordinates(int x, int y) {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }

    private boolean isEdgePixel(int col, int row) {
        return col == 0 || row == 0 || col == width() - 1 || row == height() - 1;
    }

    private Color[][] colors(Picture p) {
        Color[][] result = new Color[height()][width()];
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                result[row][col] = p.get(col, row);
            }
        }
        return result;
    }

    private void transpose() {
        Picture tPicture = new Picture(height(), width());

        Color[][] tColors = new Color[tPicture.height()][tPicture.width()];
        for (int row = 0; row < tPicture.height(); row++) {
            for (int col = 0; col < tPicture.width(); col++) {
                tColors[row][col] = colors[col][row];
                tPicture.set(col, row, tColors[row][col]);
            }
        }

        picture = tPicture;
        colors = tColors;
        transposed = !transposed;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!isTransposed()) {
            transpose();
        }
        return findSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (isTransposed()) {
            transpose();
        }
        return findSeam();
    }

    private int[] findSeam() {
        findSeamInit();
        relaxEdges();
        int lastVertex = shortestPathLastVertex();

        int[] path = shortestPath(lastVertex);

        // revert transposition
        if (isTransposed()) {
            transpose();
        }

        return path;
    }

    private void printStatus() {
        System.out.println("w="+width()+", h="+height());
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                int v = coordinateToVertexIndex(i, j);
                System.out.printf("%2d:%6.0f:%6.0f ", v, energy(i, j), (distTo[v] == Double.POSITIVE_INFINITY ? 0 : distTo[v]));
            }
            System.out.println();
        }
    }

    private void findSeamInit() {
        int size = width() * height();
        weights = new double[size];
        distTo = new double[size];
        edgeTo = new int[size];

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                int v = coordinateToVertexIndex(col, row);
                weights[v] = energy(col, row);
                edgeTo[v] = -1;
                if (row == 0) {
                    distTo[v] = weights[v];
                } else {
                    distTo[v] = Double.POSITIVE_INFINITY;
                }
            }
        }

    }

    // consider vertices in topological order
    // start from each vertex in top row
    // relax all edges pointing from that vertex
    // downward edge from pixel (x, y) to pixels (x − 1, y + 1), (x, y + 1), and (x + 1, y + 1)
    // precedence since all edges pointing downward
    private void relaxEdges() {
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {

                int v = coordinateToVertexIndex(col, row);
                Iterable<Integer> adj = adj(col, row);

                for (int w : adj) {
                    relax(v, w);
                }

            }
        }
    }

    private Iterable<Integer> adj(int col, int row) {
        Stack<Integer> adj = new Stack<Integer>();

        // bottom
        int x = col;
        int y = row + 1;
        int w;
        if (isValidCoordinates(x, y)) {
            w = coordinateToVertexIndex(x, y);
            adj.push(w);
        }

        // bottom left
        x = col - 1;
        y = row + 1;
        if (isValidCoordinates(x, y)) {
            w = coordinateToVertexIndex(x, y);
            adj.push(w);
        }

        // bottom right
        x = col + 1;
        y = row + 1;
        if (isValidCoordinates(x, y)) {
            w = coordinateToVertexIndex(x, y);
            adj.push(w);
        }
        return adj;
    }

    // relax edge between vertexes v and w
    private void relax(int v, int w) {
        if (distTo[w] > distTo[v] + weights[w]) {
            distTo[w] = distTo[v] + weights[w];
            edgeTo[w] = v;
        }
    }

    // find shortest path
    private int shortestPathLastVertex() {
        double min = Double.POSITIVE_INFINITY;
        int lastRow = height() - 1;
        int lastVertex = -1;
        for (int col = 0; col < width(); col++) {
            int v = coordinateToVertexIndex(col, lastRow);
            if (min > distTo[v]) {
                min = distTo[v];
                lastVertex = v;
            }
        }
        return lastVertex;
    }

    // assemble shortest path
    private int[] shortestPath(int lastVertex) {
        int[] path = new int[height()];
        int col = height() - 1;
        for (int v = lastVertex; v >= 0; v = edgeTo[v]) {
            path[col] = v % width();
            col--;
        }
        return path;
    }

    // index for column and row
    private int coordinateToVertexIndex(int col, int row) {
        return (row * width()) + col;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!isTransposed()) {
            transpose();
        }
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // init resized picture
        int width = width() - 1;
        int height = height();
        Picture resizedPicture = new Picture(width, height);
        Color[][] resizedColors = new Color[height][width];

        // set each row to original row's Colors minus seam pixel
        for (int row = 0; row < height; row++) {
            int seamCol = seam[row];

            Color[] original = colors[row];
            Color[] seamRemoved = new Color[width];

            System.arraycopy(original, 0, seamRemoved, 0, seamCol);
            System.arraycopy(original, seamCol + 1, seamRemoved, seamCol, width - seamCol);

            resizedColors[row] = seamRemoved;

            for (int col = 0; col < width; col++) {
                resizedPicture.set(col, row, seamRemoved[col]);
            }
        }

        // set picture and colors
        picture = resizedPicture;
        colors = resizedColors;
    }

    public static void main(String[] args) {}
}