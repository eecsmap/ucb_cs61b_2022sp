import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.IdentityHashMap;

/** Tests for minimal spanning trees.
 *  @author P. N. Hilfinger
 */
public class MSTTest {

    /** Return true iff the multiset of edges in E0 is a subset
     *  of those in E1, where two edges are considered the same iff
     *  they are identical array objects. */
    private static boolean isSubset(int[][] E0, int[][] E1) {
        IdentityHashMap<int[], Boolean>
            M0 = new IdentityHashMap<>(E0.length),
            M1 = new IdentityHashMap<>(E1.length);

        for (int[] e : E0) {
            M0.put(e, true);
        }
        for (int[] e : E1) {
            M1.put(e, true);
        }
        return M1.keySet().containsAll(M0.keySet());
    }

    /** Return true iff E0[i] is an array containing the same sequence
        of values as E1[i] for each i. */
    private static boolean sameContents(int[][] E0, int[][] E1) {
        if (E0.length != E1.length) {
            return false;
        }
        for (int i = 0; i < E0.length; i += 1) {
            if (E0[i] == null || !Arrays.equals(E0[i], E1[i])) {
                return false;
            }
        }
        return true;
    }

    /** Returns weight of graph. */
    private int graphWeight(int[][] graph) {
        int total = 0;
        for (int[] edge: graph) {
            total += edge[2];
        }
        return total;
    }

    private void check(int V, int minEdges, int maxWeight, long seed,
                       int mstWeight, int[][] E) {
        String desc = String.format("%d vertices, >=%d edges, max weight %d, "
                                    + "seed %d", V, minEdges, maxWeight, seed);
        int[][] E0 = Arrays.copyOf(E, E.length);
        int[][] E1 = Utils.deepCopy(E);
        int[][] mst = MST.mst(V, E);
        assertTrue(String.format("E input disturbed: (%s)", desc),
                   Arrays.equals(E, E0));
        assertTrue(String.format("Edges modified: (%s)", desc),
                   sameContents(E, E1));
        assertTrue(String.format("Not a subset: (%s)", desc),
                   isSubset(mst, E0));
        assertTrue(String.format("Result not a tree: (%s)", desc),
                   Utils.isTree(V, mst));
        assertEquals(String.format("Wrong minimal value (should be %d): (%s)",
                                   mstWeight, desc),
                     mstWeight, Utils.totalWeight(mst));
    }

    private void check(int V, int minEdges, int maxWeight, long seed,
                       int mstWeight) {
        int[][] E = Utils.randomConnectedGraph(V, minEdges, maxWeight, seed);
        check(V, minEdges, maxWeight, seed, mstWeight, E);
    }

    /** Test from the Kruskal's Demo linked in the spec.
    *   Vertex 0 from the slides is replaced with vertex 7 so that 
    *   vertices are 1-indexed. 
    *   This test is NOT in the autograder and is for your debugging
    *   purposes only. */
    public void kruskalsDemoTest() {
        int[][] graph = {
            {7, 1, 2},
            {7, 2, 1},
            {1, 2, 5},
            {1, 3, 11},
            {2, 4, 1},
            {2, 5, 15},
            {3, 4, 3},
            {3, 6, 1},
            {4, 1, 3},
            {4, 5, 4},
            {5, 6, 1},
            {6, 4, 3}
        };
        int[][] result = MST.mst(7, graph);
        assertEquals("Wrong MST total weight", 9, graphWeight(result));
    }

    @Test(timeout = 500)
    public void singletonTest() {
        check(1, 0, 100, 1923, 0);
    }

    @Test(timeout = 500)
    public void doubletonTest() {
        check(2, 1, 100, 1923, 96);
    }

    @Test(timeout = 500)
    public void smallishTest() {
        check(20, 10, 100, 19234, 1076);
    }

    @Test(timeout = 500)
    public void biggishTest1() {
        check(5000, 30000, 1000, 192345, 536912);
    }

    @Test(timeout = 500)
    public void biggishTest2() {
        check(20000, 100000, 1000, 1923456, 2595553);
    }

    @Test(timeout = 2000)
    public void biggishTest3() {
        check(200000, 1000000, 100, 192345678, 2515034, BIG3);
    }

    private static final int[][] BIG3 =
        Utils.randomConnectedGraph(200000, 1000000, 100, 192345678);

}
