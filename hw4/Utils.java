import java.util.List;
import java.util.LinkedList;
/** Utility functions for hw4. Do not modify this file.
 *  If you need additional utilities, please make your
 *  own class.
 *  @author Josh Hug
 */

public class Utils {
    /** Make List<Integer> from integer ARGS, e.g.
     *  List<Integer> x = createList(1, 6, 3, 2, 4);
     *  Returns this new list.
     */
    public static List<Integer> createList(Integer... args) {
        LinkedList<Integer> L = new LinkedList<Integer>();
        for (int k = 0; k < args.length; k += 1) {
            L.add(args[k]);
        }
        return L;
    }

    /** Prints an integer list L. */
    public static void printList(List<Integer> L) {
        for (int x : L) {
            System.out.print(x + " ");
        }
        System.out.println();
    }
}
