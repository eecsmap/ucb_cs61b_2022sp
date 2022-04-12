import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

public class RedBlackTreeTest {
    @Rule
    public Timeout globalTimeout = Timeout.seconds(3);

    /* Make sure that the tree follows binary search tree
     * rules (i.e. inorder traversal is sorted),
     * make sure that the root is black, and check that the
     * tree is balanced and never has two red nodes in a row. */
    private void checkTree(RedBlackTree.RBTreeNode<Integer> root,
                           Integer[] items) {

        assertEquals("Inorder traversal of tree does not yield "
                     + "original list.",
                     Arrays.asList(items), allItems(root));
        assertTrue("Root of tree must be black", root.isBlack);
        checkAlternate(root, true);
        checkHeights(root);
    }

    private ArrayList<Integer>
        allItems(RedBlackTree.RBTreeNode<Integer> root) {
        ArrayList<Integer> result = new ArrayList<>();
        allItems(root, result);
        return result;
    }

    /* Populates the input items list to contain an inorder
     * (and thus, sorted) traversal of the input red
     * black tree. */
    private void allItems(RedBlackTree.RBTreeNode<Integer> root,
                          ArrayList<Integer> items) {
        if (root != null) {
            allItems(root.left, items);
            items.add(root.item);
            allItems(root.right, items);
        }
    }

    /* Make sure we never have two red nodes in a row, and that red children
     * are always left-leaning. */
    private void checkAlternate(RedBlackTree.RBTreeNode<Integer> root,
                                boolean parentBlack) {
        if (root != null) {
            assertFalse("Bad color alternation near " + root.item,
                        !root.isBlack && !parentBlack);
            assertFalse("Not left-leaning near " + root.item,
                        root.isBlack && root.right != null
                        && !root.right.isBlack);
            checkAlternate(root.left, root.isBlack);
            checkAlternate(root.right, root.isBlack);
        }
    }

    /* Check that all paths have an equal number of black nodes. */
    private int checkHeights(RedBlackTree.RBTreeNode<Integer> root) {
        if (root == null) {
            return 1;
        }
        int leftHt = checkHeights(root.left),
            rightHt = checkHeights(root.right);
        assertEquals("Paths to leaves have differing black heights near "
                     + root.item,
                     leftHt, rightHt);
        if (root.isBlack) {
            return leftHt + 1;
        } else {
            return leftHt;
        }
    }

    /* Given an array of items to insert, add them to a new
     * Red Black Tree in a seeded random order. Then, check that the
     * tree is valid.  */
    private void doTest(Integer[] items, long seed) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        ArrayList<Integer> itemsList =
            new ArrayList<>(Arrays.asList(items));
        Collections.shuffle(itemsList, new Random(seed));
        for (Integer item : itemsList) {
            tree.insert(item);
        }
        checkTree(tree.graderRoot(), items);
    }


    /* See the hw8 intro vid for this example. */
    @Test
    public void rotateLeftTest() {
        RedBlackTree.RBTreeNode A = new RedBlackTree.RBTreeNode<String>(false, "a");
        RedBlackTree.RBTreeNode W = new RedBlackTree.RBTreeNode<String>(true, "w");
        RedBlackTree.RBTreeNode Z = new RedBlackTree.RBTreeNode<String>(true, "z");
        RedBlackTree.RBTreeNode Y = new RedBlackTree.RBTreeNode<String>(false, "y", W, Z);
        RedBlackTree.RBTreeNode B = new RedBlackTree.RBTreeNode<String>(true, "b", A, null);
        RedBlackTree.RBTreeNode T = new RedBlackTree.RBTreeNode<String>(true, "t", B, Y);

        RedBlackTree tester = new RedBlackTree();
        tester.rotateLeft(T);

        assertEquals("T should be the left child of Y", Y.left, T);
        assertTrue("Y should now be T's old color", Y.isBlack);
        assertFalse("T should be red", T.isBlack);
        assertEquals("T should adopt Y's old left child", T.right, W);

        assertEquals("No other parts of the tree should change", Y.right, Z);
        assertEquals("No other parts of the tree should change", T.left, B);
        assertEquals("No other parts of the tree should change", B.left, A);
    }

    /* See the hw8 intro vid for this example.
    *  Note that the intro video does not contain a node C, but this test does. */
    @Test
    public void rotateRightTest() {
        RedBlackTree.RBTreeNode A = new RedBlackTree.RBTreeNode<String>(false, "a");
        RedBlackTree.RBTreeNode W = new RedBlackTree.RBTreeNode<String>(false, "w");
        RedBlackTree.RBTreeNode C = new RedBlackTree.RBTreeNode<String>(true, "c");
        RedBlackTree.RBTreeNode Y = new RedBlackTree.RBTreeNode<String>(true, "y", W, null);
        RedBlackTree.RBTreeNode B = new RedBlackTree.RBTreeNode<String>(false, "b", A, C);
        RedBlackTree.RBTreeNode T = new RedBlackTree.RBTreeNode<String>(true, "t", B, Y);

        RedBlackTree tester = new RedBlackTree();
        tester.rotateRight(T);

        assertEquals("T should be the right child of B", B.right, T);
        assertTrue("B should now be T's old color", B.isBlack);
        assertFalse("T should be red", T.isBlack);
        assertEquals("T should adopt B's old right child", T.left, C);

        assertEquals("No other parts of the tree should change", Y.left, W);
        assertEquals("No other parts of the tree should change", T.right, Y);
        assertEquals("No other parts of the tree should change", B.left, A);
    }

    /* Test inserting just a single element.
     * Note that the seed passed in has L at the end to indicate
     * that it is a LONG number type. */
    @Test
    public void oneTest() {
        doTest(new Integer[] { 1 }, 123L);
    }

    /* Test inserting just a 3 elements.
     * Note that the seed passed in has L at the end to indicate
     * that it is a LONG number type. */
    @Test
    public void smallTest() {
        doTest(new Integer[] { 1, 2, 3 }, 1234L);
    }

    // TODO: Create a larger test?

    // TODO: Create tests that targets each of case A, B, and C?


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(RedBlackTreeTest.class));
    }

}
