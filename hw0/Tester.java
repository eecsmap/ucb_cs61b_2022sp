import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0.
 *  @author YOUR NAME HERE
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here.  We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void isEvenTest() {
        assertTrue(Solutions.isEven(2));
        assertTrue(Solutions.isEven(10));
        assertFalse(Solutions.isEven(1));
        assertFalse(Solutions.isEven(99));
    }

    @Test
    public void maxTest() {
        // Change call to max to make this call yours.
        assertEquals(5, Solutions.max(new int[] { 1, 2, 3, 4, 5 }));
        assertEquals(14, Solutions.max(new int[] { 0, -5, 2, 14, 10 }));
        assertEquals(1, Solutions.max(new int[] {1}));
        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void wordBankTest() {
        String[] adjectives = new String[]{"big", "cool", "fast"};
        assertTrue(Solutions.wordBank("big", adjectives));
        assertFalse(Solutions.wordBank("dog", adjectives));
    }

    @Test
    public void threeSumTest() {
        // Change call to threeSum to make this call yours.
        assertTrue(Solutions.threeSum(new int[] { 1, 2, -3 })); // 1 + 2 - 3 = 0
        assertTrue(Solutions.threeSum(new int[] { -6, 3, 10, 200 })); // 3 + 3 - 6 = 0
        assertFalse(Solutions.threeSum(new int[] {1, -900, 7}));
        // REPLACE THIS WITH MORE TESTS.
    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
