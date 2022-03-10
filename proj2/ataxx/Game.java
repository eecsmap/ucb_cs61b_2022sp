/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import static ataxx.PieceColor.*;
import static ataxx.GameException.error;
import static ataxx.Utils.*;

/** Main logic for playing (a) game(s) of Ataxx.
 *  @author P. N. Hilfinger
 */
class Game {

    /** Name of resource containing help message. */
    private static final String HELP = "ataxx/Help.txt";

    /** A new Game that takes command/move input from INP, logs
     *  commands if LOGGING, displays the board using VIEW, and uses REPORTER
     *  for messages to the user and error messages. SEED is intended to
     *  seed a random number generator, if one is used in an AI.
     */
    Game(CommandSource inp, View view, Reporter reporter, boolean logging) {
        _inp = inp;
        _view = view;
        _reporter = reporter;
        _logging = logging;
        _seed = (long) (Math.random() * Long.MAX_VALUE);

        _board = new Board();
        _board.setNotifier((b) -> _view.update(b));
    }

    /** Returns the game board.  This board is not intended to be modified
     *  by the caller. */
    Board getBoard() {
        return _board;
    }

    /** Return true iff the current game is not over. */
    boolean gameInProgress() {
        return _board.getWinner() == null;
    }

    /** Play a session of Ataxx.  This may include multiple games,
     *  and proceeds until the user exits.  Returns an exit code: 0 is
     *  normal; any positive quantity indicates an error.  */
    int play() {
        boolean winnerAnnounced;

        System.out.println("Welcome to " + Defaults.VERSION);
        _board.clear();
        setManual(RED);
        setAuto(BLUE);
        _exit = -1;
        winnerAnnounced = false;
        while (_exit < 0) {
            String cmnd;
            if (_board.getWinner() == null) {
                winnerAnnounced = false;
                try {
                    executeCommand(getPlayer(_board.whoseMove()).getMove());
                } catch (GameException e) {
                    reportError(e.getMessage());
                }
            } else if (!gameInProgress()) {
                if (!winnerAnnounced) {
                    _reporter.announceWin(_board.getWinner());
                    winnerAnnounced = true;
                }
                executeCommand(getCommand("-> "));
            }
        }
        return _exit;
    }

    /** Return a suggested prompt for command input. */
    private String prompt() {
        if (gameInProgress()) {
            return String.format("%s> ", _board.whoseMove());
        } else {
            return "+> ";
        }
    }

    /** Return a command from the current source, using PROMPT as a
     *  prompt, if needed. */
    String getCommand(String prompt) {
        String cmnd = _inp.getCommand(prompt);
        if (cmnd == null) {
            return "quit";
        } else {
            return cmnd;
        }
    }

    /** Perform the move denoted by MOVESTR, which must be legal. */
    void makeMove(String moveStr) {
        Move move = Move.move(moveStr);
        if (_board.legalMove(move)) {
            _board.makeMove(move);
        } else {
            throw error("illegal move");
        }
        if (_verbose) {
            printBoard();
        }
    }

    /** Place a block at the position PLACE (in crformat), and in its three
     *  reflected squares symmetrically. */
    void block(String place) {
        if (_board.numMoves() > 0) {
            throw error("block-setting must precede first move.");
        }
        if (!place.matches("[a-i][1-9]")) {
            throw error("invalid square designation");
        }
        _board.setBlock(place.charAt(0), place.charAt(1));
    }

    /** Undo the last move, and also the previous one, if that player is
     *  automatic. */
    void undo() {
        if (_board.numMoves() > 0) {
            _board.undo();
            if (_board.numMoves() > 0
                && getPlayer(_board.whoseMove()).isAuto()) {
                _board.undo();
            }
        }
    }

    /** Report the move MOVE by PLAYER. */
    void reportMove(Move move, PieceColor player) {
        _reporter.announceMove(move, player);
    }

    /** Send a message to the user as determined by FORMAT and ARGS, which
     *  are interpreted as for String.format or PrintWriter.printf. */
    void message(String format, Object... args) {
        _reporter.msg(format, args);
    }

    /** Send announcement of winner to my user output. */
    private void announceWinner() {
        _reporter.msg("* %s wins.", _board.getWinner().toString());
    }

