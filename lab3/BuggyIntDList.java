/**
 * @author Vivant Sakore on 1/29/2020; updated by Linda Deng (1/26/2022)
 */

public class BuggyIntDList extends IntDList {

    /**
     * @param values creates a BuggyIntDList with ints values
     */
    public BuggyIntDList(Integer... values) {
        super(values);
    }


    /**
     * Merge IntDList `l` into the calling IntDList
     * Assume that the two IntDLists being merged are sorted
     * individually before merge.
     * The resulting IntDList must also be sorted in ascending order.
     *
     * @param l Sorted IntDList to merge
     */
    public void mergeIntDList(IntDList l) {
        _front = sortedMerge(this._front, l._front);
        if (this._back._next != null) {
            this._back = l._back;
        }
    }

    /**
     * Recursively merge nodes after value comparison.
     *
     * @param d1 Node 1
     * @param d2 Node 2
     * @return Nodes arranged in ascending sorted order
     */
    private DNode sortedMerge(DNode d1, DNode d2) {

        // FIXME: Below code has multiple problems. Debug the code to implement correct functionality.

        // ------ WRITE ADDITIONAL CODE HERE AND ONLY HERE (IF NEEDED) ------

        // ------------------------------------------------------------------

        if (d1._val <= d2._val) {
            d1._next = sortedMerge(d1, d2._next);   // FIXME: Replace this line (if needed). HINT: Step Into(F7) using debugger and try to figure out what it does.
            d1._next._prev = d1;
            d1._prev = null;
            return d1;
        } else {
            d2._next = sortedMerge(d1._next, d2);   // FIXME: Replace this line (if needed). HINT: Step Into(F7) using debugger and try to figure out what it does.
            d2._next._prev = d2;
            d2._prev = null;
            return d2;
        }
    }


    /**
     * Reverses IntDList in-place (destructive). Does not create a new IntDList.
     */
    public void reverse() {

        // FIXME: Below code has multiple problems. Debug the code to implement correct functionality.

        DNode temp = null;
        DNode p = _front;

        // HINT: What does this while loop do? Use Debugger and Java Visualizer to figure out.
        while (p != null) {
            temp = p._prev;
            p._prev = p._next;
            p._next = temp;
            p = p._next;        // FIXME: Replace this line (if needed). HINT: Use debugger and Java Visualizer to figure out what it does.
        }

        // HINT: What does this if block do? Use Debugger and Java Visualizer to figure out.
        if (temp != null) {
            // ------ WRITE ADDITIONAL CODE HERE AND ONLY HERE (IF NEEDED) -----

            // -----------------------------------------------------------------
            _front = temp._next;    // FIXME: Replace this line (if needed). HINT: Use debugger and Java Visualizer to figure out what it does.
        }
    }
}
