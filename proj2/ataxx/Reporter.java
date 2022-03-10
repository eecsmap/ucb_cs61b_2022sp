/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

/** An object that formats and sends messages and errors.
 *  @author P. N. Hilfinger
 */
interface Reporter {

    /** Display an announcement that SIDE has won. EMPTY indicates a tie. */
    void announceWin(PieceColor side);

    /** Report move MOVE by PLAYER. */
    void announceMove(Move move, PieceColor player);

    /** Display a message indicated by FORMAT and ARGS, which have
     *  the same meaning as in String.format. */
    void msg(String format, Object... args);

    /** Report an error as specified by FORMAT and ARGS, which have
     *  the same meaning as in String.format. */
    void err(String format, Object... args);

}

