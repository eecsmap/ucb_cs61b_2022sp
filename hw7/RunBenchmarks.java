/** Runs timing tests on various sorting algorithms.
 *  @author Josh Hug
 */
public class RunBenchmarks {

    /** Size of "large" test array. */
    static final int LARGE_ARRAY_SIZE = 1000000;

    /** Run algorithm SA on ARRAY, and print timings using INPUTDESCRIPTION
     *  to label the result. */
    public static void printTime(SortingAlgorithm sa, int[] array,
                                 String inputDescription) {
        int[] copy = BenchmarkUtility.copy(array);
        double timeTaken = BenchmarkUtility.time(sa, copy);
        System.out.printf("%s took %.2f seconds to sort %s.\n",
                          sa.toString(), timeTaken, inputDescription);
    }

    /** A test of mergesort and the Java library sort on an array of
     *  NUMINTS integers.
     */
    public static void largeArrayTest(int numInts) {
        int maxVal = Integer.MAX_VALUE;

        int[] original =
            BenchmarkUtility.randomInts(numInts, Integer.MAX_VALUE);
        int[] input = BenchmarkUtility.copy(original);

        String inputDescription = String.format("%d numbers from 0 to %d",
                                                numInts, maxVal);

        printTime(new MySortingAlgorithms.MergeSort(), input, inputDescription);
        printTime(new MySortingAlgorithms.JavaSort(), input, inputDescription);
    }

    /** Time insertion sort on an array of NUMINTS values. */
    public static void almostSortedTest(int numInts) {
        int maxVal = Integer.MAX_VALUE;
        int[] original =
              BenchmarkUtility.randomNearlySortedInts(numInts, maxVal);
        int[] input = BenchmarkUtility.copy(original);

        String inputDescription =
               String.format("%d partially sorted numbers from 0 to %d",
               numInts, maxVal);

        printTime(new MySortingAlgorithms.InsertionSort(),
                  input, inputDescription);
    }

    /** Run timing tests on arrays whose is given by ARGS[0], or
     *  LARGE_ARRAY_SIZE if there are no arguments. */
    public static void main(String[] args) {
        System.out.println("Edit Configurations and type a number for "
                           + "program arguments to run a smaller test.");
        int size =
            args.length == 0 ? LARGE_ARRAY_SIZE : Integer.parseInt(args[0]);
        largeArrayTest(size);
        almostSortedTest(size);
    }
}
