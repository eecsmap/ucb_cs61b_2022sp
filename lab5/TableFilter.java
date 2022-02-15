import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Abstract class that allows for filtered iteration over a Table.
 *
 * @author Matthew Owen, modified existing code by P. N. Hilfinger
 */
public abstract class TableFilter implements Iterator<Table.TableRow>,
        Iterable<Table.TableRow> {

    public TableFilter(Table input) {
        _input = input.iterator();
        _headerList = input.headerList();
        _valid = false;
    }

    /* "final" methods may not be overridden. */

    @Override
    public final boolean hasNext() {
        while (!_valid && _input.hasNext()) {
            _next = _input.next();
            _valid = keep();
        }
        return _valid;
    }

    @Override
    public final Table.TableRow next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        _valid = false;
        return _next;
    }

    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Iterator<Table.TableRow> iterator() {
        return this;
    }

    /**
     * Returns true iff the value of _next should be delivered by
     * next().  Override this to provide a particular type of
     * Filter.
     */
    protected abstract boolean keep();

    /**
     * Returns the current value of _next (from the input iterator), without
     * advancing that iterator.
     */
    protected final Table.TableRow candidateNext() {
        return _next;
    }

    /**
     * Returns copy of _headerList for external use
     */
    public final List<String> headerList() {
        return new ArrayList<>(_headerList);
    }

    /**
     * True iff the value of _next is valid.  If not, one or more new
     * values must be read from _input to set it.
     */
    private boolean _valid;

    /**
     * The next value to be delivered by this Filter.
     */
    protected Table.TableRow _next;

    /**
     * The iterator that supplies my values.
     */
    private Iterator<Table.TableRow> _input;

    /**
     * The list of columns in the table being filtered.
     */
    private List<String> _headerList;

}
