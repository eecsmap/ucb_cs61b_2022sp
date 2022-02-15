import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for Table.
 *
 * @author Matthew Owen
 */
public class TestTable {

    /**
     * Tests for Table Row
     */

    @Test
    public void testCreateRow() {
        List<String> rowValues = Arrays.asList("Data 0", "Data 1", "Data 2");
        Table.TableRow row = new Table.TableRow(rowValues);
        testRow(row, rowValues);
    }

    @Test
    public void testRowToString() {
        Table.TableRow row = new Table.TableRow(
                Arrays.asList("Data 0", "Data" + " 1", "Data 2"));
        assertEquals("Table Row toString Incorrect", "Data 0,Data 1,Data 2",
                row.toString());
    }

    @Test
    public void testJoinRows() {
        Table.TableRow row1 = new Table.TableRow(
                Arrays.asList("Data 0", "Data 1"));
        Table.TableRow row2 = new Table.TableRow(
                Arrays.asList("Data 2", "Data 3"));
        List<String> joinedValues = Arrays.asList("Data 0", "Data 1",
                "Data " + "2", "Data 3");
        Table.TableRow joinedRow = Table.TableRow.joinRows(row1, row2);
        testRow(joinedRow, joinedValues);
    }

    /**
     * Tests for Table Creation
     */

    @Test
    public void testCreateTableFromFile() {
        Table t = new Table("sample_db/ta_data_struct.db");

        List<String> expectedHeaderList = Arrays.asList("first_name",
                "last_name",
                "fav_data_structure");
        List<String> expectedRow0 = Arrays.asList("Itai", "Smith",
                "Red Black" + " Tree");
        List<String> expectedRow1 = Arrays.asList("Matt", "Owen", "kd-tree");
        List<String> expectedRow2 = Arrays.asList("Michelle", "Hwang",
                "HashMap");
        List<String> expectedRow3 = Arrays.asList("Sean", "Dooher",
                "Merkle " + "Hash Tree");
        List<String> expectedRow4 = Arrays.asList("Omar", "Khan", "LinkedList");

        assertEquals("Header is Incorrect", expectedHeaderList, t.headerList());
        testRow(t.getRow(0), expectedRow0);
        testRow(t.getRow(1), expectedRow1);
        testRow(t.getRow(2), expectedRow2);
        testRow(t.getRow(3), expectedRow3);
        testRow(t.getRow(4), expectedRow4);
    }

    @Test
    public void testCreateTableFromFileWhiteSpace() {
        Table t = new Table("sample_db/white_space.db");

        List<String> expectedHeaderList = Arrays.asList("first_name",
                "last_name",
                "fav_data_structure");
        List<String> expectedRow0 = Arrays.asList("Itai", "Smith",
                "Red Black" + " Tree");
        List<String> expectedRow1 = Arrays.asList("Matt", "Owen", "kd-tree");
        List<String> expectedRow2 = Arrays.asList("Michelle", "Hwang",
                "HashMap");
        List<String> expectedRow3 = Arrays.asList("Sean", "Dooher",
                "Merkle " + "Hash Tree");
        List<String> expectedRow4 = Arrays.asList("Omar", "Khan", "LinkedList");

        assertEquals("Header is Incorrect", expectedHeaderList, t.headerList());
        testRow(t.getRow(0), expectedRow0);
        testRow(t.getRow(1), expectedRow1);
        testRow(t.getRow(2), expectedRow2);
        testRow(t.getRow(3), expectedRow3);
        testRow(t.getRow(4), expectedRow4);
    }

