package blocks;

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;

import ucb.util.CommandArgs;

/** The main class for the Blocks puzzle.
 *  @author P. N. Hilfinger
 */
public class Main {

    /** The main program.  ARGS may contain the options --seed=NUM,
     *  (random seed); --log (record commands, clicks);
     *  --testing (take puzzles and commands from standard input);
     *  --setup (take puzzles from standard input and commands from GUI);
     *  and --no-display. */
    public static void main(String... args) {

        CommandArgs options =
            new CommandArgs("--seed=(\\d+) --log --testing "
                            + "--no-display --debug --=(.*)",
                            args);
        if (!options.ok()) {
            System.err.println("Usage: java blocks.Main [ --seed=NUM ] "
                               + "[ --log ] [ --testing ] [ --no-display ]"
                               + " [ --debug ]"
                               + " [ INPUT ]");
            System.exit(1);
        }

        if (options.contains("--")) {
            String inpFile = options.getFirst("--");
            try {
                System.setIn(new FileInputStream(inpFile));
            } catch (IOException excp) {
                System.err.printf("Could not open %s%n", inpFile);
                System.exit(1);
            }
        }

        Utils.setDebuggingMessages(options.contains("--debug"));

        Controller puzzler = getController(options);

        try {
            while (puzzler.active()) {
                puzzler.playPuzzle();
            }
        } catch (IllegalStateException excp) {
            System.err.printf("Internal error: %s%n", excp.getMessage());
            System.exit(1);
        }

        System.exit(0);

    }

    /** Return an appropriate Controller as indicated by OPTIONS. */
    private static Controller getController(CommandArgs options) {
        GUI gui;
        CommandSource cmds;
        PuzzleSource puzzles;

        if (options.contains("--no-display")) {
            gui = null;
        } else {
            gui = new GUI("Blocks 61B");
        }

        puzzles = null;
        if (options.contains("--testing")) {
            TextSource src = new TextSource(new Scanner(System.in), null);
            cmds = src;
            puzzles = src;
        } else if (gui == null) {
            cmds = new TextSource(new Scanner(System.in), "> ");
        } else {
            cmds = new GUISource(gui);
        }
        if (puzzles == null) {
            long seed;
            if (options.contains("--seed")) {
                seed = options.getLong("--seed");
            } else {
                seed = (long) (Math.random() * SEED_RANGE);
            }
            puzzles = new PuzzleGenerator(seed);
        }

        return new Controller(gui, cmds, puzzles,
                              options.contains("--log"),
                              options.contains("--testing"));
    }

    /** Maximum default seed. */
    private static final double SEED_RANGE = 1e12;
}
