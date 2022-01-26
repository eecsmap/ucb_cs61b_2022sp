package blocks;

import java.util.ArrayList;
import java.util.Formatter;

import static blocks.Utils.*;

/** The state of a Blocks puzzle, which consists of a rectangular grid
 *  of cells, plus a set of pieces, called the "hand".  The cells have
 *  0-based column and row numbers, and each contains the value true or
 *  false, indicating whether it is currently filled (true) or empty (false).
 *  Rows are numbered from the top (row 0) down, and columns from left to
 *  right. Methods allow querying the contents of the cells,
 *  adding pieces to the hand, moving pieces from the hand to the board,
 *  determining whether a puzzle piece may be added at a given position or
 *  anywhere, and undoing or redoing moves.
 *
 *  @author
 */
class Model {

    /** A Model of WIDTH x HEIGHT cells that are initially OPEN, and with
     *  a hand containing no pieces (null or otherwise).
     *  Requires that 0 < WIDTH and 0 < HEIGHT. */
    Model(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw badArgs("invalid width or height");
        }
        _height = height;
        _width = width;
        _cells = new boolean[_height][_width];
        // FIXME
        _current = _lastHistory = -1;
    }

    /** Initializes a copy of MODEL. Does not modify or share structure with
     *  MODEL.  */
    Model(Model model) {
        _width = model.width(); _height = model.height();
        _cells = new boolean[_height][_width];
        deepCopy(model._cells, _cells);
        // FIXME
        _current = model._current;
        _lastHistory = model._lastHistory;
        for (GameState g : model._history.subList(0, model._lastHistory + 1)) {
            _history.add(new GameState(g));
        }
    }

    /** Returns the width (number of columns of cells) of the board. */
    int width() {
        return _width;
    }

    /** Returns the height (number of rows of cells) of the board. */
    int height() {
        return _height;
    }

    /** Returns the number of pieces dealt to the hand since this Model
     *  was created or the hand was last cleared. */
    int handSize() {
        return 0; // FIXME
    }

    /** Return piece #K (numbering from 0) in the current hand. Returns
     *  null unless 0 <= K < handSize() and the indicated Piece has not
     *  already been used. */
    Piece piece(int k) {
        if (k < 0 || k >= _hand.size()) {
            return null;
        }
        return null; // FIXME
    }

    /** Return true iff PIECE may be added to the board with its
     *  reference point at (ROW, COL). False if PIECE == null. */
    boolean placeable(Piece piece, int row, int col) {
        if (piece == null) {
            return false;
        }
        // FIXME
        return true;
    }

    /** Return true iff PIECE may be added to the board at some position. */
    boolean placeable(Piece piece) {
        // FIXME
        return false;
    }

    /** Return true iff piece(K) may be added to the board with its
     *  reference point at (ROW, COL). False if piece(K) == null. */
    boolean placeable(int k, int row, int col) {
        return placeable(piece(k), row, col);
    }

    /** Return true iff piece(K) may be added to the board at some position. */
    boolean placeable(int k) {
        return placeable(piece(k));
    }

    /** Place PIECE on the board at (ROW, COL), assuming it is placeable
     *  there. Also updates score(). */
    void place(Piece piece, int row, int col) {
        assert placeable(piece, row, col);
        // FIXME
    }

    /** Place piece(K) on the board at (ROW, COL), assuming it is placeable
     *  there, and remove it from the hand (without renumbering the rest of
     *  the hand). Also updates score(). */
    void place(int k, int row, int col) {
        place(piece(k), row, col);
        // FIXME
    }

    /** Return an array COUNTS such that COUNTS[0][r] is the number of
     *  filled grid squares in row r and COUNTS[1][c] is the number of
     *  filled grid cells in column c. */
    int[][] rowColumnCounts() {
        int[][] result = new int[][] { new int[_height], new int[_width] };
        // FIXME
        return result;
    }

    /** Clear all cells currently in completely filled rows and columns.
     *  Also updates score(). */
    void clearFilledLines() {
        int nrows, ncols;
        int[][] counts = rowColumnCounts();
        nrows = ncols = 0;
        // FIXME
        _score += scoreClearedLines(nrows, ncols);
    }

    /** Return the score increase caused by clearing full lines, given that
     *  NROWS is the number of rows cleared and NCOLS is the number
     *  of columns cleared. */
    private int scoreClearedLines(int nrows, int ncols) {
        return 0; // FIXME
    }

    /** Return true iff the current hand is empty (i.e., piece(k) is null
     *  for all k). */
    boolean handUsed() {
        // FIXME
        return true;
    }

    /** Empty all Pieces from the current hand. */
    void clearHand() {
        // FIXME
    }

    /** Add PIECE to the current hand.  Assumes PIECE is not null. */
    void deal(Piece piece) {
        // FIXME
    }

    /** Return current score. */
    int score() {
        return _score;
    }

    /** Save the current state on the undo history. */
    void pushState() {
        _current += 1;
        _lastHistory = _current;
        if (_current >= _history.size()) {
            _history.add(new GameState());
        }
        _history.get(_current).saveState();
    }

    /** Undo to the state saved by the last call to pushState, if any.
     *  Does nothing if at the initial board. */
    void undo() {
        if (_current > 0) {
            return; // FIXME
        }
    }

    /** Redo one move, if possible. Does nothing if
     *  there are no available undone boards. */
    void redo() {
        if (_current < _lastHistory) {
            return; // FIXME
        }
    }

    /** Returns true if this puzzle round is over because the hand is not empty
     *  but contains only Pieces that cannot be placed.  */
    boolean roundOver() {
        return true; // FIXME
    }

    /** Returns true iff (ROW, COL) is a valid cell location. */
    boolean isCell(int row, int col) {
        return 0 <= col && col < width() && 0 <= row && row < height();
    }

    /** Returns true iff location (ROW, COL) is not a position on the board
     *  or is currently filled.   That is, it returns true iff one may not
     *  add a Piece that would fill location (ROW, COL). */
    boolean get(int row, int col) {
        return true; // FIXME
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        for (int row = 0; row < height(); row += 1) {
            for (int col = 0; col < width(); col += 1) {
                out.format(get(row, col) ? "*" : ".");
            }
            out.format("%n");
        }
        out.format("Score: %d.%n", score());
        out.format("Hand:%n%s", handToString());
        return out.toString();
    }

    /** Return a printable representation of the current hand. */
    String handToString() {
        Formatter out = new Formatter();
        for (int k = 0; k < handSize(); k += 1) {
            out.format("%n%d:%n", k);
            if (piece(k) == null) {
                out.format("   empty%n");
            } else {
                out.format("%s", piece(k).toString().indent(3));
            }
        }
        return out.toString();
    }

    /** Dimensions of board. */
    private int _width, _height;

    /** The current board contents, indexed by row in the first index and column
     *  in the second. */
    private boolean[][] _cells;

    /** The current hand.  Items that have already been used are null. */
    private ArrayList<Piece> _hand = new ArrayList<>();

    /** Current score: total number of cells that have been filled by
     *  place(...) plus the total number emptied by clearFilledLines(). */
    private int _score;

    /** The number of calls to clearFilledLines() that have cleared one or
     *  more cells since the beginning of the round or that last call to
     *  clearFilledLines that did not change any cells.   Used in score
     *  calculation. */
    private int _streakLength;

    /** Represents enough of the state of a game to allow undoing and
     *  redoing of moves. */
    private class GameState {

        /* GameState is an "inner class", meaning that every GameState is
         * attached to the particular Model object that created it, and
         * has access to the fields of that model. So references to,
         * e.g., _width in a GameState refer to the _width field of the
         * Model that created the GameState. */

        /** A holder for the _cells and _active instance variables of this
         *  Model. */
        GameState() {
            _savedCells = new boolean[_height][_width];
            _savedScore = 0;
            _savedStreakLength = 0;
        }

        /** A copy of STATE. */
        GameState(GameState state) {
            _savedCells = new boolean[_height][_width];
            deepCopy(state._savedCells, _savedCells);
            _savedHand.addAll(state._savedHand);
            _savedScore = state._savedScore;
            _savedStreakLength = state._savedStreakLength;
        }

        /** Initialize to the current state of the Model. */
        void saveState() {
            deepCopy(_cells, _savedCells);
            _savedHand.clear();
            _savedHand.addAll(_hand);
            _savedScore = _score;
            _savedStreakLength = _streakLength;
        }

        /** Restore the current Model's state from our saved state. */
        void restoreState() {
            deepCopy(_savedCells, _cells);
            _hand.clear();
            _hand.addAll(_savedHand);
            _score = _savedScore;
            _streakLength = _savedStreakLength;
        }

        /** Contents of board. */
        private boolean[][] _savedCells;
        /** Contents of hand. */
        private ArrayList<Piece> _savedHand = new ArrayList<>();
        /** Current score. */
        private int _savedScore;
        /** Number of consecutive moves that had bonuses. */
        private int _savedStreakLength;
    }

    /** A sequence of puzzle states. At any given time, _history[_current]
     *  is the last puzzle state saved by pushState() (and not undone).
     *  _history[_current+1] through _history[_lastHistory] are undone
     *  states that can be redone.  _lastHistory is reset to _current after
     *  each move.  _history only expands: there can be more than
     *  _lastHistory+1 elements in it at any time, with those following
     *  _lastHistory being available for re-use. */
    private ArrayList<GameState> _history = new ArrayList<>();

    /** The position of the current state in _history.  This is always
     *  non-negative and <=_lastHistory.  */
    private int _current;

    /** The index of the last valid state in _history, including those
     *  that can be redone (with indices >_current). */
    private int _lastHistory;
}
