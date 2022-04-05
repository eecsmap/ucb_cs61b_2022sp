/**
 * A sorting algorithm (e.g. Selection Sort, Insertion Sort, etc.)
 * that implements a single sort method.
 * @author Josh Hug
 */
public interface SortingAlgorithm {

    /**
     * Given a not-necessarily sorted array ARRAY, puts the first
     * K elements in sorted ascending order.
     *
     * Precondition: K <= ARRAY.LENGTH
     */
    void sort(int[] array, int k);

}
