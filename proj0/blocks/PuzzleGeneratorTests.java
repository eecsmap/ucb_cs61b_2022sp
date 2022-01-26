package blocks;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

/** Tests of the PuzzleGenerator class.
 *  @author P. N. Hilfinger
 */
public class PuzzleGeneratorTests {

    @Rule
    public Timeout methodTimeout = Timeout.seconds(10);

    /** Check that PuzzleGenerator correctly deals pieces to a Model object.
     * Uses an arbitrary random seed (it doesn't impact the test), and checks
     * correctness using Model's handSize method. Additionally, ensures that two
     * PuzzleGenerators instantiated with the same seed result in the same
     * deals using Model's get method.*/
    @Test
    public void puzzleTest() {
        Model m = new Model(4, 4);
        PuzzleGenerator pg = new PuzzleGenerator(42);
        boolean dealResult;

        assertEquals("Incorrect hand size for new model", 0, m.handSize());
        dealResult = pg.deal(m, 3);
        assertTrue("Deal incorrectly returned false", dealResult);
        assertEquals("Incorrect hand size for model after dealing 3",
                3, m.handSize());
        dealResult = pg.deal(m, 3);
        assertTrue("Deal incorrectly returned false", dealResult);
        assertEquals("Incorrect hand size for model after dealing 3",
                3, m.handSize());
        dealResult = pg.deal(m, 15);
        assertTrue("Deal incorrectly returned false", dealResult);
        assertEquals("Incorrect hand size for model after dealing 3",
                15, m.handSize());

        PuzzleGenerator pg2 = new PuzzleGenerator(43);
        PuzzleGenerator pg3 = new PuzzleGenerator(43);
        Piece[] pg2Array = new Piece[3];
        Piece[] pg3Array = new Piece[3];

        pg2.deal(m, 3);
        for (int i = 0; i < pg2Array.length; i++) {
            pg2Array[i] = m.piece(i);
        }
        pg3.deal(m, 3);
        for (int i = 0; i < pg3Array.length; i++) {
            pg3Array[i] = m.piece(i);
        }
        for (int i = 0; i < pg2Array.length; i++) {
            assertEquals("Mismatching pieces at index " + i,
                    pg2Array[i], pg3Array[i]);
        }
    }

}
