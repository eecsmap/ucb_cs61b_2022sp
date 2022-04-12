/**
 * Generic BTree class that exposes the root to the world.
 *
 * @param <T> Type of items to hold.
 * @author Paul Hilfinger
 */
public class BTree<T extends Comparable<T>> {

    /** Root of the tree. */
    protected Node<T> root;

    /**
     * Btree node. supports getting items/children at given indices and counts
     * of items/children. Allows for setting child at given index. Should hold
     * the invariant that getItemCount() + 1 == getChildrenCount(). Holds
     * items of type T.
     * ith child (subtree) has elements less than ith item, and i+1th child
     * (subtree) has elements greater than ith item and less than i+1th item.
     */
    public interface Node<T> {

        /**
         * Returns the number of items in this node.
         *
         * @return number of items.
         */
        int getItemCount();

        /**
         * Returns the number of children in this node. It should equal to
         * getItemCount() + 1.
         *
         * @return number of children.
         */
        int getChildrenCount();

        /**
         * Returns item at index i. Must hold that 0 <= i < getItemCount().
         *
         * @param i index of item.
         * @return item at i.
         */
        T getItemAt(int i);

        /**
         * Returns child at index I. Must hold that I is in the half-open
         * interval [0 ..  getChildrenCount()).
         */
        Node<T> getChildAt(int i);

        /**
         * Sets child at index I to NODE. This is a pointer-setting
         * operation. Must hold * that 0 <= I < getChildrenCount().
         */
        void setChildAt(int i, Node<T> node);
    }

    /**
     * 2-4 Node that implements Btree.Node. Designed to be immutable. Items
     * are immutable of type T (no insertion or deletion allowed), so
     * any modification should be done through new Node creation.
     */
    static class TwoThreeFourNode<T> implements Node<T> {

        /** Keys. */
        private final T[] items;
        /** Children. */
        private final Node<T>[] children;

        /** A node containing the keys in KEYS and null children. */
        @SuppressWarnings("unchecked")
        TwoThreeFourNode(T... keys) {
            items = (T[]) new Object[keys.length];
            System.arraycopy(keys, 0, this.items, 0, keys.length);
            children = (Node<T>[]) new Node[keys.length + 1];
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        @Override
        public int getChildrenCount() {
            return children.length;
        }

        @Override
        public T getItemAt(int i) {
            if (i < 0 || i >= items.length) {
                throw new IllegalArgumentException("No item at index: " + i);
            }
            return items[i];
        }

        @Override
        public Node<T> getChildAt(int i) {
            return children[i];
        }

        @Override
        public void setChildAt(int i, Node<T> node) {
            if (i < 0 || i >= children.length) {
                throw new IllegalArgumentException(
                    "Child index out of bounds: " + i);
            }
            if (!(node instanceof TwoThreeFourNode)) {
                throw new IllegalArgumentException(
                    "Children of 2-4 node must be a 2-4 node.");
            }

            children[i] = node;
        }
    }
}
