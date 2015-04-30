import java.awt.*;

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
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    private boolean isTransposed() {
        return transposed;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateCoordinates(x, y);
        if (isEdgePixel(x, y)) {
            return MAX_ENERGY;
        }
        return xGradientSquare(x, y) + yGradientSquare(x, y);
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
            throw new IndexOutOfBoundsException();
        }
    }

    // valid column x and row y
    private boolean isValidCoordinates(int x, int y) {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }

    // valid vertex v
    private boolean isValidVertex(int v) {
        return v >= 0 && v < (width() * height());
    }

    private boolean isEdgePixel(int x, int y) {
        return x == 0 || y == 0 || x == width() - 1 || y == height() - 1;
    }

    private Color[][] colors(Picture p) {
        Color[][] result = new Color[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                result[i][j] = p.get(j, i);
            }
        }
        return result;
    }

    private double[][] energies() {
        double[][] result = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                result[i][j] = energy(j, i);
            }
        }
        return result;
    }

    private void transpose() {
        picture = new Picture(height(), width());
        colors = colors(picture);
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

        int size = width() * height();

        // initialize
        weights = new double[size];
        distTo = new double[size];
        edgeTo = new int[size];

        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {

                int index = coordinateToVertexIndex(j, i);

                weights[index] = energy(j, i);

                if (isFirstRow(i)) {
                    distTo[index] = 0;
                } else {
                    distTo[index] = Double.POSITIVE_INFINITY;
                }

                edgeTo[index] = -1;

            }
        }

        // consider vertices in topological order
        // start from each vertex in top row
        // relax all edges pointing from that vertex
        // downward edge from pixel (x, y) to pixels (x − 1, y + 1), (x, y + 1), and (x + 1, y + 1)
        // precedence since all edges pointing downward
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {

                int v = coordinateToVertexIndex(j, i);
                int w;

                // bottom left
                w = coordinateToVertexIndex(j - 1, i + 1);
                if (isValidVertex(w)) {
                    relax(v, w);
                }

                // bottom
                w = coordinateToVertexIndex(j, i + 1);
                if (isValidVertex(w)) {
                    relax(v, w);
                }

                // bottom right
                w = coordinateToVertexIndex(j + 1, i + 1);
                if (isValidVertex(w)) {
                    relax(v, w);
                }
            }
        }

        // find shortest path
        double min = Double.POSITIVE_INFINITY;
        int i = height() - 1;
        int lastVertex = -1;
        for (int j = 0; j < width(); j++) {
            int v = coordinateToVertexIndex(j, i);
            if (min > distTo[v]) {
                min = distTo[v];
                lastVertex = v;
            }
        }

        // assemble shortest path
        int[] result = new int[height()];
        for (int v = lastVertex; v >= 0; v = edgeTo[v]) {
            int row = (int) Math.floor(v / width());
            int col = v % width();
            result[row] = col;
        }

        return result;
    }

    // relax edge between vertexes v and w
    private void relax(int v, int w) {
        if (distTo[w] > distTo[v] + weights[w]) {
            distTo[w] = distTo[v] + weights[w];
            edgeTo[w] = v;
        }
    }

    // index for column x and row y
    private int coordinateToVertexIndex(int x, int y) {
        return (y * width()) + x;
    }

    private boolean isFirstRow(int j) {
        return j == 0;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {}

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {}

    public static void main(String[] args) {}
}