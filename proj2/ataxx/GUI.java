/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;

import java.util.concurrent.ArrayBlockingQueue;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import static ataxx.PieceColor.*;
import static ataxx.Utils.*;

/** The GUI for the Ataxx game.
 *  @author
 */
class GUI extends TopLevel implements View, CommandSource, Reporter {

    /* The implementation strategy applied here is to make it as
     * unnecessary as possible for the rest of the program to know that it
     * is interacting with a GUI as opposed to a terminal.
     *
     * To this end, we first have made Board observable, so that the
     * GUI gets notified of changes to a Game's board and can interrogate
     * it as needed, while the Game and Board themselves need not be aware
     * that it is being watched.
     *
     * Second, instead of creating a new API by which the GUI communicates
     * with a Game, we instead simply arrange to make the GUI's input look
     * like that from a terminal, so that we can reuse all the machinery
     * in the rest of the program to interpret and execute commands.  The
     * GUI simply composes commands (such as "start" or "clear") and
     * writes them to a Writer that (using the Java library's PipedReader
     * and PipedWriter classes) provides input to the Game using exactly the
     * same API as would be used to read from a terminal. Thus, a simple
     * Manual player can handle all commands and moves from the GUI.
     *
     * See also Main.java for how this might get set up.
     */

    /** Minimum size of board in pixels. */
    private static final int MIN_SIZE = 300;

    /** A new GUI with TITLE as its window title. */
    GUI(String title) {
        super(title, true);
        addMenuButton("Game->New", this::newGame);
        addMenuRadioButton("Game->Blocks->Set Blocks", "Blocks",
                           false, this::adjustBlockMode);
        addMenuRadioButton("Game->Blocks->Move Pieces", "Blocks",
                           true, this::adjustBlockMode);
        addMenuButton("Game->Quit", this::quit);
        addMenuButton("Options->Seed...", this::setSeed);
        addMenuRadioButton("Options->Players->Red AI", "Red",
                           false, (dummy) -> send("auto red"));
        addMenuRadioButton("Options->Players->Red Manual", "Red",
                           true, (dummy) -> send("manual red"));
        addMenuRadioButton("Options->Players->Blue AI", "Blue",
                           true, (dummy) -> send("auto blue"));
        addMenuRadioButton("Options->Players->Blue Manual", "Blue",
                           false, (dummy) -> send("manual blue"));
        addMenuButton("Info->Help", this::doHelp);
        _widget = new BoardWidget(_commandQueue);
        add(_widget,
            new LayoutSpec("height", "1",
                           "width", "REMAINDER",
                           "ileft", 5, "itop", 5, "iright", 5,
                           "ibottom", 5));
        addLabel("Red to move", "State",
                 new LayoutSpec("y", 1, "anchor", "west"));
        addButton("Pass", this::doPass, new LayoutSpec("y", "1"));
    }

    /** Execute the "Quit" button function. */
    private synchronized void quit(String unused) {
        send("quit");
    }

    /** Execute the "New Game" button function. */
    private synchronized void newGame(String unused) {
        send("new");
        setEnabled(false, "Game->Blocks->Set Blocks");
        setEnabled(true, "Game->Blocks->Move Pieces");
        _widget.setBlockMode(false);
    }

    /** Execute Seed... command. */
    private synchronized void setSeed(String unused) {
        String resp =
            getTextInput("Random Seed", "Get Seed", "question", "");
        if (resp == null) {
            return;
        }
        try {
            long s = Long.parseLong(resp);
            send("seed %d", s);
        } catch (NumberFormatException excp) {
            return;
        }
    }

    /** Execute 'pass' command, if legal. */
    private synchronized void doPass(String unused) {
        if (_board.legalMove(Move.pass())) {
            send("-");
        }
    }

    /** Display 'help' text. */
    private void doHelp(String unused) {
        InputStream helpIn =
            Game.class.getClassLoader()
            .getResourceAsStream("ataxx/guihelp.txt");
        if (helpIn != null) {
            try {
                BufferedReader r
                    = new BufferedReader(new InputStreamReader(helpIn));
                char[] buffer = new char[1 << 15];
                int len = r.read(buffer);
                showMessage(new String(buffer, 0, len), "Help", "plain");
                r.close();
            } catch (IOException e) {
                /* Ignore IOException */
            }
        }
    }

    @Override
    public void err(String format, Object... args) {
        showMessage(String.format(format, args), "Error", "error");
    }

    @Override
    public void announceWin(PieceColor player) {
        // FIXME
    }

    @Override
    public void announceMove(Move move, PieceColor player) {
    }

    @Override
    public void msg(String format, Object... args) {
        showMessage(String.format(format, args), "Message", "information");
    }

    @Override
    public void update(Board board) {
        if (board == _board) {
            updateLabel();
        }
        _board = board;
        _widget.update(board);
    }

    @Override
    public String getCommand(String ignored) {
        try {
            return _commandQueue.take();
        } catch (InterruptedException excp) {
            throw new Error("unexpected interrupt");
        }
    }

    /** Return true iff we are currently in block-setting mode. */
    private boolean blockMode() {
        return isSelected("Game->Blocks->Set Blocks");
    }

    void adjustBlockMode(String label) {
        _widget.setBlockMode(label.equals("Game->Blocks->Set Blocks"));
    }

    /** Set PLAYER ("red" or "blue") to be an AI iff ON. */
    private void setAIMode(String player, boolean on) {
        send("%s %s%n", on ? "auto" : "manual", player);
    }

    /** Set label indicating board state. */
    private void updateLabel() {
        String label;
        int red = _board.redPieces();
        int blue = _board.bluePieces();
        if (_board.getWinner() != null) {
            if (red > blue) {
                label = String.format("Red wins (%d-%d)", red, blue);
            } else if (red < blue) {
                label = String.format("Blue wins (%d-%d)", red, blue);
            } else {
                label = "Drawn game";
            }
        } else {
            label = String.format("%s to move", _board.whoseMove());
        }
        setLabel("State", label);
    }

    /** Add the command described by FORMAT, ARGS (as for String.format) to
     *  the queue of waiting commands returned by getCommand. */
    private void send(String format, Object... args) {
        _commandQueue.offer(fmt(format, args));
    }

    /** Contains the drawing logic for the Ataxx model. */
    private BoardWidget _widget;
    /** Queue for commands going to the controlling Game. */
    private final ArrayBlockingQueue<String> _commandQueue =
        new ArrayBlockingQueue<>(5);
    /** The model of the game. */
    private Board _board;
}
