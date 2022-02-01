package lists;

/** A list of IntLists.
 *  @author P. N. Hilfinger
 */
public class IntListList {
    /** A List with head HEAD0 and tail TAIL0. */
    public IntListList(IntList head0, IntListList tail0) {
        head = head0; tail = tail0;
    }

    /** A List with null head and tail. */
    public IntListList() {
        this (null, null);
    }

    /** First element of list. */
    public IntList head;
    /** Remaining elements of list. */
    public IntListList tail;

    /** Return a new IntListList containing the lists in ARGS. */
    public static IntListList list(IntList ... args) {
        IntListList sentinel = new IntListList(null, null);

        IntListList p;
        p = sentinel;
        for (IntList x : args) {
            p.tail = new IntListList(x, null);
            p = p.tail;
        }
        return sentinel.tail;
    }

    /** Return a new IntListList containing the lists of ints corresponding
     *  to the arrays in A. */
    public static IntListList list(int[][] A) {
        IntListList sentinel = new IntListList(null, null);

        IntListList p;
        p = sentinel;
        for (int[] x : A) {
            p.tail = new IntListList(IntList.list(x), null);
            p = p.tail;
        }
        return sentinel.tail;
    }

    /** Return true iff X is an IntListList or int[][] containing the
     *  same sequence of ints as THIS. */
    public boolean equals(Object x) {
        if (x instanceof IntListList) {
            IntListList L = (IntListList) x;
            IntListList p;
            for (p = this; p != null && L != null; p = p.tail, L = L.tail) {
                if ((p.head == null && L.head != null)
                    || (p.head != null && !p.head.equals(L.head))) {
                    return false;
                }
            }
            if (p == null && L == null) {
                return true;
            }
        } else if (x instanceof int[][]) {
            int[][] A = (int[][]) x;
            IntListList p;
            int i;
            for (i = 0, p = this; i < A.length && p != null;
                 i += 1, p = p.tail) {
                if ((p.head == null && A[i].length != 0)
                    || (p.head != null && !p.head.equals(A[i]))) {
                    return false;
                }
            }
            if (i == A.length && p == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("[");
        for (IntListList L = this; L != null; L = L.tail) {
            b.append(" " + L.head);
        }
        b.append("]");
        return b.toString();
    }

    @Override
    public int hashCode() {
        if (head == null) {
            return 0;
        } else {
            return head.hashCode();
        }
    }

}
