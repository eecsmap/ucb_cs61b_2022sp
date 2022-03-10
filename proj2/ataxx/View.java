/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

/** An updateable view of an Ataxx board.
 *  @author P. N. Hilfinger */
interface View {

    /** Update the current view of the game according to BOARD. */
    void update(Board board);

}
