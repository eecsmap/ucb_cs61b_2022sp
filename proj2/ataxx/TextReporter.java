/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import static ataxx.PieceColor.*;

/** An object that formats and sends messages and errors by printing them.
 *  @author P. N. Hilfinger
 */
class TextReporter implements Reporter {

    @Override
    public void announceWin(PieceColor side) {
        if (side == EMPTY) {
            msg("* Draw.");
        } else {
            msg("* %s wins.", side.toString());
        }
    }

    @Override
    public void announceMove(Move move, PieceColor player) {
        msg("* %s moves %s.", player, move);
    }

    @Override
    public void msg(String format, Object... args) {
        System.out.printf(format, args);
        System.out.println();
    }

    @Override
    public void err(String format, Object... args) {
        System.err.printf(format, args);
        System.err.println();
    }

}
