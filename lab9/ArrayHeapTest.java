import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArrayHeapTest {

    /** Adds and removes one item. **/
    @Test
    public void insertOne() {
        ArrayHeap<String> hp = new ArrayHeap<>();
        hp.insert("Paul", 100);
        assertEquals("Size should be 1.", 1, hp.size());
        assertEquals("Min item should be \"Paul\" when peeking.",
                "Paul", hp.peek());
        assertEquals("Min item should be \"Paul\" when removing.",
                "Paul", hp.removeMin());
    }

    /** Adds and removes four items with ascending priority values. **/
    @Test
    public void insertAscending() {
        ArrayHeap<String> hp = new ArrayHeap<>();
        String[] items = {"Paul", "Josh", "John", "Dan"};
        int[] priorities = {1, 2, 3, 4};
        for (int i = 0; i < items.length; i++) {
            hp.insert(items[i], priorities[i]);
        }
        assertEquals("Size should be 4", 4, hp.size());
        for (int i = 0; i < items.length; i++) {
            assertEquals(String.format("%d-th peek call", i),
                    items[i], hp.peek());
            assertEquals(String.format("%d-th removeMin call", i),
                    items[i], hp.removeMin());
        }
    }

    /** Adds and removes four items with descending priority values. **/
    @Test
    public void insertDescending() {
        ArrayHeap<String> hp = new ArrayHeap<>();
        String[] items = {"Alex", "Henry", "Anjali", "Linda"};
        int[] priorities = {4, 3, 2, 1};
        for (int i = 0; i < items.length; i++) {
            hp.insert(items[i], priorities[i]);
        }
        assertEquals("Size should be 4", 4, hp.size());
        for (int i = 0; i < items.length; i++) {
            assertEquals(String.format("%d-th peek call", i),
                    items[items.length - 1 - i], hp.peek());
            assertEquals(String.format("%d-th removeMin call", i),
                    items[items.length - 1 - i], hp.removeMin());
        }
    }

    /** Inserts and removes 8 items in no particular order. Each item's
     * priority value is itself. **/
    @Test
    public void insertMany() {
        ArrayHeap<Integer> hp = new ArrayHeap<>();
        int[] items = {2, 9, -7, 3, 1, -1, 10, 8};
        int[] sortedItems = {-7, -1, 1, 2, 3, 8, 9, 10};
        for (int item: items) {
            hp.insert(item, item);
        }
        for (int i = 0; i < sortedItems.length; i++) {
            assertEquals(String.format("removing %d-th item", i),
                    (Integer) sortedItems[i], hp.removeMin());
        }
    }

    /** Ensures that removeMin and peek return null if heap is empty. **/
    @Test
    public void removeMinPeekNull() {
        ArrayHeap<String> hp = new ArrayHeap<>();
        hp.insert("Pie", 314);
        assertEquals("Removing min", "Pie", hp.removeMin());
        assertNull(hp.peek());
        assertNull(hp.removeMin());
    }

    /** Changes the priority value of one item to be higher. **/
    @Test
    public void changePriorityIncreaseOne() {
        ArrayHeap<String> hp = new ArrayHeap<>();
        String[] items = {"Hashmap", "Linked list", "LLRB tree",
                "Deque", "Graph"};
        int[] priorities = {1, 2, 3, 4, 5};
        for (int i = 0; i < items.length; i++) {
            hp.insert(items[i], priorities[i]);
        }
        hp.changePriority("LLRB tree", 4.5);
        String[] expected = {"Hashmap", "Linked list", "Deque",
                "LLRB tree", "Graph"};
        for (int i = 0; i < items.length; i++) {
            assertEquals(String.format("%d-th removeMin call", i),
                    expected[i], hp.removeMin());
        }
    }

    /** Changes the priority value of one item to be lower. **/
    @Test
    public void changePriorityDecreaseOne() {
        ArrayHeap<String> hp = new ArrayHeap<>();
        String[] items = {"Hashmap", "Linked list", "LLRB tree",
                "Deque", "Graph"};
        int[] priorities = {1, 2, 3, 4, 5};
        for (int i = 0; i < items.length; i++) {
            hp.insert(items[i], priorities[i]);
        }
        hp.changePriority("LLRB tree", 0);
        String[] expected = {"LLRB tree", "Hashmap", "Linked list",
                "Deque", "Graph"};
        for (int i = 0; i < items.length; i++) {
            assertEquals(String.format("%d-th removeMin call", i),
                    expected[i], hp.removeMin());
        }
    }

    /** Inserts 8 integers with their priorities as themselves.
     * Then changes each of their priorities to be the negation
     * of their original priority. **/
    @Test
    public void changePriorityAll() {
        ArrayHeap<Integer> hp = new ArrayHeap<>();
        int[] items = {2, 9, -7, 3, 1, -1, 10, 8};
        int[] sortedItems = {-7, -1, 1, 2, 3, 8, 9, 10};
        for (int item: items) {
            hp.insert(item, item);
        }
        for (int item: items) {
            hp.changePriority(item, -1 * item);
        }
        for (int i = 0; i < sortedItems.length; i++) {
            assertEquals(String.format("removing %d-th item", i),
                    (Integer) sortedItems[sortedItems.length - 1 - i],
                    hp.removeMin());
        }
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArrayHeapTest.class));
    }
}
