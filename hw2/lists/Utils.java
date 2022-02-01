package lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

/** Various functions for dealing with arrays:
 *  * int[] B; ... B = Utils.readIntArray();
 *       will, for example, read the input
 *          [ 1, 2, 3 ]
 *       from System.in and set B to point to the 3-element array
 *       whose elements are 1, 2, and 3.
 *
 *  * int[][] B; ... B = Utils.readIntArray2();
 *       reads a 2-D array (array of array of ints) from System.in,
 *       in the form
 *           [ [ 1, 2, 3 ], [ 0, 12, 14 ], [ 42 ] ]
 *       Each inner [] segment is one row.  Rows need not be the same
 *       length.
 *  * Utils.print(A)
 *       writes a representation of A to System.out. A may be an int[]
 *       or int[][].
 *  * Utils.equals (A, B), where A and B are 1- or 2-D arrays of ints
 *       is true iff both represent the same sequences of values.
 *  * Utils.toString(A), converts to String with printed form.

 *  * IntList B; ... B = Utils.readIntList();
 *       will, for example, read the input
 *          [ 1, 2, 3 ]
 *       from System.in and set B to point to the 3-element list
 *       whose elements are 1, 2, and 3.
 *
 *  * IntListList B; ... B = Utils.readIntListList();
 *       reads a 2-D list (list of list of ints) from System.in,
 *       in the form
 *           [ [ 1, 2, 3 ], [ 0, 12, 14 ], [ 42 ] ]
 *       Each inner [] segment is one row.  Rows need not be the same
 *       length.
 *
 *  * Utils.reverse(A),
 *  * Utils.dreverse(A) (destructive).
 *  * Utils.toList(V) (convert int[] to IntList, int[][] to IntListList).
 *  * Utils.equals(L1, L2) equality test (L2 can also be an array).
 * @author P. N. Hilfinger
 */

public class Utils {

    /* ARRAYS */

    /** Read an array of integers from System.in (the standard input).
     *  The input must consist of a left curly brace ("["), followed by a
     *  sequence of integers separated by commas or blanks, followed by a
     *  closing right curly brace ("]").  Returns null if an end-of-file
     *  or input format error occurs. */
    public static int[] readIntArray() {
        return readIntArray(defaultScanner());
    }

    /** Returns as for readIntArray(), but reads from IN. */
    public static int[] readIntArray(Scanner in) {
        if (!in.hasNext("\\[.*")) {
            return null;
        }

        String data =
            in.findWithinHorizon("\\[\\s*((\\d+\\s*,?\\s*)*)\\]|(\\S)", 0);

        if (data == null || in.match().group(1) == null) {
            throw new NoSuchElementException("badly formed input");
        }

        StringTokenizer ints =
            new StringTokenizer(in.match().group(1), ", \t\n");

        int[] A = new int[ints.countTokens()];
        for (int i = 0; ints.hasMoreTokens(); i += 1) {
            A[i] = Integer.parseInt(ints.nextToken());
        }
        return A;
    }

    /** Returns readIntArray2 applied to System.in. */
    public static int[][] readIntArray2() {
        return readIntArray2(defaultScanner());
    }

    /** Read and return an array of arrays of ints from IN.  The input
     *  has the form of a sequence of arrays of double (in the format
     *  required by readDoubleArray, above) separated by commas or whitespace,
     *  and surrounded in braces ("[", "]").  For example
     *     [ [ 0, 1 ], [ ], [ 3, 1, 4 ] ]
     *  is an array of three rows, the first of length 2, the second of length
     *  0 and the third of length 3. */
    public static int[][] readIntArray2(Scanner in) {
        if (!in.hasNext()) {
            return null;
        }

        if (!in.hasNext("\\[.*")) {
            throw new NoSuchElementException("badly formed input");
        }

        in.findWithinHorizon("\\[", 0);

        List<int[]> result = new ArrayList<int[]>();

        while (true) {
            int[] item = readIntArray(in);
            if (item == null) {
                break;
            }
            result.add(item);
            if (!in.hasNext(",.*")) {
                break;
            }
            in.findWithinHorizon(",", 0);
        }

        if (!in.hasNext("\\].*")) {
            throw new NoSuchElementException("badly formed input");
        }
        in.findWithinHorizon("\\]", 0);

        return result.toArray(new int[result.size()][]);
    }

    /** Returns a Java array initializer that gives A. */
    public static String toString(int[] A) {
        return java.util.Arrays.toString(A);
    }

    /** Write A to the standard output. */
    public static void print(int[] A) {
        System.out.print(toString(A));
    }

    /** Returns a Java array initializer that gives A. */
    public static String toString(int[][] A) {
        return Arrays.deepToString(A);
    }

    /** Write A to the standard output. */
    public static void print(int[][] A) {
        System.out.print(toString(A));
    }

    /** Returns the subarray of A consisting of the LEN items starting
     *  at index K. */
    public static int[] subarray(int[] A, int k, int len) {
        int[] result = new int[len];
        System.arraycopy(A, k, result, 0, len);
        return result;
    }

    /** True iff the sequence of items in A0 of length LEN>=0 starting at #K0 is
     *  the same as the sequence in A1 starting at #K1.   Returns false if
     *  the items to be compared are missing on either array. */
    public static boolean equals(int[] A0, int k0, int[] A1, int k1, int len) {
        if (k0 < 0 || k1 < 0 || k0 + len > A0.length || k1 + len > A1.length) {
            return false;
        } else {
            return equals(subarray(A0, k0, len), subarray(A1, k1, len));
        }
    }

    /** Return true iff the sequences of items in A and B are equal. */
    public static boolean equals(int[] A, int[] B) {
        return Arrays.equals(A, B);
    }

