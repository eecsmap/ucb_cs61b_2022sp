package blocks;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static blocks.Utils.*;

/** Tests of the Model class.
 *  @author P. N. Hilfinger
 */
public class ModelTests {

    @Rule
    public Timeout methodTimeout = Timeout.seconds(1);

    /** Check that the cells of MODEL are as indicated by EXPECTED, which must
     *  be a string of sequences of asterisks (occupied) and dots (unoccupuied)
     *  separated by whitespace. Each sequence represents a row, starting at
     *  row 0. */
    private void checkCells(String expected, Model model) {
        String[] rows = expected.trim().split("\\s+");
        assertEquals("Wrong height", rows.length, model.height());
        assertEquals("Wrong width", rows[0].length(), model.width());

        for (int r = 0; r < model.height(); r += 1) {
            for (int c = 0; c < model.width(); c += 1) {
                assertEquals("Wrong contents at row " + r
                             + ", column " + c + ".",
                             rows[r].charAt(c) == '*', model.get(r, c));
            }
        }
    }

    /** Check that MODEL.piece(NPIECE) is placeable on MODEL at all and
     *  only positions given by POSITIONS.  POSITIONS is a string in format
     *  of the constructor argument to Piece, but indicating the reference
     *  points of all and only the positions in MODEL at which PIECE may
     *  be placed.  */
    private void checkPlaceable(int npiece, String placements, Model model) {
        Piece piece = model.piece(npiece);
        boolean[][] places = positions(placements);
        assertEquals("Wrong height", places.length, model.height());
        assertEquals("Wrong width", places[0].length, model.width());
        for (int r = 0; r < model.height(); r += 1) {
            for (int c = 0; c < model.width(); c += 1) {
                assertEquals(String.format("Wrong placement at (%d, %d)", r, c),
                             places[r][c], model.placeable(piece, r, c));
                assertEquals(String.format("Wrong placement at (%d, %d)", r, c),
                             places[r][c], model.placeable(npiece, r, c));
            }
        }
    }

    /** Check that a new Model has the proper initial state, using the
     *  get method. */
    @Test
    public void initGetTest() {
        Model m = new Model(6, 7);
        checkCells("...... ...... ...... ...... ...... ...... ......", m);
        assertEquals("Hand should be empty", 0, m.handSize());
        assertEquals("Score should be 0", 0, m.score());
    }

    @Test
    public void placeTest1() {
        Model m = new Model(4, 4);
        Piece p0 = new Piece("*** .*. ***"),
            p1 = new Piece("** *. **");
        m.deal(p0);
        m.deal(p1);
        m.place(0, 0, 1);
        checkCells(".*** ..*. .*** ....", m);
        assertNull("Piece not cleared from hand", m.piece(0));
        assertEquals("Bad score after place", 7, m.score());
    }

    @Test
    public void placeableTest1() {
        Model m = new Model(2, 2);
        Piece[] pieces = {
            new Piece(".* *."),
            new Piece("** *. *."),
            new Piece("*. **")
        };
        assertTrue("pieces[0] should be placeable at (0, 0)",
                   m.placeable(pieces[0], 0, 0));
        assertFalse("pieces[0] should not be placeable at (1, 0)",
                    m.placeable(pieces[0], 1, 0));
        assertTrue("pieces[0] should be placeable on m",
                   m.placeable(pieces[0]));

        assertFalse("pieces[1] should not be placeable at (0, 0)",
                    m.placeable(pieces[1], 0, 0));
        assertFalse("pieces[1] should not be placeable at (1, 0)",
                    m.placeable(pieces[1], 1, 0));
        assertFalse("pieces[1] should not be placeable on m",
                    m.placeable(pieces[1]));

        assertTrue("pieces[2] should be placeable at (0, 0)",
                   m.placeable(pieces[2], 0, 0));
        assertFalse("pieces[1] should not be placeable at (1, 0)",
                    m.placeable(pieces[2], 1, 0));
        assertTrue("pieces[1] be placeable on m",
                   m.placeable(pieces[2]));
    }

    @Test
    public void placeableTest2() {
        Model m = new Model(5, 5);
        Piece[] pieces = {
            new Piece(".* *."),
            new Piece("** *. *."),
            new Piece("*. **"),
            new Piece("** .*"),
            new Piece("..* *** ..*"),
            new Piece(".* *."),
            new Piece("*** .*. ***")
        };
        for (Piece p : pieces) {
            m.deal(p);
        }
        m.place(0, 3, 0);
        m.place(1, 0, 0);
        m.place(2, 0, 2);
        m.place(4, 2, 2);
        checkCells("***.. *.**. *...* .**** *...*", m);
        checkPlaceable(3, "...*. ..... ..... ..... .....", m);
        checkPlaceable(5, "..... ...*. *.... ..... .....", m);
        checkPlaceable(6, "..... ..... ..... ..... .....", m);
    }

