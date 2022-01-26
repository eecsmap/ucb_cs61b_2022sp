package blocks;

import static blocks.Utils.*;

/** The input/output and GUI controller for play of a Signpost puzzle.
 *  @author P. N. Hilfinger. */
public class Controller {

    /** The default number of squares on a side of the board. */
    static final int DEFAULT_SIZE = 8;

    /** The default size of a hand. */
    static final int DEFAULT_HAND_SIZE = 3;


    /** Controller for a game represented by MODEL, using COMMANDS as the
     *  the source of commands, and PUZZLES to supply puzzles.  If LOGGING,
     *  prints commands received on standard output.  If TESTING, prints
     *  the board when possibly changed.  If VIEW is non-null, update it
     *  at appropriate points when the model changes. */
    public Controller(View view,
                      CommandSource commands, PuzzleSource puzzles,
                      boolean logging, boolean testing) {
        _view = view;
        _commands = commands;
        _puzzles = puzzles;
        _logging = logging;
        _testing = testing;
        _active = true;
        setType(DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_HAND_SIZE);
    }

    /** Return true iff we have not received a Quit command. */
    boolean active() {
        return _active;
    }

    /** Clear the board and play one puzzle, until receiving a quit,
     *  new-game, or parameter-change request.  Update the viewer with
     *  each visible modification to the model. */
    void playPuzzle() {
        _model = new Model(_width, _height);
        if (!_puzzles.deal(_model, _handSize)) {
            _active = false;
            return;
        }
        _model.pushState();

        logHand();
        while (_active) {
            if (_view != null) {
                _view.update(_model);
            }

            String cmnd = _commands.getCommand();
            if (_logging || _testing) {
                System.out.println(cmnd);
            }
            String[] parts = cmnd.split("\\s+");
            switch (parts[0]) {
            case "QUIT":
                _active = false;
                return;
            case "NEW":
                return;
            case "SIZE":
                setType(toInt(parts[1]), toInt(parts[2]), toInt(parts[3]));
                return;
            case "SET":
                addPiece(toInt(parts[1]), toInt(parts[2]), toInt(parts[3]));
                break;
            case "SEED":
                _puzzles.setSeed(toLong(parts[1]));
                break;
            case "UNDO":
                undo();
                break;
            case "REDO":
                redo();
                break;
            case "BOARD":
                System.out.printf("==%n%s==%n", _model.toString());
                break;
            case "":
                break;
            default:
                System.err.printf("Bad command: '%s'%n", cmnd);
                break;
            }
        }
    }

    /** Add piece #P at (ROW, COL). */
    private void addPiece(int p, int row, int col) {
        if (_model.roundOver()) {
            return;
        }
        if (!_model.placeable(p, row, col)) {
            System.err.printf("Invalid placement of piece %d at (%d, %d)%n",
                              p, row, col);
        }
        _model.place(p, row, col);
        _model.clearFilledLines();
        if (_model.handUsed()) {
            if (!_puzzles.deal(_model, _handSize)) {
                _active = false;
            }
            logHand();
        }
        _model.pushState();
    }

    /** Set current puzzle parameters to dimensions WIDTH x HEIGHT,
     *  with HANDSIZE pieces in a hand. */
    private void setType(int width, int height, int handSize) {
        if (width <= 0 || height <= 0 || handSize <= 0) {
            throw badArgs("improper type parameters");
        }
        _width = width;
        _height = height;
        _handSize = handSize;
    }

    /** Back up one move, if possible.  Does nothing otherwise. */
    private void undo() {
        _model.undo();
    }

    /** Redo one move, if possible.  Does nothing otherwise. */
    private void redo() {
        _model.redo();
    }

    /** If logging, print a representation of the current hand. */
    private void logHand() {
        if (_logging) {
            System.out.printf("H[%n%s]%n", _model.handToString());
        }
    }

    /** The board. */
    private Model _model;

    /** Our view of _model. */
    private View _view;

    /** Puzzle dimensions. */
    private int _width, _height, _handSize;

    /** Input source from standard input. */
    private CommandSource _commands;

    /** Input source from standard input. */
    private PuzzleSource _puzzles;

    /** True until user quits. */
    private boolean _active;

    /** True iff we are logging commands on standard output. */
    private boolean _logging;

    /** True iff we are testing the program and printing board contents. */
    private boolean _testing;

}
