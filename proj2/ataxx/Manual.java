/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import static ataxx.PieceColor.*;
import static ataxx.Command.Type.*;

/** A Player that receives its moves from its Game's getMoveCmnd method.
 *  @author P. N. Hilfinger
 */
class Manual extends Player {

    /** A Player that will play MYCOLOR on GAME, taking its moves from
     *  GAME. */
    Manual(Game game, PieceColor myColor) {
        super(game, myColor);
        _prompt = myColor + "> ";
    }

    @Override
    String getMove() {
        Game game = game();
        Board board = getBoard();
        while (true) {
            return game.getCommand(_prompt);
        }
    }

    /** The User serving as a source of input commands. */
    private String _prompt;
}

