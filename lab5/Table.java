import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Simple DataBase System which allows for joins and filtering.
 *
 * @author Matthew Owen
 */
public class Table implements Iterable<Table.TableRow> {

    /**
     * Initialize a Table without a header or any rows.
     */
    private Table() {
        _columnMap = new HashMap<>();
        _rows = new ArrayList<>();
    }

    /**
     * Initialize a Table from a file.
     */
    public Table(String file) {
        this();

        try {
            File f = new File(file);
            Scanner reader = new Scanner(f);
            String headerRow = reader.nextLine();
            initColumnMap(headerRow);
            while (reader.hasNextLine()) {
                String dataRow = reader.nextLine();
                addRow(dataRow);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new TableException(e.getMessage());
        }
    }

    /**
     * Initialize a mapping from column name to column index.
     */
    private void initColumnMap(String headerRow) {
        initColumnMap(
                Arrays.asList(headerRow.trim().split("[\\s]*,[\\s]*", -1)));
    }

    /**
     * Initialize a mapping from column name to column index.
     */
    private void initColumnMap(List<String> headerList) {
        for (int i = 0; i < headerList.size(); i++) {
            _columnMap.put(headerList.get(i), i);
        }
    }

    /**
     * Add a row to this. Errors if the data is not the correct size.
     */
    private void addRow(String dataRow) {
        addRow(new TableRow(
                Arrays.asList(dataRow.trim().split("[\\s]*," + "[\\s]*", -1))));
    }

    /**
     * Add a row to this. Errors if the data is not the correct size.
     */
    private void addRow(TableRow row) {
        if (row.size() != numColumns()) {
            throw new TableException("Row contains incorrect number of values");
        }
        _rows.add(row);
    }

    /**
     * Returns a string representation of the column header of this
     */
    private String headerRow() {
        return String.join(",", headerList());
    }

    /**
     * Returns the int corresponding to the column named colName.
     */
    public int colNameToIndex(String colName) {
        return _columnMap.get(colName);
    }

    /**
     * Returns the number of columns in this.
     */
    public int numColumns() {
        return _columnMap.size();
    }

    /**
     * Returns the number of rows in this.
     */
    public int numRows() {
        return _rows.size();
    }

    /**
     * Returns the list of columns in this, in the correct order.
     * <p>
     * Implementation here uses streams to sort the values in the keySet (the
     * column names)
     * by their values in the _columnMap(their column indices). The sorted
     * stream is then
     * collected into a List to be returned.
     */
    public List<String> headerList() {
        return _columnMap.keySet().stream().sorted(
            (c1, c2) -> _columnMap.get(c1) - _columnMap.get(c2)).collect(
            Collectors.toList());
    }

    /**
     * Return the ith row of this.
     */
    public TableRow getRow(int i) {
        return _rows.get(i);
    }

    /**
     * Returns the result of doing a cross join on two tables.
     * <p>
     * This implementation first creates a new header row for the joined
     * table which contains
     * the same names as before prepended with "t1." or "t2.". A new table is
     * initialized and
     * the column map is initialized. Then a JoinIterator is used to add each
     * of the joined
     * rows into the new table.
     */
    public static Table join(Table t1, Table t2) {
        List<String> t1HeaderList = t1.headerList().stream().map(
            (x) -> "t1." + x).collect(Collectors.toList());
        List<String> t2HeaderList = t2.headerList().stream().map(
            (x) -> "t2." + x).collect(Collectors.toList());
        List<String> headerlist = new ArrayList<>();
        headerlist.addAll(t1HeaderList);
        headerlist.addAll(t2HeaderList);

        Table joinedTable = new Table();
        joinedTable.initColumnMap(headerlist);

        for (TableRow row : new JoinIterator(t1, t2)) {
            joinedTable.addRow(row);
        }

        return joinedTable;
    }

    /**
     * Returns the result of doing a filtering a table using filter.
     * <p>
     * This implementation first creates a new table is initialized and the
     * column map is
     * initialized. Then the filter is used to iterate over the filtered rows
     * which are
     * then added to the new table.
     */
    public static Table filter(TableFilter filter) {
        Table filteredTable = new Table();
        filteredTable.initColumnMap(filter.headerList());

        for (TableRow row : filter) {
            filteredTable.addRow(row);
        }

        return filteredTable;
    }

    @Override
    public String toString() {
        return headerRow() + "\n" + _rows.stream().map(
                TableRow::toString).collect(Collectors.joining("\n"));
    }

    /**
     * Returns an iterator over the rows of the Table
     */
    public Iterator<TableRow> iterator() {
        return _rows.iterator();
    }

    /**
     * Map which stores column name to column index key-value pairs.
     */
    private HashMap<String, Integer> _columnMap;
    private ArrayList<TableRow> _rows;

    /**
     * Iterator over TableRows which returns joined rows from the cartesian
     * product
     * of two tables.
     */
    private static class JoinIterator implements Iterator<TableRow>,
            Iterable<TableRow> {

        private JoinIterator(Table t1, Table t2) {
            _table2 = t2;
            _tableIter1 = t1.iterator();
            _tableIter2 = t2.iterator();
            _currRow1 = _tableIter1.next();
        }

        @Override
        public boolean hasNext() {
            if (_nextRow == null) {
                // FIXME: Fill in the hasNext method to update the _nextRow
                //        variable to be the next joined row to be returned
                //        by the iterator. The rows should be returned in the
                //        order t1[0] + t2[0], t1[0] + t2[1], t1[0] + t2[2],
                //        ..., t1[n] + t2[m]. I.e. the first row of t1 should
                //        be joined to all the rows of t2, then the second row
                //        of t1 should be joined to all of the rows of t2,
                //        etc.
            }
            return _nextRow != null;
        }

        @Override
        public TableRow next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            TableRow returnRow = _nextRow;
            _nextRow = null;
            return returnRow;
        }

        @Override
        public final Iterator<Table.TableRow> iterator() {
            return this;
        }

        /**
         * Iterator over the rows of the first table.
         */
        private Iterator<TableRow> _tableIter1;

        /**
         * Iterator over the rows of the second table.
         */
        private Iterator<TableRow> _tableIter2;

        /**
         * The next joined row to be returned by .next()
         */
        private TableRow _nextRow;

        /**
         * The current row from the first table.
         */
        private TableRow _currRow1;

        /**
         * Store table2 for convenient creation of iterators when we "reset".
         */
        private Table _table2;
    }

    /**
     * Class that represents a single row in a Table.
     */
    public static class TableRow {

        public TableRow(List<String> data) {
            _values = new ArrayList<>();
            _values.addAll(data);
        }

        /**
         * Returns the ith value in this TableRow
         */
        public String getValue(int i) {
            return _values.get(i);
        }

        /**
         * Returns a TableRow which is the result of joining two table rows
         */
        public static TableRow joinRows(TableRow tr1, TableRow tr2) {
            ArrayList<String> newData = new ArrayList<>();
            newData.addAll(tr1._values);
            newData.addAll(tr2._values);
            return new TableRow(newData);
        }

        /**
         * Return the size of this TableRow.
         */
        public int size() {
            return _values.size();
        }

        @Override
        public String toString() {
            return String.join(",", _values);
        }

        /**
         * List of string values in this TableRow.
         */
        private ArrayList<String> _values;
    }

    /**
     * Class that represents exceptions in the creation and manipulation of
     * Tables.
     */
    public static class TableException extends RuntimeException {
        public TableException(String errorMessage) {
            super(errorMessage);
        }
    }

}
