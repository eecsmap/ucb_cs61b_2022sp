import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
/** Implementation of Quicksort for integers.
 *  @author Josh Hug
 */
public class Quicksort {
    /** When true, prints out information as the algorithm runs. */
    private static final boolean VERBOSE = true;
    /** When true, performs JUnit testing after each partition. */
    private static final boolean SELF_TEST = true;

    /** Puts A into sorted order using QuickSort. */
    public static void quicksort(int[] a) {
        quicksort(a, 0, a.length - 1);
    }

    /** Puts A[LO..HI] into sorted order. */
    private static void quicksort(int[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int j = partition(a, lo, hi);
        quicksort(a, lo, j - 1);
        quicksort(a, j + 1, hi);
        if (SELF_TEST) {
            assertTrue(isSorted(a, lo, hi));
        }
    }

    /** Partitions A[LO..HI] using a[LO] as pivot.
      * Uses stable partitioning procedure that clusters all items equal to
      * the pivot together in the middle. Returns the position where the pivot
      * lands.
      */
    private static int partition(int[] a, int lo, int hi) {
        /** This method partitions by:
          *
          * 1. Creating three ArrayList of the smaller, equal and larger
          *    items, respectively.
          * 2. Concatenting these ArrayLists into a single list.
          * 3. Converting this ArrayList into an array.
          * 4. Copying the elements back into a.
          *
          * It is possible to write a much faster and more memory efficient
          * partitioning procedure!
          */

        if (VERBOSE) {
            System.out.println("Partitioning " + Arrays.toString(a)
                               + " from index " + lo + " to index " + hi);
        }
        List<Integer> smaller = new ArrayList<Integer>();
        List<Integer> equal = new ArrayList<Integer>();
        List<Integer> larger = new ArrayList<Integer>();

        int pivot = a[lo];
        for (int i = lo; i <= hi; i += 1) {
            if (a[i] < pivot) {
                smaller.add(a[i]);
            } else if (a[i] > pivot) {
                larger.add(a[i]);
            } else {
                equal.add(a[i]);
            }
        }

        List<Integer> partitioned = new ArrayList<Integer>();

        if (VERBOSE) {
            System.out.println("Smaller items: " + smaller);
            System.out.println("Equal items  : " + equal);
            System.out.println("Larger items : " + larger);
        }

        partitioned.addAll(smaller);
        partitioned.addAll(equal);
        partitioned.addAll(larger);

        int[] partitionedArray = convertListToArray(partitioned);
        System.arraycopy(partitionedArray, 0, a, lo, partitionedArray.length);

        if (VERBOSE) {
            System.out.println("Result array : " + Arrays.toString(a));
        }

        return smaller.size() + lo;
    }

    /** Returns array copy of AL. */
    private static int[] convertListToArray(List<Integer> al) {
        int[] returnArray = new int[al.size()];

        for (int i = 0; i < al.size(); i += 1) {
            returnArray[i] = al.get(i);
        }

        return returnArray;
    }

    /** Returns true if A is sorted between LO and HI. */
    private static boolean isSorted(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i += 1) {
            if (a[i] < a[i - 1]) {
                return false;
            }
        }
        return true;
    }

    /** Performs a demo of Quicksort. ARGS is ignored. */
    public static void main(String[] args) {
        int[] someInts = new int[]{5, 3, 2, 1, 7, 8, 4, 6};
        quicksort(someInts);
    }
}
