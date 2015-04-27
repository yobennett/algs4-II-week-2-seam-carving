import java.awt.*;

public class SeamCarver {

    private static final double BORDER_ENERGY = 195075.0;

    private final Picture picture;
    private final int w;
    private final int h;
    private final double[][] energies;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.w = picture.width();
        this.h = picture.height();
        this.energies = energies();
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return w;
    }

    // height of current picture
    public int height() {
        return h;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateCoordinates(x, y);
        if (isEdgePixel(x, y)) {
            return BORDER_ENERGY;
        }
        return xGradientSquare(x, y) + yGradientSquare(x, y);
    }

    private double xGradientSquare(int x, int y) {
        Color c1, c2;
        c1 = picture.get(x - 1, y);
        c2 = picture.get(x + 1, y);
        return gradientSquare(c1, c2);
    }

    private double yGradientSquare(int x, int y) {
        Color c1, c2;
        c1 = picture.get(x, y - 1);
        c2 = picture.get(x, y + 1);
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

    private boolean isValidCoordinates(int x, int y) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }

    private boolean isEdgePixel(int x, int y) {
        return x == 0 || y == 0 || x == w - 1 || y == h - 1;
    }

    private double[][] energies() {
        double[][] result = new double[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                result[x][y] = energy(x, y);
            }
        }
        return result;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {}

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {}

    public static void main(String[] args) {

    }
}