/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

/** A generic Ataxx Player.
 *  @author P. N. Hilfinger
 */
abstract class Player {

    /** A Player that will play MYCOLOR in GAME. */
    Player(Game game, PieceColor myColor) {
        _game = game;
        _myColor = myColor;
    }

    /** Return my pieces' color. */
    PieceColor myColor() {
        return _myColor;
    }

    /** Return true iff I am automated. */
    boolean isAuto() {
        return false;
    }

    /** Return the game I am playing in. */
    Game game() {
        return _game;
    }

    /** Return the board I am playing on. The caller should not modify this
     *  board. */
    Board getBoard() {
        return _game.getBoard();
    }

    /** Return a legal move or command for my side. Assumes that
     *  board.whoseMove() == myColor() and that the game is not over. */
    abstract String getMove();

    /** The game I am playing in. */
    private final Game _game;
    /** The color of my pieces. */
    private final PieceColor _myColor;
}
