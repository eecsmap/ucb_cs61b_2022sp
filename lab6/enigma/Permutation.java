package enigma;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet. For lab6, this is made an abstract
 * class so we don't give you the solutions. In proj1, this is a concrete
 * class you will need to implement.
 * @author Michelle Hwang
 */
abstract class Permutation {

    /**
     * Constructor should be in the format:
     * Permutation(String cycles, Alphabet alphabet)
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     * */

    /**
     * Returns the size of the alphabet I permute.
     */
    abstract int size();

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    abstract int permute(int p);

    /**
     * Return the result of applying the inverse of this permutation
     * to C modulo the alphabet size.
     */
    abstract int invert(int c);

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    abstract char permute(char p);

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    abstract char invert(char c);

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    abstract Alphabet alphabet();

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    abstract boolean derangement();
}
