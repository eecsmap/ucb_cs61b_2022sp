package blocks;

import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.concurrent.ArrayBlockingQueue;

import java.awt.Dimension;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import static blocks.Utils.*;

/** The GUI controller for a Blocks board and buttons.
 *  @author P. N. Hilfinger
 */
class GUI extends TopLevel implements View {

    /** Margins around label placement are multiples of this (pixels). */
    static final int UNIT_MARGIN = 5;

    /** Time in milliseconds between score-label updates. */
    static final long LABEL_UPDATE_PAUSE = 20;

    /** Size of pane used to contain help text. */
    static final Dimension TEXT_BOX_SIZE = new Dimension(500, 700);

    /** Resource name of "About" message. */
    static final String ABOUT_TEXT = "/blocks/resources/About.html";

    /** Resource name of Signpost help text. */
    static final String HELP_TEXT = "/blocks/resources/Help.html";

    /** Resource name of sound for move. */
    static final String MOVE_SOUND = "/blocks/resources/tick.wav";

    /** Resource name for end-of-round. */
    static final String DONE_SOUND = "/blocks/resources/end-chime.wav";

    /** A new window with given TITLE providing a view of MODEL. */
    GUI(String title) {
        super(title, true);
        addMenuButton("Game->New", this::newGame);
        addSeparator("Game");
        addMenuButton("Game->Undo", this::undo);
        addMenuButton("Game->Redo", this::redo);
        addSeparator("Game");
        addMenuButton("Game->Size", (s) -> newSize());
        addMenuButton("Game->Seed", this::newSeed);
        addSeparator("Game");
        addMenuButton("Game->Quit", this::quit);
        addMenuButton("Help->About", (s) -> displayText("About", ABOUT_TEXT));
        addMenuButton("Help->Blocks", (s) -> displayText("Blocks Help",
                                                         HELP_TEXT));
        _moveSound = new SoundEffect(MOVE_SOUND);
        _doneSound = new SoundEffect(DONE_SOUND);
    }

    /** Response to "Quit" button click. */
    private void quit(String dummy) {
        _pendingCommands.offer("QUIT");
    }

    /** Response to "New Game" button click. */
    private void newGame(String dummy) {
        _pendingCommands.offer("NEW");
    }

    /** Response to "Undo" button click. */
    private void undo(String dummy) {
        _pendingCommands.offer("UNDO");
    }

    /** Response to "Redo" button click. */
    private void redo(String dummy) {
        _pendingCommands.offer("REDO");
    }

