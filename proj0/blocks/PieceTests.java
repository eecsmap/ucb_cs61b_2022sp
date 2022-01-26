package blocks;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static blocks.Utils.*;

/** Tests of the Piece class.
 *  @author P. N. Hilfinger
 */
public class PieceTests {

    @Rule
    public Timeout methodTimeout = Timeout.seconds(1);

    /** Check that the cells of M are as indicated by EXPECTED, which must be
     *  a string of sequences of asterisks (occupied) and dots (unoccupuied)
     *  separated by whitespace. Each sequence represents a row, starting at
     *  row 0. */
    private void checkCells(String expected, Model m) {
        String[] rows = expected.trim().split("\\s+");
        assertEquals("Wrong height", rows.length, m.height());
        assertEquals("Wrong width", rows[0].length(), m.width());

        for (int r = 0; r < m.height(); r += 1) {
            for (int c = 0; c < m.width(); c += 1) {
                assertEquals("Wrong contents at row " + r
                             + ", column " + c + ".",
                             rows[r].charAt(c) == '*', m.get(r, c));
            }
        }
    }

    @Test
    public void constructorTest() {
        Piece p = new Piece(PIECE1);
        assertEquals("Bad width", 3, p.width());
        assertEquals("Bad height", 4, p.height());
    }

    @Test
    public void getTest() {
        Piece p = new Piece(PIECE1);
        for (int row = 0; row < p.height(); row += 1) {
            for (int col = 0; col < p.width(); col += 1) {
                assertEquals("Wrong square in Piece at row " + row
                             + ", column " + col + ".",
                             PIECE1.charAt(4 * row + col) == '*',
                             p.get(row, col));
            }
        }
    }

    /** Ensure that the toString method, which depends on the get
     *  method, works as intended. */
    @Test
    public void toStringTest() {
        Piece p = new Piece(PIECE1);
        assertEquals("Wrong string",
                PIECE1.replace(" ", System.getProperty("line.separator")),
                p.toString());
    }

    /** Sample input for 3x4 piece. */
    private static final String PIECE1 = "*** *.* ..* ***";

}
