import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Perform tests of the IntDList class
 *
 * @author P. N. Hilfinger; updated by Linda Deng (1/26/2022)
 */

public class IntDListTest {

    private IntDList d;

    /**
     * Tests the constructor and size operations.
     */
    @Test
    public void testSize() {
        d = new IntDList();
        assertEquals("Size of empty", 0, d.size());
        d = new IntDList(5);
        assertEquals("Size of singleton", 1, d.size());
        d = new IntDList(5, 10, 15);
        assertEquals("Size of 3-element list", 3, d.size());
    }

    /**
     * Test front and back.
     */
    @Test
    public void testFrontBack() {
        d = new IntDList(0);
        assertEquals("getFront", 0, d.getFront());
        assertEquals("getBack", 0, d.getBack());

        d = new IntDList(0, 5);
        assertEquals("getFront", 0, d.getFront());
        assertEquals("getBack", 5, d.getBack());

        d = new IntDList(0, 5, 10);
        assertEquals("getFront", 0, d.getFront());
        assertEquals("getBack", 10, d.getBack());

        d = new IntDList(0, 5, 10, 15);
        assertEquals("getFront", 0, d.getFront());
        assertEquals("getBack", 15, d.getBack());
    }

    /**
     * Tests the get operation.
     */
    @Test
    public void testGet() {
        d = new IntDList(0, 5, 10, 15);
        assertEquals(".get(0)", 0, d.get(0));
        assertEquals(".get(1)", 5, d.get(1));
        assertEquals(".get(2)", 10, d.get(2));
        assertEquals(".get(3)", 15, d.get(3));
    }

    /**
     * Test insertBack
     */
    @Test
    public void testInsertBack() {
        d = new IntDList();
        d.insertBack(0);

        assertEquals("Size after 1 insert should be 1", 1, d.size());
        assertEquals("First element after inserting 0 should be 0", 0, d.get(0));

        d.insertBack(5);
        assertEquals("Size after 2 inserts should be 2", 2, d.size());
        assertEquals("First element after inserting 5 should be 0", 0, d.get(0));
        assertEquals("Second element after inserting 5 should be 5", 5, d.get(1));

        d.insertBack(10);
        assertEquals("Size after 3 inserts should be 3", 3, d.size());
        assertEquals("First element after inserting 10 should be 0", 0, d.get(0));
        assertEquals("Second element after inserting 10 should be 5", 5, d.get(1));
        assertEquals("Third element after inserting 10 should be 10", 10, d.get(2));

        d.insertBack(15);
        assertEquals("Size after 4 inserts should be 4", 4, d.size());
        assertEquals("First element after inserting 15 should be 0", 0, d.get(0));
        assertEquals("Second element after inserting 15 should be 5", 5, d.get(1));
        assertEquals("Third element after inserting 15 should be 10", 10, d.get(2));
        assertEquals("Fourth element after inserting 15 should be 15", 15, d.get(3));
    }

    /**
     * Test insertFront
     */
    @Test
    public void testInsertFront() {
        d = new IntDList();
        d.insertFront(0);

        assertEquals("Size after 1 insert should be 1", 1, d.size());
        assertEquals("First element after inserting 0 should be 0", 0, d.get(0));

        d.insertFront(5);
        assertEquals("Size after 2 inserts should be 2", 2, d.size());
        assertEquals("First element after inserting 5 should be 5", 5, d.get(0));
        assertEquals("Second element after inserting 5 should be 0", 0, d.get(1));

        d.insertFront(10);
        assertEquals("Size after 3 inserts should be 3", 3, d.size());
        assertEquals("First element after inserting 10 should be 10", 10, d.get(0));
        assertEquals("Second element after inserting 10 should be 5", 5, d.get(1));
        assertEquals("Third element after inserting 10 should be 0", 0, d.get(2));

        d.insertFront(15);
        assertEquals("Size after 4 inserts should be 4", 4, d.size());
        assertEquals("First element after inserting 15 should be 15", 15, d.get(0));
        assertEquals("Second element after inserting 15 should be 10", 10, d.get(1));
        assertEquals("Third element after inserting 15 should be 5", 5, d.get(2));
        assertEquals("Fourth element after inserting 15 should be 0", 0, d.get(3));
    }

    /**
     * Test insertAtIndex
     */
    @Test
    public void testInsertAtIndex() {
        d = new IntDList();
        d.insertAtIndex(5, 0);
        assertEquals("Size after 1 insert should be 5", 1, d.size());
        assertEquals("Front element after insert should be 5", 5, d.getFront());
        assertEquals("Back element after insert should be 5", 5, d.getBack());
        assertEquals("First element after inserting 5 should be 5", 5, d.get(0));


        d = new IntDList(5, 10, 15, 20);
        d.insertAtIndex(1, 0);
        assertEquals("Size after insert should be 5", 5, d.size());
        assertEquals("Front element after insert should be 1", 1, d.getFront());
        assertEquals("Back element after insert should be 20", 20, d.getBack());
        assertEquals("First element after insert should be 1", 1, d.get(0));
        assertEquals("Second element after insert should be 5", 5, d.get(1));
        assertEquals("Third element after insert should be 10", 10, d.get(2));
        assertEquals("Fourth element after insert should be 15", 15, d.get(3));
        assertEquals("Fifth element after insert should be 20", 20, d.get(4));

        d = new IntDList(5, 10, 15, 20);
        d.insertAtIndex(13, 2);
        assertEquals("Size after insert should be 5", 5, d.size());
        assertEquals("First element after insert should be 5", 5, d.get(0));
        assertEquals("Second element after insert should be 10", 10, d.get(1));
        assertEquals("Third element after insert should be 13", 13, d.get(2));
        assertEquals("Fourth element after insert should be 15", 15, d.get(3));
        assertEquals("Fifth element after insert should be 20", 20, d.get(4));

        d = new IntDList(5, 10, 15, 20);
        d.insertAtIndex(25, 4);
        assertEquals("Size after insert should be 5", 5, d.size());
        assertEquals("Front element after insert should be 5", 5, d.getFront());
        assertEquals("Back element after insert should be 25", 25, d.getBack());
        assertEquals("First element after insert should be 5", 5, d.get(0));
        assertEquals("Second element after insert should be 10", 10, d.get(1));
        assertEquals("Third element after insert should be 15", 15, d.get(2));
        assertEquals("Fourth element after insert should be 20", 20, d.get(3));
        assertEquals("Fifth element after insert should be 25", 25, d.get(4));
    }

