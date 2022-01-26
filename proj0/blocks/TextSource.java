package blocks;

import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static blocks.Utils.*;

/** A type of CommandSource and PuzzleSource that receives commands and
 *  puzzles from a Scanner. This kind of source is intended for testing and
 *  manual command input.
 *  @author P. N. Hilfinger
 */
class TextSource implements CommandSource, PuzzleSource {

    /** Provides commands and puzzles from SOURCE.  Prompts for input with
     *  PROMPT, if non-null. */
    TextSource(Scanner source, String prompt) {
        source.useDelimiter("[ \t\n\r(,)]+");
        _prompt = prompt;
        _source = source;
    }

    /** Returns a command string read from my source. At EOF, returns QUIT.
     *  Allows comment lines starting with "#", which are discarded. */
    @Override
    public String getCommand() {
        while (true) {
            if (_prompt != null) {
                System.out.print(_prompt);
                System.out.flush();
            }
            if (!_source.hasNext()) {
                return "QUIT";
            }
            String line = _source.nextLine().trim().toUpperCase();
            if (!line.startsWith("#")) {
                return line;
            }
        }
    }

    /** Prefix of a hand on test input. */
    static final Pattern HAND_START_PATTERN = Pattern.compile("H\\[");

    /** End delimiter of a hand of test input. */
    static final Pattern HAND_END_PATTERN = Pattern.compile("\\]");

    /** Format for a piece. */
    static final Pattern PIECE_PATTERN =
        Pattern.compile("\\G\\s*(\\d+):([\\s*.]+)");

    /** Create a model initialized to a puzzle.  Returns true on success
     *  and false if at the end of the test source.  Throws
     *  IllegalArgumentException if not at end of file, but there is not a
     *  valid puzzle next in the input, or if the puzzle provided does
     *  not contain handSize pieces. */
    @Override
    public boolean deal(Model model, int handSize) {
        model.clearHand();
        try {
            if (!_source.hasNext()) {
                return false;
            }

            _source.next(HAND_START_PATTERN);
            for (int i = 0; i < handSize; i += 1) {
                if (_source.findWithinHorizon(PIECE_PATTERN, 0) != null) {
                    model.deal(new Piece(_source.match().group(2)));
                } else {
                    throw badArgs("missing or badly formed piece");
                }
            }
            _source.next(HAND_END_PATTERN);
            return true;
        } catch (NoSuchElementException excp) {
            throw badArgs("expected hand not available or malformed");
        }
    }

    @Override
    public void setSeed(long seed) {
        _randomPuzzler.setSeed(seed);
    }

    /** Input source. */
    private Scanner _source;
    /** Input prompt. */
    private String _prompt;
    /** Source for random puzzles. */
    private PuzzleGenerator _randomPuzzler = new PuzzleGenerator(0);
}
