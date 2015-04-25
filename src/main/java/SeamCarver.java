public class SeamCarver {

    private static final double BORDER_ENERGY = 195075.0;

    private final Picture picture;
    private final int w;
    private final int h;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.w = picture.width();
        this.h = picture.height();
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
        if (isEdgePixel(x, y)) {
            return BORDER_ENERGY;
        }
        return 0;
    }

    private boolean isEdgePixel(int x, int y) {
        return x == 0 || y == 0 || x == w - 1 || y == h - 1;
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