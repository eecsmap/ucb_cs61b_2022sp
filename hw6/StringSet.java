import java.util.List;

/** Interface for a basic String set.
  * @author Josh Hug and Paul Hilfinger */
public interface StringSet {
    /** Adds the string S to the string set. If it is already present in the
      * set, do nothing. */
    void put(String s);

    /** Returns true iff S is in the string set. */
    boolean contains(String s);

    /** Return a list of all members of this set. */
    List<String> asList();
}
