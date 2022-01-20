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

    public static void isEvenTest() {
        System.out.println("isEven(2) should be true, result: " + Solutions.isEven(2));
        System.out.println("isEven(10) should be true, result: " + Solutions.isEven(10));
        System.out.println("isEven(1) should be false, result: " + Solutions.isEven(1));
        System.out.println("isEven(99) should be false, result: " + Solutions.isEven(1));
    }

    public static void maxTest() {
        // Change call to max to make this call yours.
        System.out.println("max {1, 2, 3, 4, 5} should return 5, result: "
                + Solutions.max(new int[] { 1, 2, 3, 4, 5 }));
        System.out.println("max {0, -5, 2, 14, 10} should return 14, result: "
                + Solutions.max(new int[] { 0, -5, 2, 14, 10 }));
        System.out.println("max {1} should return 1, result: "
                + Solutions.max(new int[] { 1 }));
        // REPLACE THIS WITH MORE TESTS.
    }
    //
    // @Test
    // public void wordBankTest() {
    //     String[] adjectives = new String[]{"big", "cool", "fast"};
    //     assertTrue(wordBank("big", adjectives));
    //     assertFalse(wordBank("dog", adjectives));
    // }
    //
    // @Test
    // public void threeSumTest() {
    //     // Change call to threeSum to make this call yours.
    //     assertTrue(Solutions.threeSum(new int[] { 1, 2, -3 })); // 1 + 2 - 3 = 0
    //     assertTrue(Solutions.threeSum(new int[] { -6, 3, 10, 200 })); // 3 + 3 - 6 = 0
    //     assertFalse(Solutions.threeSum(new int[] {1, -900, 7}));
    //     // REPLACE THIS WITH MORE TESTS.
    // }

    public static void main(String[] unused) {
        isEvenTest();
        System.out.println("");
        maxTest();
    }

}
