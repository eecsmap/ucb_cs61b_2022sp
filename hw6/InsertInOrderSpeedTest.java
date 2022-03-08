import java.util.TreeSet;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.util.Scanner;

/** Performs a timing test on three different set implementations.
 *  @author Josh Hug
 */
public class InsertInOrderSpeedTest {
    /** Returns time needed to put N random strings of length L into the
      * StringSet SS. */
    public static double insertInOrder(StringSet ss, int N) {
        Stopwatch sw = new Stopwatch();
        String s = "cat";
        for (int i = 0; i < N; i++) {
            s = StringUtils.nextString(s);
            ss.put(s);
        }
        return sw.elapsedTime();
    }

    /** Returns time needed to put N random strings of length L into the
      * Set TS. */
    public static double insertInOrder(Set<String> ss, int N) {
        Stopwatch sw = new Stopwatch();
        String s = "cat";
        for (int i = 0; i < N; i++) {
            s = StringUtils.nextString(s);
            ss.add(s);
        }
        return sw.elapsedTime();
    }

    /** Prints the result of a random timing test on a StringSet. */
    public static void printInOrderTimingTest(StringSet s, int N) {
        System.out.printf("Inserting %d in-order strings into a"
                          + " StringSet of type %s\n",
                          N, s.getClass().getName());
        double runTime = insertInOrder(s, N);
        System.out.printf("Took: %.2f sec.\n\n", runTime);
    }

    /** Prints the result of a random timing test on a StringSet. */
    public static void printInOrderTimingTest(Set<String> s, int N) {
        System.out.printf("Inserting %d in-order strings Strings into a"
                          + " StringSet of type %s\n",
                          N, s.getClass().getName());
        double runTime = insertInOrder(s, N);
        System.out.printf("Took: %.2f sec.\n\n", runTime);
    }


    /** Requests user input and performs tests of three different set
        implementations. ARGS is unused. */
    public static void main(String[] args) throws IOException {
        int N;
        if (args.length > 0) {
            N = Integer.parseInt(args[0]);
            System.out.printf("Testing %d in-order 10-character strings.%n",
                               N);
        } else {
            Scanner input = new Scanner(System.in);
            System.out.println("This program inserts in-order length 10 "
                               + "strings into various string-set "
                               + "implementations.");
            System.out.print("\nEnter # strings to insert: ");
            N = input.nextInt();
        }
        printInOrderTimingTest(new BSTStringSet(), N);
        printInOrderTimingTest(new ECHashStringSet(), N);
        printInOrderTimingTest(new TreeSet<String>(), N);
        printInOrderTimingTest(new HashSet<String>(), N);

    }
}
