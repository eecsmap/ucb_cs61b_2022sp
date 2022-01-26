package blocks;

import static java.lang.System.arraycopy;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.*;

/** Various utility methods.
 *  @author P. N. Hilfinger
 */
class Utils {

    /** Returns String.format(FORMAT, ARGS...). */
    static String msg(String format, Object... args) {
        return String.format(format, args);
    }

    /** Copy contents of SRC into DEST.  SRC and DEST must both be
     *  rectangular, with identical dimensions. */
    static void deepCopy(boolean[][] src, boolean[][] dest) {
        assert src.length == dest.length && src[0].length == dest[0].length;
        for (int i = 0; i < src.length; i += 1) {
            arraycopy(src[i], 0, dest[i], 0, src[i].length);
        }
    }

    /** Returns a deep copy of SRC: the result has no shared state with SRC. */
    static boolean[][] deepCopyOf(boolean[][] src) {
        boolean[][] result = new boolean[src.length][src[0].length];
        deepCopy(src, result);
        return result;
    }

    /** Returns true iff V1 and V2 have the same dimensions and each row of V1
     *  has the same contents as the corrsponding row of V2. */
    static boolean arrayEquals(boolean[][] v1, boolean[][] v2) {
        if (v1.length != v2.length) {
            return false;
        }
        for (int i = 0; i < v1.length; i += 1) {
            if (!Arrays.equals(v1[i], v2[i])) {
                return false;
            }
        }
        return true;
    }

    /** Check that the set of T's in EXPECTED is the same as that in ACTUAL.
     *  Use MSG as the error message if the check fails. */
    static <T> void assertSetEquals(String msg,
                                     Collection<T> expected,
                                     Collection<T> actual) {
        assertNotNull(msg, actual);
        assertEquals(msg, new HashSet<T>(expected), new HashSet<T>(actual));
    }

    /** Return an IllegalArgumentException whose message is formed from
     *  MSGFORMAT and ARGS as for String.format. */
    static IllegalArgumentException badArgs(String msgFormat, Object... args) {
        return new IllegalArgumentException(String.format(msgFormat, args));
    }

    /** PLACES must have the form of a sequence of one or more substrings
     *  of "*"s and "."  separated by whitespace, where all substrings arnbe
     *  of the same length.  Returns a rectangular array, P, in which P[r][c]
     *  is true iff the c'th character of the r'th row (0 indexed) is '*'. */
    static boolean[][] positions(String places) {
        assert places.matches("(\\s*([\\s*.]+))+\\s*$");
        String[] rows  = places.trim().split("[\t\n\r ]+");
        int nrows = rows.length;
        assert nrows > 0;
        int ncols = rows[0].length();
        boolean[][] result = new boolean[nrows][ncols];
        for (int r = 0; r < nrows; r += 1) {
            String row = rows[r];
            assert row.length() == ncols;
            for (int c = 0; c < ncols; c += 1) {
                result[r][c] = row.charAt(c) == '*';
            }
        }
        return result;
    }

    /** Return integer denoted by NUMERAL. */
    static int toInt(String numeral) {
        return Integer.parseInt(numeral);
    }

    /** Return long integer denoted by NUMERAL. */
    static long toLong(String numeral) {
        return Long.parseLong(numeral);
    }

    /** If debugging messages are on, print according to FORMAT and ARGS on
     *  standard error, as for System.printf. */
    static void debug(String format, Object... args) {
        if (_debuggingMessages) {
            System.err.printf(format, args);
            System.err.println();
        }
    }

    /** Print debugging messages iff ON, returning the previous value. */
    static boolean setDebuggingMessages(boolean on) {
        boolean prev = _debuggingMessages;
        _debuggingMessages = on;
        return prev;
    }

    /** True if debug(...) is printing debugging messages. */
    private static boolean _debuggingMessages;
}

