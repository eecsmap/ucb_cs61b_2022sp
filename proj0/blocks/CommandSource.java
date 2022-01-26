package blocks;

/** Describes a source of input commands.  The possible text commands are as
 *  follows (parts of a command are separated by whitespace):
 *    - TYPE w h ncolors extra: Replace the current board with one that is
 *                w cells wide and h cells high with ncolors colors.
 *                Requires that w, h, ncolors >= 3 and extra >= 0,  and all are
 *                properly formed numerals.  Sets move limit of board to
 *                extra + estimated moves needed to solve.
 *    - NEW:      Start a new puzzle with current parameters.
 *    - SET row col: Set active region to color at position (row, col).
 *    - RESTART:  Clear all work on the current puzzle, returning to its initial
 *                state.
 *    - UNDO:     Go back one move.
 *    - REDO:     Go forward one previously undone move.
 *    - SEED s:   Set a new random seed.
 *    - QUIT:     Exit the program.
 *  @author P. N. Hilfinger
 */
interface CommandSource {

    /** Returns one command string, trimmed of preceding and following
     *  whitespace and converted to upper case. */
    String getCommand();

}
