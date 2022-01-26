package blocks;

import java.util.Arrays;
import java.util.Formatter;

import static blocks.Utils.positions;

/** A piece that may be added to a Blocks board.  Essentially, a Piece is a
 *  set of positions relative to its reference point that contain filled
 *  squares.   The reference point of a piece is the upper left corner
 *  of the smallest rectangle enclosing the filled squares of that piece.
 *  Adding a Piece to the Blocks board fills a set of previously open grid
 *  cells corresponding to the positions stored in the Piece, after selecting
 *  a position on the board to correspond to the Piece's reference point.
 *  @author
 */
class Piece {

    /** Maximum dimensions of any Piece. */
    static final int MAX_PIECE_WIDTH = 4,
        MAX_PIECE_HEIGHT = 4;

    /** A new Piece whose positions are denoted by PIECE.  PIECE contains
     *  a sequence of one or more rows separated by whitespace, where
     *  all rows are of the same length and contain only the characters
     *  "*" (denoting a filled grid square), and "." (denoting an empty
     *  grid square).  At least one cell in the first and last row and one
     *  cell in the first and last column of the Piece should be filled.
     */
    Piece(String piece) {
        _positions = positions(piece);
        assert positionsCheck();
    }

    /** Return the width of this Piece. */
    int width() {
        return _positions[0].length;
    }

    /** Return the height of this Piece. */
    int height() {
        return _positions.length;
    }

    /** Return true iff (ROW, COL) is a position in this Piece and is
     *  filled.  ROW and COL are relative to this Piece's reference point. */
    boolean get(int row, int col) {
        return true; // FIXME
    }

    /** Return true iff _positions meets all the requirements for a correctly
     *  formed piece, with at least one filled square in the top and bottom
     *  rows and the left and right columns. */
    private boolean positionsCheck() {
        if (_positions.length > MAX_PIECE_HEIGHT
            || _positions[0].length > MAX_PIECE_WIDTH) {
            return false;
        }

        boolean ok1, ok2;
        ok1 = false;
        for (boolean b : _positions[0]) {
            ok1 |= b;
        }
        if (!ok1) {
            return false;
        }
        ok1 = false;
        for (boolean b : _positions[_positions.length - 1]) {
            ok1 |= b;
        }
        if (!ok1) {
            return false;
        }
        ok1 = ok2 = false;
        for (boolean[] row : _positions) {
            ok1 |= row[0];
            ok2 |= row[row.length - 1];
        }
        if (!(ok1 & ok2)) {
            return false;
        }
        return true;
    }

    /** Return an external representation of this Piece. */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        String sep;
        sep = "";
        for (int row = 0; row < height(); row += 1) {
            out.format(sep);
            sep = System.getProperty("line.separator");
            for (int col = 0; col < width(); col += 1) {
                out.format(get(row, col) ? "*" : ".");
            }
        }
        return out.toString();
    }

    @Override
    public boolean equals(Object obj) {
        Piece p = (Piece) obj;
        return Utils.arrayEquals(_positions, p._positions);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_positions);
    }

    /** The filled squares of this Piece. */
    private boolean[][] _positions;

}