    /** Make the player of COLOR an AI for subsequent moves. */
    private void setAuto(PieceColor color) {
        setPlayer(color, new AI(this, color, _seed));
        _seed += 1;
    }

    /** Make the player of COLOR take manual input from the user for
     *  subsequent moves. */
    private void setManual(PieceColor color) {
        setPlayer(color, new Manual(this, color));
    }

    /** Return the Player playing COLOR. */
    private Player getPlayer(PieceColor color) {
        return _players[color.ordinal()];
    }

    /** Set getPlayer(COLOR) to PLAYER. */
    private void setPlayer(PieceColor color, Player player) {
        _players[color.ordinal()] = player;
    }

    /** Clear the board to its initial state. */
    void clear() {
        _board.clear();
    }

    /** Print the current board using standard board-dump format. */
    private void dump() {
        _reporter.msg("===%n%s===", _board.toString());
    }

    /** Print a board with row/column numbers. */
    private void printBoard() {
        _reporter.msg(_board.toString(true));
    }

    /** Print a help message. */
    private void help() {
        printHelpResource(HELP, System.out);
    }

    /** Seed the random-number generator with SEED. */
    private void setSeed(long seed) {
        _seed = seed;
    }

    /** Execute command CMNDSTR.  Throws GameException on errors. */
    private void executeCommand(String cmndStr) {
        Command cmnd = Command.parseCommand(cmndStr);
        String[] parts = cmnd.operands();
        log(cmndStr);
        try {
            switch (cmnd.commandType()) {
            case COMMENT:
                break;
            case AUTO:
                setAuto(parseColor(parts[0]));
                break;
            case BOARD:
                printBoard();
                break;
            case DUMP:
                dump();
                break;
            case HELP:
                help();
                break;
            case MANUAL:
                setManual(parseColor(parts[0]));
                break;
            case NEW:
                clear();
                break;
            case QUIET:
                _verbose = false;
                break;
            case QUIT:
                _exit = 0;
                break;
            case SEED:
                setSeed(toLong(parts[0]));
                break;
            case VERBOSE:
                _verbose = true;
                break;
            case UNDO:
                undo();
                break;
            case BLOCK:
                block(parts[0]);
                break;
            case PIECEMOVE:
                makeMove(parts[0]);
                break;
            case ERROR:
                throw error("Unknown command.");
            default:
                break;
            }
        } catch (NumberFormatException excp) {
            reportError("Bad number in: %s", cmnd);
        } catch (ArrayIndexOutOfBoundsException excp) {
            reportError("Argument(s) missing: %s", cmnd);
        } catch (GameException excp) {
            reportError(excp.getMessage());
        }
    }

    /** Print a message on the logging stream, if any, appending a newline.
     *  The arguments FORMAT and ARGS have the same meaning as for
     *  String.format. */
    private void log(String format, Object... args) {
        if (_logging) {
            System.out.printf(format + "%n", args);
        }
    }

    /** Send an error message to the user formed from arguments FORMAT
     *  and ARGS, whose meanings are as for printf. */
    void reportError(String format, Object... args) {
        _reporter.err(format, args);
        if (Main.isStrict()) {
            _exit = 1;
        }
    }

    /** Returns command input for the current game. */
    private final CommandSource _inp;
    /** Outlet for responses to the user. */
    private final Reporter _reporter;

    /** The board on which I record all moves. */
    private final Board _board;
    /** Displayer of boards. */
    private View _view;
    /** True iff we are logging commands. */
    private boolean _logging;

    /** True iff we should print the board after each move. */
    private boolean _verbose;
    /** Current pseudo-random number seed.  Provided as an argument to AIs
     *  that use a random element in their choices.  Incremented for each
     *  AI to which it is supplied.
     */
    private long _seed;

    /** When set to a non-negative value, indicates that play should terminate
     *  at the earliest possible point, returning _exit.  When negative,
     *  indicates that the session is not over. */
    private int _exit;

    /** Current players, indexed by color (RED, BLUE). */
    private final Player[] _players = new Player[PieceColor.values().length];

   /** Used to return a move entered from the console.  Allocated
     *  here to avoid allocations. */
    private final int[] _move = new int[2];
}
