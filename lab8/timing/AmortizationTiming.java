package timing;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AmortizationTiming {

    private static final int MAX_SIZE = 1024;
    private static final int N_LISTS = 1000;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int maxSize, nLists;
                maxSize = nLists = 0;
                boolean accumulate = false;
                if(args.length == 0 ) {
                    maxSize = MAX_SIZE;
                    nLists = N_LISTS;
                } else if (args.length == 3) {
                    maxSize = Integer.parseInt(args[0]);
                    nLists = Integer.parseInt(args[1]);
                    accumulate = args[2].equals("-accum");
                } else {
                    System.err.println("args should either be empty or 3 values: maxSize, nLists, and accumulate");
                    System.exit(1);
                }

                List<List<Double>> allscores = new ArrayList<>();

                GrowList[] lists = {
                        new GeomGrowList(),
                        new ArithGrowList(),
                        new JavaGrowList()};

                String[] dataLabels = {"timing.GeomGrowList", "timing.ArithGrowList", "timing.JavaGrowList"};

                for (int i = 0; i < lists.length; i += 1) {
                    List<Double> scores;
                    if (accumulate) {
                        scores = lists[i].accumScore(maxSize, nLists);
                        System.out.printf("Average time of one addition for %s: %f\n", dataLabels[i], scores.get(scores.size() - 1) / scores.size());
                    } else {
                        scores = lists[i].score(maxSize, nLists);
                    }
                    allscores.add(scores);
                }

                List<Double> xVals = new ArrayList<>();

                for (int i = 1; i <= maxSize; i += 1) {
                    xVals.add(i * 1.0);
                }

                GraphUtil mainPanel = new GraphUtil(allscores, xVals, Arrays.asList(dataLabels), "Amortization Timing","Size of Array", "Microseconds");
                mainPanel.showGraph();
            }
        });
    }
}
