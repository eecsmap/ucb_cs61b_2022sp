package timing;

import org.junit.Test;

import static org.junit.Assert.*;

public class MySortTester {
    public static void testSorter(Sorter expected, Sorter other) {
        for(int i = 0; i < 10; i++) {
            int[] a1 = other.getRandomArray(100);
            int[] a2 = new int[a1.length];
            System.arraycopy(a1,0,a2, 0, a1.length);
            expected.sort(a1);
            other.sort(a2);
            assertArrayEquals(a1, a2);
        }
    }

    @Test
    public void testBubbleSort() {
        testSorter(new JavaSorter(), new BubbleSorter());
    }

    @Test
    public void testWipingBubbleSort() {
        testSorter(new JavaSorter(), new WipingBubbleSorter());
    }

    @Test
    public void testInsertionSort() {
        testSorter(new JavaSorter(), new InsertionSorter());
    }

    @Test
    public void testCountingSort() {
        testSorter(new JavaSorter(), new CountingSorter());
    }

}