    @Test
    public void testCreateTableFromFileMissing() {
        Table t = new Table("sample_db/missing_vals.db");

        List<String> expectedHeaderList = Arrays.asList("first_name",
                "last_name",
                "fav_data_structure");
        List<String> expectedRow0 = Arrays.asList("Itai", "", "");
        List<String> expectedRow1 = Arrays.asList("Matt", "Owen", "kd-tree");
        List<String> expectedRow2 = Arrays.asList("Michelle", "Hwang", "");
        List<String> expectedRow3 = Arrays.asList("", "Dooher",
                "Merkle Hash " + "Tree");
        List<String> expectedRow4 = Arrays.asList("Omar", "Khan", "LinkedList");

        assertEquals("Header is Incorrect", expectedHeaderList, t.headerList());
        testRow(t.getRow(0), expectedRow0);
        testRow(t.getRow(1), expectedRow1);
        testRow(t.getRow(2), expectedRow2);
        testRow(t.getRow(3), expectedRow3);
        testRow(t.getRow(4), expectedRow4);
    }

    @Test
    public void testCreateFileDoesNotExist() {
        boolean exceptionThrown = false;
        try {
            new Table("sample_db/not_a_real_file.db");
        } catch (Table.TableException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testCreateBadRows() {
        boolean exceptionThrown = false;
        try {
            new Table("sample_db/bad_rows.db");
        } catch (Table.TableException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Tests for Joining
     */
    @Test
    public void testJoin() {
        Table t1 = new Table("sample_db/ta_data_struct.db");
        Table t2 = new Table("sample_db/ta_color.db");
        Table expectedJoined = new Table("sample_db/joined.db");
        Table joined = Table.join(t1, t2);

        assertEquals("Header is incorrect.", expectedJoined.headerList(),
                joined.headerList());

        for (int i = 0; i < expectedJoined.numRows(); i += 1) {
            testRow(joined.getRow(i), expectedJoined.getRow(i));
        }
    }

    /**
     * Tests for TableFilter
     */

    @Test
    public void testIdentityFilter() {
        Table t = new Table("sample_db/ta_color.db");
        Table filtered = Table.filter(new IdentityFilter(t));

        List<String> expectedHeaderList = Arrays.asList("first_name",
                "last_name",
                "fav_color");
        List<String> expectedRow0 = Arrays.asList("Matt", "Owen", "Yellow");
        List<String> expectedRow1 = Arrays.asList("Michelle", "Hwang", "Black");
        List<String> expectedRow2 = Arrays.asList("Itai", "Smith", "Blue");
        List<String> expectedRow3 = Arrays.asList("Tina", "Zhao", "Salmon");
        List<String> expectedRow4 = Arrays.asList("Carlo", "Cruz-Albrecht",
                "Blue");
        List<String> expectedRow5 = Arrays.asList("Shivani", "Kishnani",
                "Emerald");

        assertEquals("Filtered Table size is incorrect.", 6,
                filtered.numRows());
        assertEquals("Header is Incorrect", expectedHeaderList,
                filtered.headerList());
        testRow(filtered.getRow(0), expectedRow0);
        testRow(filtered.getRow(1), expectedRow1);
        testRow(filtered.getRow(2), expectedRow2);
        testRow(filtered.getRow(3), expectedRow3);
        testRow(filtered.getRow(4), expectedRow4);
        testRow(filtered.getRow(5), expectedRow5);
    }

    @Test
    public void testEqualityFilter() {
        Table t = new Table("sample_db/ta_color.db");
        Table filtered = Table.filter(
                new EqualityFilter(t, "fav_color", "Blue"));

        List<String> expectedHeaderList = Arrays.asList("first_name",
                "last_name",
                "fav_color");
        List<String> expectedRow0 = Arrays.asList("Itai", "Smith", "Blue");
        List<String> expectedRow1 = Arrays.asList("Carlo", "Cruz-Albrecht",
                "Blue");

        assertEquals("Filtered Table size is incorrect.", 2,
                filtered.numRows());
        assertEquals("Header is Incorrect", expectedHeaderList,
                filtered.headerList());
        testRow(filtered.getRow(0), expectedRow0);
        testRow(filtered.getRow(1), expectedRow1);
    }

    @Test
    public void testGreaterThanFilter() {
        Table t = new Table("sample_db/ta_color.db");
        Table filtered = Table.filter(
                new GreaterThanFilter(t, "fav_color", "Brown"));

        List<String> expectedHeaderList = Arrays.asList("first_name",
                "last_name",
                "fav_color");
        List<String> expectedRow0 = Arrays.asList("Matt", "Owen", "Yellow");
        List<String> expectedRow1 = Arrays.asList("Tina", "Zhao", "Salmon");
        List<String> expectedRow2 = Arrays.asList("Shivani", "Kishnani",
                "Emerald");

        assertEquals("Filtered Table size is incorrect.", 3,
                filtered.numRows());
        assertEquals("Header is Incorrect", expectedHeaderList,
                filtered.headerList());
        testRow(filtered.getRow(0), expectedRow0);
        testRow(filtered.getRow(1), expectedRow1);
        testRow(filtered.getRow(2), expectedRow2);
    }

    @Test
    public void testSubstringFilter() {
        Table t = new Table("sample_db/ta_color.db");
        Table filtered = Table.filter(
                new SubstringFilter(t, "fav_color", "al"));

        List<String> expectedHeaderList = Arrays.asList("first_name",
                "last_name",
                "fav_color");
        List<String> expectedRow0 = Arrays.asList("Tina", "Zhao", "Salmon");
        List<String> expectedRow1 = Arrays.asList("Shivani", "Kishnani",
                "Emerald");

        assertEquals("Filtered Table size is incorrect.", 2,
                filtered.numRows());
        assertEquals("Header is Incorrect", expectedHeaderList,
                filtered.headerList());
        testRow(filtered.getRow(0), expectedRow0);
        testRow(filtered.getRow(1), expectedRow1);
    }

    @Test
    public void testColumnMatchFilter() {
        Table joined = new Table("sample_db/joined.db");
        Table filtered = Table.filter(
                new ColumnMatchFilter(joined, "t1" + ".first_name",
                        "t2.first_name"));

        List<String> expectedHeaderList = Arrays.asList("t1.first_name",
                "t1" + ".last_name",
                "t1.fav_data_structure",
                "t2.first_name",
                "t2" + ".last_name",
                "t2.fav_color");
        List<String> expectedRow0 = Arrays.asList("Itai", "Smith",
                "Red Black" + " Tree", "Itai",
                "Smith", "Blue");
        List<String> expectedRow1 = Arrays.asList("Matt", "Owen", "kd-tree",
                "Matt", "Owen", "Yellow");
        List<String> expectedRow2 = Arrays.asList("Michelle", "Hwang",
                "HashMap", "Michelle",
                "Hwang", "Black");

        assertEquals("Filtered Table size is incorrect.", 3, filtered.numRows());
        assertEquals("Header is Incorrect", expectedHeaderList,
                filtered.headerList());
        testRow(filtered.getRow(0), expectedRow0);
        testRow(filtered.getRow(1), expectedRow1);
        testRow(filtered.getRow(2), expectedRow2);
    }


    /**
     * Testing Helper Functions
     */

    private void testRow(Table.TableRow row, List<String> values) {
        assertEquals("TableRow Size Incorrect", values.size(), row.size());
        for (int i = 0; i < row.size(); i++) {
            assertEquals(
                    "Element " + i + " Incorrect for:\nExpectedRow: "
                            + listString(values) + "\nActualRow: " + row + "\n",
                    values.get(i), row.getValue(i));
        }
    }

    private void testRow(Table.TableRow actualRow, Table.TableRow expectedRow) {
        assertEquals("TableRow Size Incorrect", expectedRow.size(),
                actualRow.size());
        for (int i = 0; i < expectedRow.size(); i++) {
            assertEquals(
                    "Element " + i + " Incorrect for:\nExpectedRow: "
                            + expectedRow + "\nActualRow: " + actualRow + "\n",
                    expectedRow.getValue(i), actualRow.getValue(i));
        }
    }

    private String listString(List<String> list) {
        return String.join(",", list);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(TestTable.class));
    }
}
