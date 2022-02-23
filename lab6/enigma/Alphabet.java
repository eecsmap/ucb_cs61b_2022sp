package enigma;

/**
 * An alphabet of encodable characters.  Provides a mapping from characters
 * to and from indices into the alphabet. For lab6, this is made an abstract
 * class so we don't give you the solutions. In proj1, this is a concrete
 * class you will need to implement.
 *
 * @author Michelle Hwang
 */
abstract class Alphabet {

    /**
     * Constructor1 should be in the format: Alphabet(String chars)
     * A new alphabet containing CHARS.  Character number #k in the alphabet
     * has index k (numbering from 0). No character may be duplicated.
     */

    /**
     * Constructor2 should be in the format: Alphabet()
     * A default alphabet of all upper-case characters, i.e. ABCD...Z.
     */

    /**
     * Returns the size of the alphabet.
     */
    abstract int size();

    /**
     * Returns true if ch is in this alphabet.
     * @param ch the character to test
     */
    abstract boolean contains(char ch);

    /**
     * Returns character number INDEX in the alphabet, where
     * 0 <= INDEX < size().
     */
    abstract char toChar(int index);

    /**
     * Returns the index of character ch, which must be in
     * the alphabet. This is the inverse of toChar().
     * @param ch the character to convert to its index in the Alphabet
     */
    abstract int toInt(char ch);

}
