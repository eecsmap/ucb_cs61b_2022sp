package lists;

/** Scheme-like pairs that can be used to form a list of
 *  integers.
 *  @author P. N. Hilfinger
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

    /** A List with null tail, and head = 0. */
    public IntList() {
        /* NOTE: public IntList () { }  would also work. */
        this (0, null);
    }

    /** Returns a new IntList containing the ints in ARGS. */
    public static IntList list(Integer ... args) {
        IntList sentinel = new IntList(0, null);

        IntList p;
        p = sentinel;
        for (Integer x : args) {
            p.tail = new IntList(x, null);
            p = p.tail;
        }
        return sentinel.tail;
    }

    /** Returns a new IntList containing the ints A. */
    public static IntList list(int[] A) {
        IntList sentinel = new IntList(0, null);

        IntList p;
        p = sentinel;
        for (int x : A) {
            p.tail = new IntList(x, null);
            p = p.tail;
        }
        return sentinel.tail;
    }


    /** Returns true iff X is an IntList or int[] containing the same
     *  sequence of ints as THIS. */
    public boolean equals(Object x) {
        if (x instanceof IntList) {
            IntList L = (IntList) x;
            IntList p;
            for (p = this; p != null && L != null; p = p.tail, L = L.tail) {
                if (p.head != L.head) {
                    return false;
                }
            }
            if (p == null && L == null) {
                return true;
            }
        } else if (x instanceof int[]) {
            int[] A = (int[]) x;
            IntList p;
            int i;
            for (i = 0, p = this; i < A.length && p != null;
                 i += 1, p = p.tail) {
                if (A[i] != p.head) {
                    return false;
                }
            }
            if (i == A.length && p == null) {
                return true;
            }
        }
        return false;
    }

    /** Returns a readable String for THIS. */
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("[");
        for (IntList L = this; L != null; L = L.tail) {
            b.append(" " + L.head);
        }
        b.append("]");
        return b.toString();
    }

    @Override
    public int hashCode() {
        return head;
    }
}
