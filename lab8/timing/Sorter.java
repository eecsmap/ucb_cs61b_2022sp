package timing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public abstract class Sorter {
    public abstract void sort(int[] array);

    public int[] getRandomArray(int nelements) {
        int[] array = new int[nelements];
        Random random = new Random();
        for (int k = 0; k < nelements; k++) {
            array[k] = random.nextInt();
        }
        return array;
    }

    public List<Double> score(int by, int ntrials, int nrepeats) {
        /* Set min to reasonably small number */
        int min = 5;
        /* Calculate max on interval and number of trials */
        int max = min + by * ntrials;

        Timer t = new Timer();
        List<Double> scores = new ArrayList<>();

        /* For each size of array */
        for (int nelements = min; nelements < max; nelements += by) {
            List<Double> scorePerSize = new ArrayList<>();
            /* For each repeat of sorting of array */
            for (int i = 0; i < nrepeats; i++) {
                int[] array = getRandomArray(nelements);
                t.start();
                sort(array);
                double elapsedMs = t.stop();
                scorePerSize.add(elapsedMs);
            }
            double total = 0;
            for (int k = 0; k < scorePerSize.size(); k++) {
                total += scorePerSize.get(k);
            }
            scores.add(total / ntrials);
        }
        return scores;
    }
}

class BubbleSorter extends Sorter {
    @Override
    public void sort(int[] array) {
        boolean finished = false;
        while(!finished) {
            finished = true;
            for (int j = 0; j < array.length - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = temp;
                    finished = false;
                }
            }
        }
    }
}

class WipingBubbleSorter extends Sorter {
    @Override
    public void sort(int[] array) {
        boolean finished = false;
        boolean even = true;
        while(!finished) {
            finished = true;
            if(even) {
                for (int j = 0; j < array.length - 1; j++) {
                    if (array[j] > array[j + 1]) {
                        int temp = array[j + 1];
                        array[j + 1] = array[j];
                        array[j] = temp;
                        finished = false;
                    }
                }
                even = false;
            } else {
                for (int j = array.length - 1; j > 0; j--) {
                    if (array[j] < array[j - 1]) {
                        int temp = array[j - 1];
                        array[j - 1] = array[j];
                        array[j] = temp;
                        finished = false;
                    }
                }
                even = true;
            }
        }
    }
}

class InsertionSorter extends Sorter {
    @Override
    public void sort(int[] array) {
        for (int k = 1; k < array.length; k++) {
            // Insert element k into its correct position, so that
            //   array[0] <= array[1] <= ... <= array[k-1].
            int temp = array[k];
            int j;
            for (j = k - 1; j >= 0 && array[j] > temp; j--) {
                array[j + 1] = array[j];
            }
            array[j + 1] = temp;
        }
    }
}

class JavaSorter extends Sorter {
    public void sort(int[] array) {
        Arrays.sort(array);
    }
}

class CountingSorter extends Sorter {
    @Override
    public int[] getRandomArray(int nElements) {
        int[] array = new int[nElements];
        Random random = new Random();
        for (int k = 0; k < nElements; k++) {
            array[k] = random.nextInt(10);
        }
        return array;
    }

    @Override
    public void sort(int[] array) {
        int[] counts = new int[10];
        for (Integer i : array) {
            counts[i] += 1;
        }
        int index = 0;
        for (int i = 0; i < 10; i += 1) {
            while (counts[i] > 0) {
                array[index] = i;
                counts[i] -= 1;
                index += 1;
            }
        }
    }
}
