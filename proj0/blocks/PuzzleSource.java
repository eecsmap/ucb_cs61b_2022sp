package blocks;

/** Describes a source of Blocks puzzles.
 *  @author P. N. Hilfinger
 */
interface PuzzleSource {

    /** Deal HANDSIZE>0 pieces to MODEL, clearing any previous ones. Returns
     *  true on success, or false if there are no more hands. */
    boolean deal(Model model, int handSize);

    /** Reseed the random number generator with SEED. */
    void setSeed(long seed);

}