    @Test
    public void placeableTest3() {
        Model m = new Model(5, 5);
        Piece[] pieces = {
            new Piece(".* *."),
            new Piece("** *. *."),
            new Piece("*. **"),
            new Piece("** .*"),
            new Piece("..* *** ..*"),
            new Piece(".* *."),
            new Piece("*** .*. ***")
        };
        for (Piece p : pieces) {
            m.deal(p);
        }
        m.place(0, 3, 0);
        m.place(1, 0, 0);
        m.place(2, 0, 2);
        m.place(4, 2, 2);
        assertTrue("Wrong placeability for piece 3", m.placeable(3));
        assertTrue("Wrong placeability for piece 3", m.placeable(pieces[3]));
        assertTrue("Wrong placeability for piece 5", m.placeable(5));
        assertTrue("Wrong placeability for piece 5", m.placeable(pieces[5]));
        assertFalse("Wrong placeability for piece 6", m.placeable(6));
        assertFalse("Wrong placeability for piece 6", m.placeable(pieces[6]));
    }

    @Test
    public void clearLinesTest() {
        Model m = new Model(4, 4);
        Piece p0 = new Piece("*** .*. ***"),
            p1 = new Piece("** *. **"),
            p2 = new Piece("* *");
        m.deal(p0);
        m.deal(p1);
        m.deal(p2);
        m.place(0, 0, 1);
        assertArrayEquals("Counts wrong after one piece",
                          new int[][] { { 3, 1, 3, 0 }, { 0, 2, 3, 2 } },
                          m.rowColumnCounts());
        m.place(1, 1, 0);
        checkCells(".*** ***. **** **..", m);
        assertArrayEquals("Counts wrong after two pieces",
                          new int[][] { { 3, 3, 4, 2 }, { 3, 4, 3, 2 } },
                          m.rowColumnCounts());
        m.clearFilledLines();
        checkCells("..** *.*. .... *...", m);
        assertEquals("Wrong score after lines cleared", 27, m.score());
        m.place(2, 2, 2);
        m.clearFilledLines();
        checkCells("...* *... .... *...", m);
        assertEquals("Wrong score after two consecutive moves that "
                     + "cleared line",
                     41, m.score());
    }

    /** Check that dealing and hand size work correctly.*/
    @Test
    public void handTest1() {
        Model m = new Model(4, 4);
        Piece p0 = new Piece("*** .*. ***"),
                p1 = new Piece("** *. **");
        assertEquals("Initial hand size incorrect", 0, m.handSize());
        m.deal(p0);
        assertEquals("Hand size incorrect after 1 deal", 1, m.handSize());
        m.deal(p1);
        assertEquals("Hand size incorrect after 2 deals", 2, m.handSize());
        m.clearHand();
        assertEquals("Hand incorrectly not empty after clear", 0, m.handSize());
    }

    @Test
    public void handTest2() {
        Model m = new Model(4, 4);
        Piece p0 = new Piece("*** .*. ***"),
            p1 = new Piece("** *. **");
        m.deal(p0);
        m.deal(p1);

        assertEquals("Wrong piece(0) value in hand", p0, m.piece(0));
        assertEquals("Wrong piece(1) value in hand", p1, m.piece(1));
        assertEquals("Wrong hand size", 2, m.handSize());

        m.place(0, 0, 1);
        assertEquals("piece(0) not cleared", null, m.piece(0));
        assertEquals("Wrong piece(1) value in hand", p1, m.piece(1));
        assertEquals("Wrong hand size after place", 2, m.handSize());
        m.place(1, 1, 0);
        assertTrue("Empty hand not detected", m.handUsed());
        assertEquals("Wrong hand size after place", 2, m.handSize());
    }

    /** Checks that undo works for a simple example. */
    @Test
    public void simpleUndo() {
        Model m = new Model(4, 4);
        Piece p0 = new Piece("** *. *."),
                p1 = new Piece("** .*");
        m.pushState();

        m.deal(p0);
        m.deal(p1);

        m.place(0, 0, 0);
        m.clearFilledLines();
        m.pushState();

        checkCells("**.. *.... *... ....", m);

        m.place(1, 2, 2);
        m.clearFilledLines();
        m.pushState();

        checkCells("**.. *... *.** ...*", m);
        m.undo();
        checkCells("**.. *... *... ....", m);
        m.undo();
        checkCells(".... .... .... ....", m);
    }

