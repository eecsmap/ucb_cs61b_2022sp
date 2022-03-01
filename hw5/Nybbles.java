/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author
 */
public class Nybbles {
    /** Mask out a nybble. */
    private static final int NYBBLE_MASK = 0xf;
    /** Mask that takes a modulo base 8  */
    private static final int MOD_8_MASK = 0b111;
    /** Shift offset that will multiply by four  */
    private static final int MULT_4_SHIFT = 2;

    // FIXME: Change these values!

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 0;
    /** Number of nybbles per word  */
    private static final int NYBBLES_PER_INT = 0;
    /** Bits per integer */
    private static final int BITS_PER_INT = 0;
    /** Bits per nybble */
    private static final int BITS_PER_NYBBLE = 0;

    /** Return an array of size N.
    public Nybbles(int N) {
        // FIXME: Change the line below
        _data = new int[0];
        _n = N;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }

    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
            return 0; // REPLACE WITH SOLUTION
        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
            _data[0] = 0; // REPLACE WITH SOLUTION
        }
    }

    /** DON'T CHANGE OR ADD TO THESE.*/
    /** Size of current array (in nybbles). */
    private int _n;
    /** The array data, packed 8 nybbles to an int. */
    private int[] _data;
}
