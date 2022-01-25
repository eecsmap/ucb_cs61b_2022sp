public class ArithmeticTest {

    private static final double DELTA = 1e-15;

    /** Reports whether test result ISOK is true for test whose name
     *  is NAME on standard output. */
    private static void report(String name, boolean isOK) {
        if (isOK) {
            System.out.printf("%s OK.%n", name);
        } else {
            System.out.printf("%s FAILS.%n", name);
        }
    }

    /** Returns true iff X and Y are within DELTA of each other. */
    private static boolean approxEqual(double x, double y) {
        return Math.abs(x - y) < DELTA;
    }

    /** Check that CORRECT and EXPECTED are approximately equal,
     *  incrementing error count if not. */
    private static void check(double correct, double expected) {
        if (!approxEqual(correct, expected)) {
            errorCount++;
        }
    }

    /** Performs a few arbitrary tests to see if the product
     *  method is correct. */
    public static boolean testProduct() {
        int startingErrors = errorCount;
        check(30, Arithmetic.product(5, 6));
        check(-30, Arithmetic.product(5, -6));
        check(0, Arithmetic.product(0, -6));
        return startingErrors == errorCount;
    }

    /* Performs a few arbitrary tests to see if the sum method is correct. */
    public static boolean testSum() {
        int startingErrors = errorCount;
        check(11, Arithmetic.sum(5, 6));
        check(-1, Arithmetic.sum(5, -6));
        check(-6, Arithmetic.sum(0, -6));
        check(0, Arithmetic.sum(6, -6));
        return startingErrors == errorCount;
    }

    /** Run all tests. */
    public static void main(String[] args) {
        report("product", testProduct());
        report("sum", testSum());
    }

    /** Cumulative test errors. */
    private static int errorCount;
}