    /** Return true iff the sequences of arrays in A and B are equal. */
    public static boolean equals(int[][] A, int[][] B) {
        return Arrays.deepEquals(A, B);
    }

    /* INTLISTS */

    /** Sentinel indicating an end of file (as opposed to empty list). */
    static final IntList INTLIST_EOF = new IntList(0, null);
    /** Sentinel indicating an end of file (as opposed to empty list). */
    static final IntListList IntListList_EOF = new IntListList(null, null);

    /** Read an list of integers from System.in (the standard input).
     *  The input must consist of a left curly brace ("["), followed by a
     *  sequence of integers separated by commas or blanks, followed by a
     *  closing right curly brace ("]").  Returns INTLIST_EOF if an
     *  end-of-file or input format error occurs. */
    public static IntList readIntList() {
        return readIntList(defaultScanner());
    }

    /** Return as for readIntList(), but reads from IN. */
    public static IntList readIntList(Scanner in) {
        int[] A = readIntArray(in);
        if (A == null) {
            return INTLIST_EOF;
        } else {
            return toList(A);
        }
    }

    /** Return the IntList whose sequence of items is the same as that in A. */
    public static IntList toList(int[] A) {
        return toList(A, 0, A.length - 1);
    }

    /** Return the IntList whose sequence of items is the same as that in
     *  items L through U of array A. */
    public static IntList toList(int[] A, int L, int U) {
        IntList result;
        result = null;
        for (int i = U; i >= L; i -= 1) {
            result = new IntList(A[i], result);
        }
        return result;
    }

    /** Read an list of integers from System.in (the standard input).
     *  The input must consist of a left curly brace ("["), followed by a
     *  sequence of integers separated by commas or blanks, followed by a
     *  closing right curly brace ("]").  Returns INTLIST_EOF if an
     *  end-of-file or input format error occurs. */
    public static IntListList readIntListList() {
        return readIntListList(defaultScanner());
    }

    /** Return as for readIntListList(), but reads from IN. */
    public static IntListList readIntListList(Scanner in) {
        int[][] A = readIntArray2(in);
        if (A == null) {
            return IntListList_EOF;
        } else {
            return toList(A);
        }
    }

    /** Return the IntListList whose sequence of items is the same as that in A. */
    public static IntListList toList(int[][] A) {
        return toList(A, 0, A.length - 1);
    }

    /** Return the IntListList whose sequence of items is the same as that in
     *  items L through U of array A. */
    public static IntListList toList(int[][] A, int L, int U) {
        IntListList result;
        result = null;
        for (int i = U; i >= L; i -= 1) {
            result = new IntListList(toList(A[i]), result);
        }
        return result;
    }

    /** Return a Java array initializer containing the elements of A. */
    public static String toString(IntList A) {
        StringBuffer result = new StringBuffer();
        result.append("{ ");
        for (IntList L = A; L != null; L = L.tail) {
            if (L != A) {
                result.append(", ");
            }
            result.append(L.head);
        }
        result.append(" }");
        return result.toString();
    }

    /** Return the number of elements in L. */
    public static int length(IntList L) {
        int n;
        IntList p;
        for (p = L, n = 0; p != null; p = p.tail) {
            n += 1;
        }
        return n;
    }

    /** Returns the number of elements in L. */
    public static int length(IntListList L) {
        int n;
        IntListList p;
        for (p = L, n = 0; p != null; p = p.tail) {
            n += 1;
        }
        return n;
    }

    /** Returns the reverse of L. */
    public static IntList reverse(IntList L) {
        IntList R;
        for (R = null; L != null; L = L.tail) {
            R = new IntList(L.head, R);
        }
        return R;
    }

    /** Returns the destructive reverse of L; original L is destroyed. */
    public static IntList dreverse(IntList L) {
        IntList R, L1;
        for (R = null; L != null; L = L1) {
            L1 = L.tail;
            L.tail = R;
            R = L;
        }
        return R;
    }

    /** Returns the reverse of L. */
    public static IntListList reverse(IntListList L) {
        IntListList R;
        for (R = null; L != null; L = L.tail) {
            R = new IntListList(L.head, R);
        }
        return R;
    }

    /** Returns the destructive reverse of L; original L is destroyed. */
    public static IntListList dreverse(IntListList L) {
        IntListList R, L1;
        for (R = null; L != null; L = L1) {
            L1 = L.tail;
            L.tail = R;
            R = L;
        }
        return R;
    }

    /** Returns true iff the sequence of items in A is the same as
     *  the sequence in B. */
    public static boolean equals(IntList A, int[] B) {
        if (A == null) {
            return B.length == 0;
        } else {
            return A.equals(B);
        }
    }

    /** Returns true iff the sequence of items in A is the same as
     *  the sequence in B. */
    public static boolean equals(IntListList A, int[][] B) {
        if (A == null) {
            return B.length == 0;
        } else {
            return A.equals(B);
        }
    }

    /** Returns true iff the sequence of items in A is the same as
     *  the sequence in B. */
    public static boolean equals(IntList A, IntList B) {
        if (A == null) {
            return B == null;
        } else {
            return A.equals(B);
        }
    }

    /** Returns true iff the sequence of items in A is the same as
     *  the sequence in B. */
    public static boolean equals(IntListList A, IntListList B) {
        if (A == null) {
            return B == null;
        } else {
            return A.equals(B);
        }
    }

    /** Returns the current default Scanner for System.in. */
    private static Scanner defaultScanner() {
        if (inp == null) {
            inp = new Scanner(System.in);
        }
        return inp;
    }

    /** Default Scanner used for input from System.in. */
    private static Scanner inp;

}
