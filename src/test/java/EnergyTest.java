import static org.junit.Assert.assertEquals;

import java.awt.*;

import org.junit.Before;
import org.junit.Test;

public class EnergyTest {

    Picture picture6x5;

    @Before
    public void setUp() {
        this.picture6x5 = new Picture(6, 5);
        Color[][] colors6x5 = new Color[][] {
                { new Color(97, 82, 107), new Color(220, 172, 141),
                        new Color(243, 71, 205), new Color(129, 173, 222),
                        new Color(225, 40, 209), new Color(66, 109, 219) },
                { new Color(181, 78, 68), new Color(15, 28, 216),
                        new Color(245, 150, 150), new Color(177, 100, 167),
                        new Color(205, 205, 177), new Color(147, 58, 99) },
                { new Color(196, 224, 21), new Color(166, 217, 190),
                        new Color(128, 120, 162), new Color(104, 59, 110),
                        new Color(49, 148, 137), new Color(192, 101, 89) },
                { new Color(83, 143, 103), new Color(110, 79, 247),
                        new Color(106, 71, 174), new Color(92, 240, 205),
                        new Color(129, 56, 146), new Color(121, 111, 147) },
                { new Color(82, 157, 137), new Color(92, 110, 129),
                        new Color(183, 107, 80), new Color(89, 24, 217),
                        new Color(207, 69, 32), new Color(156, 112, 31) } };

        for (int y = 0; y < picture6x5.height(); y++) {
            for (int x = 0; x < picture6x5.width(); x++) {
                picture6x5.set(x, y, colors6x5[y][x]);
            }
        }
    }

    @Test
    public void energiesFor6x5() {
        SeamCarver seamCarver = new SeamCarver(picture6x5);

        assertEquals(195075.0, seamCarver.energy(0, 0), 0.0001);
        assertEquals(195075.0, seamCarver.energy(1, 0), 0.0001);
        assertEquals(195075.0, seamCarver.energy(2, 0), 0.0001);
        assertEquals(195075.0, seamCarver.energy(3, 0), 0.0001);
        assertEquals(195075.0, seamCarver.energy(4, 0), 0.0001);
        assertEquals(195075.0, seamCarver.energy(5, 0), 0.0001);
        assertEquals(195075.0, seamCarver.energy(0, 1), 0.0001);
        assertEquals(23346.0, seamCarver.energy(1, 1), 0.0001);
        assertEquals(51304.0, seamCarver.energy(2, 1), 0.0001);
        assertEquals(31519.0, seamCarver.energy(3, 1), 0.0001);
        assertEquals(55112.0, seamCarver.energy(4, 1), 0.0001);
        assertEquals(195075.0, seamCarver.energy(5, 1), 0.0001);
        assertEquals(195075.0, seamCarver.energy(0, 2), 0.0001);
        assertEquals(47908.0, seamCarver.energy(1, 2), 0.0001);
        assertEquals(61346.0, seamCarver.energy(2, 2), 0.0001);
        assertEquals(35919.0, seamCarver.energy(3, 2), 0.0001);
        assertEquals(38887.0, seamCarver.energy(4, 2), 0.0001);
        assertEquals(195075.0, seamCarver.energy(5, 2), 0.0001);
        assertEquals(195075.0, seamCarver.energy(0, 3), 0.0001);
        assertEquals(31400.0, seamCarver.energy(1, 3), 0.0001);
        assertEquals(37927.0, seamCarver.energy(2, 3), 0.0001);
        assertEquals(14437.0, seamCarver.energy(3, 3), 0.0001);
        assertEquals(63076.0, seamCarver.energy(4, 3), 0.0001);
        assertEquals(195075.0, seamCarver.energy(5, 3), 0.0001);
        assertEquals(195075.0, seamCarver.energy(0, 4), 0.0001);
        assertEquals(195075.0, seamCarver.energy(1, 4), 0.0001);
        assertEquals(195075.0, seamCarver.energy(2, 4), 0.0001);
        assertEquals(195075.0, seamCarver.energy(3, 4), 0.0001);
        assertEquals(195075.0, seamCarver.energy(4, 4), 0.0001);
        assertEquals(195075.0, seamCarver.energy(5, 4), 0.0001);
    }

}