    /** Checks that redo works for a simple example. */
    @Test
    public void simpleRedo() {
        Model m = new Model(4, 4);
        Piece p0 = new Piece("** *. *."),
                p1 = new Piece("** .*");
        m.pushState();

        m.deal(p0);
        m.deal(p1);

        m.place(0, 0, 0);
        m.clearFilledLines();
        m.pushState();

        checkCells("**.. *.... *... ....", m);

        m.place(1, 0, 2);
        m.clearFilledLines();
        m.pushState();

        checkCells(".... *..* *... ....", m);
        m.undo();
        checkCells("**.. *... *... ....", m);
        m.undo();
        checkCells(".... .... .... ....", m);
        m.redo();
        checkCells("**.. *... *... ....", m);
        m.redo();
        checkCells(".... *..* *... ....", m);
    }

    /** Place piece #PIECENUM from hand at (ROW, COL) in MODEL,
     *  clear any filled lines, save state for undoing, and check that the
     *  board is now as given by EXPECTED. */
    private void checkMove(Model model,
                           int pieceNum, int row, int col, String expected) {
        model.place(pieceNum, row, col);
        model.clearFilledLines();
        model.pushState();
        checkCells(expected, model);
    }

    /** Deal Pieces specified by PIECESPECS (arguments to Piece constructor)
     *  into the hand for MODEL. */
    private void dealHand(Model model, String... pieceSpecs) {
        for (String pieceSpec : pieceSpecs) {
            model.deal(new Piece(pieceSpec));
        }
    }

    /** Complex example with undo/redo that makes sure
     * the board state is the same. */
    @Test
    public void undoRedo() {
        Model m = new Model(5, 5);

        m.pushState();
        dealHand(m, "*** *.. ***", "** .*", "..* ***");
        checkMove(m, 0, 0, 0, "***.. *.... ***.. ..... .....");
        checkMove(m, 1, 1, 2, "***.. *.**. ****. ..... .....");
        checkMove(m, 2, 2, 2, "***.. *.**. ..... ..*** .....");
        assertEquals("incorrect score after placing pieces", 24, m.score());

        m.clearHand();
        dealHand(m, "*. ** *.", "** .*", "*.. ***");
        checkMove(m, 1, 0, 3, "..... *.*** ..... ..*** .....");
        checkMove(m, 2, 3, 0, "..... *.*** ..... *.*** ***..");
        checkMove(m, 0, 1, 1, "..... ..... .**.. ..... ***..");
        assertEquals("incorrect score after placing pieces", 70, m.score());

        m.undo();
        checkCells("..... *.*** ..... *.*** ***..", m);
        m.undo();
        checkCells("..... *.*** ..... ..*** .....", m);
        assertEquals("incorrect score after undoing", 42, m.score());
        m.redo();
        checkCells("..... *.*** ..... *.*** ***..", m);
        assertEquals("incorrect score after redoing", 46, m.score());
        m.undo();
        checkCells("..... *.*** ..... ..*** .....", m);
        m.undo();
        checkCells("***.. *.**. ..... ..*** .....", m);
        m.undo();
        checkCells("***.. *.**. ****. ..... .....", m);
        assertEquals("incorrect hand size after undoing", 3, m.handSize());
        assertEquals("incorrect score after undoing", 10, m.score());
        m.undo();
        checkCells("***.. *.... ***.. ..... .....", m);
        assertEquals("incorrect score after undoing", 7, m.score());
        m.redo();
        checkCells("***.. *.**. ****. ..... .....", m);
        assertEquals("incorrect score after redoing", 10, m.score());
        checkMove(m, 2, 3, 1, "***.. *.**. ****. ...*. .***.");
        assertEquals("incorrect score after placing new piece", 14, m.score());
        m.undo();
        checkCells("***.. *.**. ****. ..... .....", m);
        assertEquals("incorrect score after undoing", 10, m.score());
        m.redo();
        checkCells("***.. *.**. ****. ...*. .***.", m);
        m.undo();
        m.undo();
        m.undo();
        checkCells("..... ..... ..... ..... .....", m);

        dealHand(m, "*** *.. ***", "** .*", "..* ***");
        assertEquals(3, m.handSize());
        checkMove(m, 0, 2, 0, "..... ..... ***.. *.... ***..");
        assertEquals("incorrect score after placing new piece", 7, m.score());
        checkMove(m, 1, 1, 2, "..... ..**. ****. *.... ***..");
        assertEquals("incorrect score after placing new piece", 10, m.score());
        checkMove(m, 2, 2, 2, "..... ..**. ..... *.*** ***..");
        assertEquals("incorrect score after placing new piece", 24, m.score());
    }
}
