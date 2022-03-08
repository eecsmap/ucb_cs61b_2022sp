package timing;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortTiming {

    private static final int N_TRIALS = 50;
    private static final int BY = 100;
    private static final int N_REPEATS = 10;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int ntrials, by, nrepeats;
                ntrials = by = nrepeats = 0;
                if(args.length == 0 ) {
                    ntrials = N_TRIALS;
                    by = BY;
                    nrepeats = N_REPEATS;
                } else if (args.length == 3) {
                    ntrials = Integer.parseInt(args[0]);
                    by = Integer.parseInt(args[1]);
                    nrepeats = Integer.parseInt(args[2]);
                } else {
                    System.err.println("args should either be empty or 3 values: ntrials, by, and nrepeats");
                    System.exit(1);
                }

                List<List<Double>> allscores = new ArrayList<>();

                Sorter[] sorters = {
                        new JavaSorter(),
                        new BubbleSorter(),
                        new WipingBubbleSorter(),
                        new InsertionSorter(),
                        new CountingSorter()};

                String[] dataLabels = {"timing.JavaSorter", "timing.BubbleSorter", "timing.WipingBubbleSorter", "timing.InsertionSorter", "timing.CountingSorter"};

                for (int nsorts = 0; nsorts < sorters.length; nsorts++) {
                    List<Double> scores = sorters[nsorts].score(by, ntrials, nrepeats);
                    allscores.add(scores);
                }

                List<Double> xVals = new ArrayList<>();
                int min = 5;
                int max = min + by * ntrials;

                for (int i = 0; i < ntrials; i += 1) {
                    xVals.add(5.0 + i  * by);
                }

                GraphUtil mainPanel = new GraphUtil(allscores, xVals, Arrays.asList(dataLabels), "Number of Repetitions: " + nrepeats,"Size of Array", "Microseconds");
                mainPanel.showGraph();
            }
        });
    }
}
