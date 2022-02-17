import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.InputMismatchException;

/** Runs tests of ReadInts class
 *  @author Josh Hug
 */

public class TestReadInts {


    @Test
    /* For human evaluation. No automated testing here. */
    public void testPrintInts() {
        String inp = "5   12   6   2 3";
        System.out.println("Calling printInts(\"5   12   6   2 3\")");
        ReadInts.printInts(inp);
    }

    @Test
    public void testReadInts() {
        List<Integer> actual = ReadInts.readInts("5   12   6   2 3");
        List<Integer> expected = Utils.createList(5, 12, 6, 2, 3);
        assertEquals(expected, actual);
        try {
            ReadInts.readInts("5 1 3 dog horse 9");
            fail("Exception should have been thrown!");
        } catch (InputMismatchException e) {
            /* Ignore InputMismatchException. */
        }
    }

    @Test
    public void testSmartReadInts() {
        List<Integer> actual = ReadInts.smartReadInts("5   12   6   2 3");
        List<Integer> expected = Utils.createList(5, 12, 6, 2, 3);
        assertEquals(expected, actual);

        actual = ReadInts.smartReadInts("5  1 3   dog horse 9");
        expected = Utils.createList(5, 1, 3, 9);
        assertEquals(expected, actual);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(TestReadInts.class));
    }
}
