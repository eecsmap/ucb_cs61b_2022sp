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


    /* Helpful formatting method for running tests.
    Prints out a statement if the test fails. */
    private static void assertTrue(boolean answer, String test) {
      if (!answer) {
        System.out.printf("Executing %s, expected true and received %b.%n", test, answer);
      }
    }

    /* Helpful formatting method for running tests.
    Prints out a statement if the test fails. */
    private static void assertFalse(boolean answer, String test) {
      if (answer) {
        System.out.printf("Executing %s, expected false and received %b.%n", test, answer);
      }
    }

    /* Helpful formatting method for running tests.
    Prints out a statement if the test fails. */
    private static void assertIntEquals(int a, int b, String test) {
      if (!(a == b)) {
        System.out.printf("Executing %s, expected %d and received %d.%n", test, a, b);
      }
    }

    public static void isEvenTest() {
        // Change call to max to make this call yours.
        assertTrue(Solutions.isEven(2), "IsEvenTest");
        assertTrue(Solutions.isEven(10), "IsEvenTest");
        assertFalse(Solutions.isEven(1), "IsEvenTest");
        assertFalse(Solutions.isEven(99), "IsEvenTest");
        // REPLACE THIS WITH MORE TESTS.
    }

//    // UNCOMMENT EVERYTHING BELOW TO RUN MORE TESTS.

//    public static void maxTest() {
//        // Change call to max to make this call yours.
//        assertIntEquals(5, Solutions.max(new int[] { 1, 2, 3, 4, 5 }), "maxTest");
//        assertIntEquals(14, Solutions.max(new int[] { 0, -5, 2, 14, 10 }), "maxTest");
//        assertIntEquals(1, Solutions.max(new int[] {1}), "maxTest");
//        // REPLACE THIS WITH MORE TESTS.
//    }
//
//    public static void wordBankTest() {
//        // Change call to threeSum to make this call yours.
//        String[] adjectives = new String[]{"big", "cool", "fast"};
//        assertTrue(Solutions.wordBank("big", adjectives), "wordBankTest");
//        assertFalse(Solutions.wordBank("dog", adjectives), "wordBankTest");
//        // REPLACE THIS WITH MORE TESTS.
//    }
//
//    public static void threeSumTest() {
//        // Change call to threeSum to make this call yours.
//        assertTrue(Solutions.threeSum(new int[] { 1, 2, -3 }), "3SUM Test"); // 1 + 2 - 3 = 0
//        assertTrue(Solutions.threeSum(new int[] { -6, 3, 10, 200 }), "3SUM Test"); // 3 + 3 - 6 = 0
//        assertFalse(Solutions.threeSum(new int[] {1, -900, 7}), "3SUM Test");
//        // REPLACE THIS WITH MORE TESTS.
//    }

    public static void main(String[] unused) {
        System.out.println("Make sure you've uncommented all the test you wish to run.");
        System.out.println("ALL FAILURES SHOWN BELOW (if none, congrats! you're good.)");
        isEvenTest();

//        // UNCOMMENT THE LINES BELOW
//        maxTest();
//        wordBankTest();
//        threeSumTest();
    }

}
