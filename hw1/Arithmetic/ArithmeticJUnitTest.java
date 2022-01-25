import static org.junit.Assert.*;
import org.junit.Test;

public class ArithmeticJUnitTest {

    /** Tolerance for comparison of doubles. */
    static final double DELTA = 1e-15;

    @Test
    public void testProduct() {
        /* assertEquals for comparison of doubles takes three arguments:
         *      assertEquals(expected, actual, DELTA).
         *  + if Math.abs(expected - actual) < DELTA, then the test succeeds.
         *  + Otherwise, the test fails.
         *
         *  See http://junit.sourceforge.net/javadoc/org/junit/ \
         *             Assert.html#assertEquals(double, double, double)
         *  for more. */

        assertEquals(30, Arithmetic.product(5, 6), DELTA);
        assertEquals(-30, Arithmetic.product(5, -6), DELTA);
        assertEquals(0, Arithmetic.product(0, -6), DELTA);
    }

    @Test
    public void testSum() {
        assertEquals(11, Arithmetic.sum(5, 6), DELTA);
        assertEquals(-1, Arithmetic.sum(5, -6), DELTA);
        assertEquals(-6, Arithmetic.sum(0, -6), DELTA);
        assertEquals(0, Arithmetic.sum(6, -6), DELTA);
    }

    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(ArithmeticJUnitTest.class));
    }
}