    /** Display text in resource named TEXTRESOURCE in a new window titled
     *  TITLE. */
    private void displayText(String title, String textResource) {
        /* Implementation note: It would have been more convenient to avoid
         * having to read the resource and simply use dispPane.setPage on the
         * resource's URL.  However, we wanted to use this application with
         * a nonstandard ClassLoader, and arranging for straight Java to
         * understand non-standard URLS that access such a ClassLoader turns
         * out to be a bit more trouble than it's worth. */
        JFrame frame = new JFrame(title);
        JEditorPane dispPane = new JEditorPane();
        dispPane.setEditable(false);
        dispPane.setContentType("text/html");
        InputStream resource = getClass().getResourceAsStream(textResource);
        StringWriter text = new StringWriter();
        try {
            while (true) {
                int c = resource.read();
                if (c < 0) {
                    dispPane.setText(text.toString());
                    break;
                }
                text.write(c);
            }
        } catch (IOException e) {
            return;
        }
        JScrollPane scroller = new JScrollPane(dispPane);
        scroller.setVerticalScrollBarPolicy(scroller.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setPreferredSize(TEXT_BOX_SIZE);
        frame.add(scroller);
        frame.pack();
        frame.setVisible(true);
    }

    /** Pattern describing the 'size' command's arguments. */
    private static final Pattern INT_PATN = Pattern.compile("\\d+$");

    /** Pattern describing the 'seed' command's arguments. */
    private static final Pattern SEED_PATN =
        Pattern.compile("\\s*(-?\\d{1,18})\\s*$");

    /** Set board size and hand size to WIDTH, HEIGHT, and HANDSIZE. */
    private void newSize(int width, int height, int handSize) {
        _pendingCommands.offer(String.format("SIZE %d %d %d",
                                             width, height, handSize));
    }

    /** Respond to "Game->Size..." menu click. */
    private void newSize() {
        String[] response =
            getTextInputs(null, "Blocks Configuration", "plain", 5,
                          "Width", String.format("%d", _width),
                          "Height", String.format("%d", _height),
                          "Hand size", String.format("%d", _handSize));
        if (response != null) {
            for (int i = 0; i < response.length; i += 1) {
                if (!Pattern.matches("\\d+$", response[i])) {
                    showMessage("Responses must be non-negative integers.",
                                "Error", "error");
                }
            }

            int width = toInt(response[0]),
                height = toInt(response[1]),
                handSize = toInt(response[2]);
            if (width > 0 && height > 0 && handSize > 0) {
                newSize(width, height, handSize);
            } else {
                showMessage("Bad board configuration parameters.",
                            "Error", "error");
            }
        }
    }

    /** Respond to "Seed" menu click. */
    private void newSeed(String dummy) {
        String response =
            getTextInput("Enter new random seed.", "New seed",  "plain", "");
        if (response != null) {
            Matcher mat = SEED_PATN.matcher(response);
            if (mat.matches()) {
                _pendingCommands.offer(String.format("SEED %s", mat.group(1)));
            } else {
                showMessage("Enter an integral seed value.", "Error", "error");
            }
        }
    }

    /** Return the next command from our widget, waiting for it as necessary.
     *  Clicking on grid cells results in "SET" messages.
     *  Menu-button clicks result in the messages "QUIT", "NEW", "UNDO",
     *  "REDO", "SEED", or "SIZE". */
    String readCommand() {
        try {
            String cmnd = _pendingCommands.take();
            if (cmnd.startsWith("SET")) {
                _moveSound.play();
            }
            return cmnd;
        } catch (InterruptedException excp) {
            throw new Error("unexpected interrupt");
        }
    }

    @Override
    public void update(Model model) {
        if (_widget == null) {
            initialize();
        }
        if (model.roundOver()) {
            _doneSound.play();
            setLabel("DoneLabel", "NO MORE MOVES!");
            _maxScore = Math.max(_maxScore, model.score());
            setLabel("MaxScoreLabel",
                     msg("Maximum score: %d", _maxScore));
        } else {
            setLabel("DoneLabel", "");
        }
        if (_width != model.width() || _height != model.height()) {
            _width = model.width();
            _height = model.height();
            _handSize = model.handSize();
            _widget.update(model);
            _widget.invalidate();
            getContentPane().validate();
            display(true);
        } else {
            _widget.update(model);
        }
        for (int s = _lastScore; s < model.score(); s += 1) {
            setLabel("ScoreLabel", msg("%4d", s));
            try {
                Thread.sleep(LABEL_UPDATE_PAUSE);
            } catch (InterruptedException excp) {
                assert false;
            }
        }
        _lastScore = model.score();
        setLabel("ScoreLabel", msg("%4d", _lastScore));
    }

    /** Set up and lay out the components of the GUI. */
    private void initialize() {
        int pad = _widget.CELL_SIDE / 2;
        _widget = new BoardWidget(_pendingCommands);
        addLabel("0", "ScoreLabel",
                 new LayoutSpec("x", 0,
                                "y", 0,
                                "width", 1,
                                "height", 1,
                                "itop", UNIT_MARGIN,
                                "anchor", "center",
                                "ibottom", UNIT_MARGIN));
        add(_widget,
            new LayoutSpec("x", 0,
                           "y", 1,
                           "ileft", pad, "iright", pad,
                           "itop", pad, "ibottom", pad,
                           "height", 1,
                           "width", 1));
        addLabel("              ",
                 "DoneLabel",
                 new LayoutSpec("x", 0,
                                "y", 2,
                                "anchor", "center",
                               "ibottom", UNIT_MARGIN));
        addLabel("Maximum score: 0",
                 "MaxScoreLabel",
                 new LayoutSpec("x", 0,
                                "y", 3,
                                "anchor", "center",
                                "ibottom", UNIT_MARGIN));
    }

    /** The board widget. */
    private BoardWidget _widget;
    /** The current size of the model. */
    private int _width, _height;
    /** The number of Pieces dealt to a hand. */
    private int _handSize;
    /** The last reported score. */
    private int _lastScore;
    /** Maximum score achieved.  Updated at end of each round. */
    private int _maxScore;

    /** Queue of pending key presses. */
    private ArrayBlockingQueue<String> _pendingCommands =
        new ArrayBlockingQueue<>(5);

    /** Sound effect for placing a piece. */
    private SoundEffect _moveSound;
    /** Sound effect for end of round. */
    private SoundEffect _doneSound;
}
