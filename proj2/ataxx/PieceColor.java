/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import static ataxx.GameException.*;

/** Describes the classes of Piece on an Ataxx board.
 *  @author P. N. Hilfinger
 */
enum PieceColor {

    /** EMPTY: no piece.
     *  BLOCKED: square contains a block.
     *  RED, BLUE: piece colors. */
    EMPTY, BLOCKED,
    RED {
        @Override
        PieceColor opposite() {
            return BLUE;
        }

        @Override
        boolean isPiece() {
            return true;
        }
    },
    BLUE {
        @Override
        PieceColor opposite() {
            return RED;
        }

        @Override
        boolean isPiece() {
            return true;
        }
    };

    /** Return the piece color of my opponent, if defined. */
    PieceColor opposite() {
        throw new UnsupportedOperationException();
    }

    /** Return true iff I denote a piece rather than an empty square or
     *  block. */
    boolean isPiece() {
        return false;
    }

    @Override
    public String toString() {
        return capitalize(super.toString().toLowerCase());
    }

    /** Return WORD with first letter capitalized. */
    static String capitalize(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    /** Return the PieceColor denoted by COLOR. */
    static PieceColor parseColor(String color) {
        switch (color.toLowerCase()) {
        case "red": case "r":
            return RED;
        case "blue": case "b":
            return BLUE;
        default:
            throw error("invalid piece color: %s", color);
        }
    }


}