    /**
     * Tests deleteFront
     */
    @Test
    public void testDeleteFront() {
        d = new IntDList(0, 5, 10, 15);
        assertEquals(".deleteFront() value (0)", 0, d.deleteFront());
        assertEquals(".getFront()", 5, d.getFront());
        assertEquals(".size() after 1 deleteFront", 3, d.size());
        assertEquals(".deleteFront() value (5)", 5, d.deleteFront());
        assertEquals(".getFront()", 10, d.getFront());
        assertEquals(".size() after 2 deleteFront", 2, d.size());
        assertEquals(".deleteFront() value (10)", 10, d.deleteFront());
        assertEquals(".getFront()", 15, d.getFront());
        assertEquals(".size() after 3 deleteFront", 1, d.size());
        assertEquals(".deleteFront() value (15)", 15, d.deleteFront());
        assertEquals(".size() after delete", 0, d.size());
    }

    /**
     * Test deleteBack
     */
    @Test
    public void testDeleteBack() {
        d = new IntDList(0, 5, 10, 15);
        assertEquals(".deleteBack() value (15)", 15, d.deleteBack());
        assertEquals(".getBack()", 10, d.getBack());
        assertEquals(".size() after 1 deleteBack", 3, d.size());
        assertEquals(".deleteBack() value (10)", 10, d.deleteBack());
        assertEquals(".getBack()", 5, d.getBack());
        assertEquals(".size() after 2 deleteBack", 2, d.size());
        assertEquals(".deleteBack() value (5)", 5, d.deleteBack());
        assertEquals(".getBack()", 0, d.getBack());
        assertEquals(".size() after 3 deleteBack", 1, d.size());
        assertEquals(".deleteBack() value (0)", 0, d.deleteBack());
        assertEquals(".size() after delete", 0, d.size());
    }

    /**
     * Test deleteAtIndex
     */
    @Test
    public void testDeleteAtIndex() {
        d = new IntDList(5);
        assertEquals(".deleteAtIndex(0) value (5)", 5, d.deleteAtIndex(0));
        assertEquals("Size after delete should be 0", 0, d.size());
        assertNull("Front should point to null", d._front);
        assertNull("Back should point to null", d._back);

        d = new IntDList(5, 10);
        assertEquals(".deleteAtIndex(0) value (5)", 5, d.deleteAtIndex(0));
        assertEquals("Size after delete should be 1", 1, d.size());
        assertEquals(".getFront() should return 10", 10, d.getFront());
        assertEquals(".getBack() should return 10", 10, d.getBack());
        assertEquals("First element after delete should be (10)", 10, d.get(0));

        d = new IntDList(5, 10, 15, 20, 25);
        assertEquals(".deleteAtIndex(2) value (15)", 15, d.deleteAtIndex(2));
        assertEquals("Size after delete should be 4", 4, d.size());
        assertEquals(".getFront() should return 5", 5, d.getFront());
        assertEquals(".getBack() should return 25", 25, d.getBack());
        assertEquals("First element after delete should be (5)", 5, d.get(0));
        assertEquals("Second element after delete should be (10)", 10, d.get(1));
        assertEquals("Third element after delete should be (20)", 20, d.get(2));
        assertEquals("Fourth element after delete should be (25)", 25, d.get(3));

        d = new IntDList(5, 10, 15, 20, 25);
        assertEquals(".deleteAtIndex(4) value (25)", 25, d.deleteAtIndex(4));
        assertEquals("Size after delete should be 4", 4, d.size());
        assertEquals(".getFront() should return 5", 5, d.getFront());
        assertEquals(".getBack() should return 20", 20, d.getBack());
        assertEquals("First element after delete should be (5)", 5, d.get(0));
        assertEquals("Second element after delete should be (10)", 10, d.get(1));
        assertEquals("Third element after delete should be (15)", 15, d.get(2));
        assertEquals("Fourth element after delete should be (20)", 20, d.get(3));
    }

    /**
     * Test toString
     */
    @Test
    public void testToString() {
        IntDList d = new IntDList(5, 10, 15, 20);
        assertEquals(".toString()", "[5, 10, 15, 20]", d.toString());
        assertEquals(".toString() of empty", "[]", new IntDList().toString());
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(IntDListTest.class));
    }
}
