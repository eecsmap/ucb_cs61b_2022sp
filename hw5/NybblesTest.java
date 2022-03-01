import org.junit.Test;
import static org.junit.Assert.*;

/** Tests of Nybbles
 *  @author P. N. Hilfinger
 */
public class NybblesTest {

    @Test
    public void testPos() {
        Nybbles arr = new Nybbles(16);
        for (int i = 0; i < arr.size(); i += 1) {
            arr.set(i, i % 8);
        }
        for (int i = 0; i < arr.size(); i += 1) {
            assertEquals(i % 8, arr.get(i));
        }
    }

    @Test
    public void testNeg() {
        Nybbles arr = new Nybbles(16);
        for (int i = 0; i < arr.size(); i += 1) {
            arr.set(i, i % 8 - 8);
        }
        for (int i = 0; i < arr.size(); i += 1) {
            assertEquals(i % 8 - 8, arr.get(i));
        }
    }

    @Test
    public void testMixed() {
        Nybbles arr = new Nybbles(16);
        for (int i = 0; i < arr.size(); i += 2) {
            arr.set(i, i / 2 - 8);
            arr.set(i + 1, i / 2);
        }
        for (int i = 0; i < arr.size(); i += 2) {
            assertEquals(i / 2 - 8, arr.get(i));
            assertEquals(i / 2, arr.get(i + 1));
        }
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(NybblesTest.class));
    }
}

