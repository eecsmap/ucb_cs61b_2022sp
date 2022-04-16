import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Stack;
import java.util.TreeSet;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;


/** Undirected, weighted graph utilities.
 *  @author P. N. Hilfinger
 */
public class Utils {

    /** Return a random array of at least MINEDGES undirected weighted
     *  edges between vertices 1..V that form a connected graph.  SEED
     *  is a random seed that determines the result.  The edge weights are
     *  randomly chosen from 0 .. MAXWEIGHT. */
    public static int[][] randomConnectedGraph(int V, int minEdges,
                                               int maxWeight, long seed) {
        Random gen = new Random(seed);
        ArrayList<Integer> vertices = new ArrayList<>();
        for (int i = 1; i <= V; i += 1) {
            vertices.add(i);
        }
        Collections.shuffle(vertices, gen);
        TreeSet<int[]> E = new TreeSet<>(SAME_EDGES);
        for (int k = 1; k < V; k += 1) {
            int u = vertices.get(gen.nextInt(k));
            int v = vertices.get(k);
            E.add(new int[] {
                    Math.min(u, v),
                    Math.max(u, v),
                    gen.nextInt(maxWeight)
                });
        }
        assert E.size() == V - 1;
        while (E.size() < minEdges) {
            int u = 1 + gen.nextInt(V - 1);
            int v = u + 1 + gen.nextInt(V - u);
            E.add(new int[] { u, v, gen.nextInt(maxWeight) });
        }
        int[][] result = E.toArray(new int[E.size()][]);
        isConnected(V, result);
        return result;
    }

    /** A lexicographic comparator of 3-element edges { u, v, w }
     *  that ignores w.  As a result, the comparator is not consistent with
     *  array equality in the sense that while A = B (equality of content)
     *  implies compare(A, B) == 0, the converse is not necessarily true. */
    private static final Comparator<int[]> SAME_EDGES =
        new Comparator<int[]>() {
            @Override
            public int compare(int[] e0, int[] e1) {
                int c = e0[0] - e1[0];
                if (c != 0) {
                    return c;
                } else {
                    return e0[1] - e1[1];
                }
            }
        };

    /** Return true iff (1..V, E) is a connected, undirected graph. */
    public static boolean isConnected(int V, int[][] E) {
        ArrayList<ArrayList<int[]>> neighbors = new ArrayList<>();
        neighbors.add(null);
        for (int i = 1; i <= V; i += 1) {
            neighbors.add(new ArrayList<>());
        }
        for (int[] e : E) {
            assert e[0] < e[1];
            neighbors.get(e[0]).add(e);
            neighbors.get(e[1]).add(e);
        }

        boolean[] visited = new boolean[V + 1];
        int nvisited;
        Stack<Integer> work = new Stack<>();
        work.push(1);
        nvisited = 0;
        while (!work.isEmpty()) {
            int v = work.pop();
            if (!visited[v]) {
                nvisited += 1;
                visited[v] = true;
                for (int[] e : neighbors.get(v)) {
                    if (e[0] != v) {
                        work.push(e[0]);
                    } else {
                        work.push(e[1]);
                    }
                }
            }
        }
        return nvisited == V;
    }

    /** Return true iff E forms a tree of vertices 1-V. */
    public static boolean isTree(int V, int[][] E) {
        return E.length == V - 1 && isConnected(V, E);
    }

    /** Return the total weight of the edges in E. */
    public static int totalWeight(int[][] E) {
        int w;
        w = 0;
        for (int[] e : E) {
            w += e[2];
        }
        return w;
    }

    /** Print E as a Java array value on the standard output. */
    public static void printEdges(int[][] E) {
        System.out.println("{");
        for (int[] e : E) {
            System.out.printf("    { %d, %d, %d },%n", e[0], e[1], e[2]);
        }
        System.out.println("}");
    }

    /** Return a deep copy of the contents of A. */
    public static int[][] deepCopy(int[][] A) {
        int[][] result = new int[A.length][];
        for (int i = 0; i < A.length; i += 1) {
            result[i] = Arrays.copyOf(A[i], A[i].length);
        }
        return result;
    }

    /** The command
     *        java Utils V MINEDGES MAXWEIGHT SEED [ P ]
     *  (the arguments become ARGS) generates a random graph with
     *  vertices 1 .. V with at least MINEDGES edges, each having weight
     *  between 0 and MAXWEIGHT inclusive. SEED is a random seed allowing
     *  for reproducible results.  Prints the total weight of an MST, using
     *  MST.mst.  If the last argument, P, is present, prints the edges. */
    public static void main(String... args) {
        int V = parseInt(args[0]),
            minEdges = parseInt(args[1]),
            maxWeight = parseInt(args[2]);
        long seed = parseLong(args[3]);

        int[][] E = randomConnectedGraph(V, minEdges, maxWeight, seed);
        if (args.length > 4) {
            printEdges(E);
        }
        int[][] T = MST.mst(V, E);
        System.out.println(totalWeight(T));
        if (args.length > 4) {
            printEdges(T);
        }
    }

}
