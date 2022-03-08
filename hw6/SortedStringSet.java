import java.util.Iterator;

/** A type of StringSet with a bounded iterator producing Strings in
 *  ascending lexicographic order.
 *  @author P. N. Hilfinger.
 */
public interface SortedStringSet extends StringSet {
    /** Return an Iterator yielding all my strings that are between L
     *  (inclusive) and U (exclusive) in ascending order. */
    Iterator<String> iterator(String L, String U);
}
