import java.util.Formatter;

/** Scheme-like pairs that can be used to form a list of integers.
 *  @author P. N. Hilfinger, Josh Hug, Melanie Cebula.
 */
public class IntList {

    /** First element of list. */
    public int head;

    /** Remaining elements of list. */
    public IntList tail;

    /** A List with head HEAD0 and tail TAIL0. */
    public IntList(int head0, IntList tail0) {
        head = head0;
        tail = tail0;
    }

    /** Constructs an IntList with null tail, and head = 0. */
    public IntList() {
        /* NOTE: public IntList () { }  would also work. */
        this(0, null);
    }

    /** Returns a list equal to L with all elements squared. Destructive. */
    public static void dSquareList(IntList L) {
        while (L != null) {
            L.head = L.head * L.head;
            L = L.tail;
        }
    }

    /** Returns a list equal to L with all elements squared. Non-destructive. */
    public static IntList squareListIterative(IntList L) {
        if (L == null) {
            return null;
        }
        IntList res = new IntList(L.head * L.head, null);
        IntList ptr = res;
        L = L.tail;
        while (L != null) {
            ptr.tail = new IntList(L.head * L.head, null);
            L = L.tail;
            ptr = ptr.tail;
        }
        return res;
    }

    /** Returns a list equal to L with all elements squared. Non-destructive. */
    public static IntList squareListRecursive(IntList L) {
        if (L == null) {
            return null;
        }
        return new IntList(L.head * L.head, squareListRecursive(L.tail));
    }

    /** DO NOT MODIFY ANYTHING ABOVE THIS LINE! */

    /** This method is run first when IntList.java is run. */
    public static void main(String[] args) {
        System.out.println("Hello, " + args[0]);

        // Creates a sample IntList.
        IntList L1 = list(1, 2, 3, 4, 5);

        //TODO:  Print out L1 using IntelliJ's 'sout' command.
    }

    /** DO NOT MODIFY ANYTHING BELOW THIS LINE! In fact, I wouldn't even
     * look below this line since it's likely to confuse you. Throughout the
     * semester, you will become familiar with what is happening below. */

    /** Checks if two IntLists are identical. */
    @Override
    public boolean equals(Object obj) {
        IntList otherList = (IntList) obj;
        for (IntList L1 = this, L2 = otherList; L1 != null; L1 = L1.tail, L2 = L2.tail) {
            if ((L2 == null) || (L1.head != L2.head)) {
                return false;
            }
        }
        return true;
    }

    /** Return an integer value such that if x1 and x2 represent two
     *  IntLists that represent identical sequences of ints, then
     *  x1.hashCode() == x2.hashCode().  (Any class that overrides
     *  equals should override this method,) */
    @Override
    public int hashCode() {
        return head;
    }

    /** Returns a new IntList containing the ints in ARGS. You are not
     * expected to read or understand this method. */
    public static IntList list(Integer ... args) {
        IntList result, p;

        if (args.length > 0) {
            result = new IntList(args[0], null);
        } else {
            return null;
        }

        int k;
        for (k = 1, p = result; k < args.length; k += 1, p = p.tail) {
            p.tail = new IntList(args[k], null);
        }
        return result;
    }

    /** Returns the sum of the integers contained in L.*/
    public static int sum(IntList L) {
        int sum = 0;
        IntList p = L;
        while (p != null) {
            sum = p.head;
            p = p.tail;
        }
        return sum;
    }

    /** If a cycle exists in A, return an integer equal to
     *  the item number of the location where the cycle is detected.
     *  If there is no cycle, returns 0. */
    private int detectCycles(IntList A) {
        IntList tortoise;
        IntList hare;

        if (A == null) {
            return 0;
        }

        tortoise = hare = A;

        for (int cnt = 0;; cnt += 1) {
            cnt += 1;
            if (hare.tail != null) {
                hare = hare.tail.tail;
            } else {
                return 0;
            }

            tortoise = tortoise.tail;

            if (tortoise == null || hare == null) {
                return 0;
            }

            if (hare == tortoise) {
                return cnt;
            }
        }
    }

    /** Return a printable representation of an IntList. */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        String sep;
        sep = "[";
        int cycleLocation = detectCycles(this);
        int cnt;

        cnt = 0;
        for (IntList p = this; p != null; p = p.tail) {
            out.format("%s%d", sep, p.head);
            sep = ", ";

            cnt += 1;
            if (cnt > cycleLocation && cycleLocation > 0) {
                out.format("... (cycle)");
                break;
            }
        }
        out.format("]");
        return out.toString();
    }

}
