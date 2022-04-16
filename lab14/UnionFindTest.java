import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/** Tests for union-find. These are slightly different from the AG.
 *  @author Michelle Hwang
 */
public class UnionFindTest {

    private void setUp(int N) {
        _uf = new UnionFind(N);
    }

    private String msg(String format, Object... args) {
        return String.format(format, args);
    }
    
    /** Tests that for singleton sets ({1}, {2}, ... {10}), each set's
    *   representative is itself. */
    @Test
    public void singletonsTest() {
        int N = 10;
        setUp(N);
        for (int i = 1; i <= N; i += 1) {
            assertEquals(msg("Wrong representative singleton %d", i),
                         i, _uf.find(i));
        }
    }

    /** Unions in pairs ({1, 2}, {3, 4},  ... {9, 10}) and ensures each
    *   set's representative is one of the items. Also ensures the
    *   representative of each set is consistent. */
    @Test
    public void pairsTest() {
        int N = 10;
        setUp(N);
        for (int i = 1; i <= N; i += 2) {
            _uf.union(i, i + 1);
        }
        for (int i = 1; i <= N; i += 2) {
            assertTrue(msg("%d, %d have improper representative", i, i + 1),
                       i == _uf.find(i) || i + 1 == _uf.find(i));
            assertEquals(msg("%d, %d have different representatives",
                             i, i + 1),
                         _uf.find(i), _uf.find(i + 1));
        }
    }

    /** Unions in groups of 10 ({1, 11, ... 91}, ... {10, 20, 100}) 
    *   and ensures each set's representative is one of the items. 
    *   Also ensures the representative of each set is consistent. */
    @Test
    public void modsTest() {
        int N = 100;
        setUp(N);
        for (int i = 1; i <= N; i += 1) {
            _uf.union(i, (i - 1) % 10 + 1);
        }
        for (int i = 11; i <= N; i += 2) {
            int min = (i - 1) % 10 + 1;
            int rep = _uf.find(min);
            assertTrue(msg("%d has representative with wrong value mod 10",
                           min),
                       (rep - i) % 10 == 0);
            assertEquals(msg("%d and %d have different representatives",
                             min, i),
                         rep, _uf.find(i));
        }            
    }

    /** Unions everything and ensures each set's representative 
    *   is one of the items. Also ensures the representative of 
    *   each set is consistent. */
    @Test
    public void unionAllTest() {
        Random gen = new Random(2718281828L);
        int N = 1 << 6;
        setUp(N);
        for (int step = 2; step <= N; step *= 2) {
            for (int k = 1; k <= N; k += step) {
                int halfStep = step >> 1;
                _uf.union(k + gen.nextInt(halfStep),
                          k + halfStep + gen.nextInt(halfStep));
            }
        }
        int part0 = _uf.find(1);
        assertTrue("bad representative", part0 > 0 && part0 <= N);
        for (int k = 1; k <= N; k += 1) {
            assertEquals("number in wrong partition", part0, _uf.find(k));
        }
    }

    private UnionFind _uf;

}
