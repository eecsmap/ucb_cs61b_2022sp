/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import org.junit.Test;

import static ataxx.PieceColor.*;
import static org.junit.Assert.*;

/** Tests of the Board class.
 *  @author P. N. Hilfinger
 */
public class BoardTest {

    private static void makeMoves(Board b, String[] moves) {
        for (String s : moves) {
            b.makeMove(s.charAt(0), s.charAt(1),
                    s.charAt(3), s.charAt(4));
        }
    }

    private static final char[] COLS = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
    private static final char[] ROWS = {'1', '2', '3', '4', '5', '6', '7'};

    private static void checkBoard(Board b, PieceColor[][] expectedColors) {
        assertEquals(Board.SIDE, expectedColors.length);
        assertEquals(Board.SIDE, expectedColors[0].length);
        for (int r = 0; r < expectedColors.length; r++) {
            for (int c = 0; c < expectedColors[0].length; c++) {
                assertEquals("incorrect color at "
                                + COLS[c] + ROWS[ROWS.length - 1 - r],
                        expectedColors[r][c],
                        b.get(COLS[c], ROWS[ROWS.length - 1 - r]));
            }
        }
    }

    @Test
    public void testConstructor() {
        Board b = new Board();
        assertEquals("bottom right not RED", RED, b.get('g', '1'));
        assertEquals("bottom left not BLUE", BLUE, b.get('a', '1'));
        assertEquals("top right not BLUE", BLUE, b.get('g', '7'));
        assertEquals("top left not RED", RED, b.get('a', '7'));
        checkBoard(b, INITIAL);

        assertNull("winner not null", b.getWinner());
        assertEquals("redPieces not 2", 2, b.redPieces());
        assertEquals("bluePieces not 2", 2, b.bluePieces());

        assertEquals("not red's move", RED, b.whoseMove());
        assertEquals("num moves not 0", 0, b.numMoves());
        assertEquals("num jumps not 0", 0, b.numJumps());
    }

    @Test
    public void testInitialLegalMove() {
        Board b = new Board();
        assertTrue("a7-a6 should be legal",
                b.legalMove('a', '7', 'a', '6'));
        assertTrue("g1-f2 should be legal",
                b.legalMove('g', '1', 'f', '2'));

        assertTrue("a7-c7 should be legal",
                b.legalMove('a', '7', 'c', '7'));
        assertTrue("g1-e3 should be legal",
                b.legalMove('g', '1', 'e', '3'));

        assertFalse("a7-a4 should not be legal",
                b.legalMove('a', '7', 'a', '4'));
        assertFalse("g1-d2 should not be legal",
                b.legalMove('g', '1', 'd', '2'));
        assertFalse("moving into the border should not be legal",
                b.legalMove('a', '7', (char) ('a' - 1), '7'));
        assertFalse("moving into the border should not be legal",
                b.legalMove('a', '7', (char) ('a' - 2), '7'));
        assertFalse("moving into the border should not be legal",
                b.legalMove('g', '1', (char) ('g' + 1), (char) ('1' - 1)));

        assertEquals("not RED's move", RED, b.whoseMove());
        assertFalse("blue moving should not be legal",
                b.legalMove('a', '1', 'a', '2'));
    }

    @Test
    public void testSimpleExtends() {
        Board b = new Board();

        b.makeMove('a', '7', 'a', '6');
        assertEquals("top left not red", RED, b.get('a', '7'));
        assertEquals("a6 not red", RED, b.get('a', '6'));
        assertEquals("redPieces not 3", 3, b.redPieces());
        assertEquals("numMoves not 1", 1, b.numMoves());
        assertEquals("numJumps not 0", 0, b.numJumps());
        assertEquals("not BLUE's move", BLUE, b.whoseMove());
        checkBoard(b, REDEXTEND1);

        b.makeMove('a', '1', 'b', '2');
        assertEquals("bottom left not blue", BLUE, b.get('a', '1'));
        assertEquals("b2 not blue", BLUE, b.get('b', '2'));
        assertEquals("bluePieces not 3", 3, b.bluePieces());
        assertEquals("numMoves not 2", 2, b.numMoves());
        assertEquals("numJumps not 0", 0, b.numJumps());
        assertEquals("not RED's move", RED, b.whoseMove());
        checkBoard(b, BLUEEXTEND1);
    }

    @Test
    public void testSimpleJumps() {
        Board b = new Board();

        b.makeMove('a', '7', 'c', '7');
        assertEquals("top left not empty", EMPTY, b.get('a', '7'));
        assertEquals("c7 not red", RED, b.get('c', '7'));
        assertEquals("redPieces not 2", 2, b.redPieces());
        assertEquals("numMoves not 1", 1, b.numMoves());
        assertEquals("numJumps not 1", 1, b.numJumps());
        assertEquals("not BLUE's move", BLUE, b.whoseMove());
        checkBoard(b, REDJUMP1);

        b.makeMove('a', '1', 'c', '3');
        assertEquals("bottom left not empty", EMPTY, b.get('a', '1'));
        assertEquals("c3 not blue", BLUE, b.get('c', '3'));
        assertEquals("bluePieces not 2", 2, b.bluePieces());
        assertEquals("numMoves not 2", 2, b.numMoves());
        assertEquals("numJumps not 2", 2, b.numJumps());
        assertEquals("not RED's move", RED, b.whoseMove());
        checkBoard(b, BLUEJUMP1);
    }

    @Test
    public void testBlocks() {
        Board b = new Board();
        int originalTotalOpen = b.totalOpen();
        assertTrue("blocks not legal at a2", b.legalBlock('a', '2'));
        assertTrue("blocks not legal at d4", b.legalBlock('d', '4'));

        b.setBlock('a', '2');
        assertEquals("wrong total open after block at a2",
                originalTotalOpen - 4, b.totalOpen());
        b.setBlock('d', '4');
        assertEquals("wrong total open after block at d4",
                originalTotalOpen - 5, b.totalOpen());
        checkBoard(b, BLOCKS1);

        assertFalse("blocks not placeable on pieces", b.legalBlock('a', '1'));
        assertFalse("blocks not placeable on other blocks",
                b.legalBlock('g', '6'));

        b.makeMove('a', '7', 'b', '7');
        assertFalse("blocks not placeable once game starts",
                b.legalBlock('d', '5'));
    }

    @Test
    public void testGame() {
        Board b = new Board();
        String[] moves = {
            "a7-a6", "a1-b1",
            "g1-f1", "g7-f6",
            "a6-a5", "a1-a2",
            "g1-g2", "b1-b2"
        };
        makeMoves(b, moves);
        checkBoard(b, GAMEEXTENDS);
        assertEquals("not 8 moves", 8, b.numMoves());
        assertEquals("not 6 red pieces", 6, b.redPieces());
        assertEquals("not 6 blue pieces", 6, b.bluePieces());
        assertEquals("not RED's move", RED, b.whoseMove());
        assertNull("no winner yet", b.getWinner());

        b.makeMove('a', '5', 'a', '3');
        checkBoard(b, GAMEJUMP1);
        assertEquals("not 8 red pieces", 8, b.redPieces());
        assertEquals("not 4 blue pieces", 4, b.bluePieces());
        assertEquals("not BLUE's move", BLUE, b.whoseMove());

        b.makeMove('a', '1', 'b', '3');
        checkBoard(b, GAMEJUMP2);
        assertEquals("not 5 red pieces", 5, b.redPieces());
        assertEquals("not 7 blue pieces", 7, b.bluePieces());
        assertEquals("not RED's move", RED, b.whoseMove());
        String[] moreMoves = {
            "a6-a4", "b2-b4",
            "a7-a5", "b3-b5",
            "g1-f2", "b5-c4",
            "f2-d3", "b4-c3",
            "f1-e1"
        };
        makeMoves(b, moreMoves);
        checkBoard(b, GAMEJUMP3);
        assertNull("no winner yet", b.getWinner());
        assertEquals("not BLUE's move", BLUE, b.whoseMove());

        b.makeMove('d', '3', 'f', '2');
        checkBoard(b, GAMEJUMP4);
        assertEquals("no red pieces", 0, b.redPieces());
        assertEquals("blue should win", BLUE, b.getWinner());
    }

    @Test
    public void testJumps() {
        Board b = new Board();
        b.makeMove('g', '1', 'g', '2');
        b.makeMove('g', '7', 'g', '6');
        b.makeMove('g', '2', 'g', '3');
        b.makeMove('g', '6', 'g', '5');
        checkBoard(b, JUMPSINITIAL);
        b.makeMove('g', '3', 'g', '4');
        checkBoard(b, JUMPS1);
        b.makeMove('g', '6', 'f', '4');
        checkBoard(b, JUMPS2);
    }

    @Test
    public void testCopyAndClear() {
        Board b0 = new Board();
        makeMoves(b0, GAME1);
        b0.clear();
        checkBoard(b0, INITIAL);
        assertNull("winner not null", b0.getWinner());
        assertEquals("redPieces not 2", 2, b0.redPieces());
        assertEquals("bluePieces not 2", 2, b0.bluePieces());

        assertEquals("not red's move", RED, b0.whoseMove());
        assertEquals("num moves not 0", 0, b0.numMoves());
        assertEquals("num jumps not 0", 0, b0.numJumps());

        Board copyOfCleared = new Board(b0);
        makeMoves(b0, GAME1);
        Board b1 = new Board(b0);
        checkBoard(b0, GAME1RESULT);
        checkBoard(b1, GAME1RESULT);
        checkBoard(copyOfCleared, INITIAL);
        assertNull("winner not null", copyOfCleared.getWinner());
        assertEquals("redPieces not 2", 2, copyOfCleared.redPieces());
        assertEquals("bluePieces not 2", 2, copyOfCleared.bluePieces());

        assertEquals("not red's move", RED, copyOfCleared.whoseMove());
        assertEquals("num moves not 0", 0, copyOfCleared.numMoves());
        assertEquals("num jumps not 0", 0, copyOfCleared.numJumps());

        b0.makeMove('b', '7', 'b', '5');
        checkBoard(b1, GAME1RESULT);
        assertNull("winner not null", b1.getWinner());
        assertEquals("redPieces not 4", 4, b1.redPieces());
        assertEquals("bluePieces not 6", 6, b1.bluePieces());

        assertEquals("not red's move", RED, b1.whoseMove());
        assertEquals("num jumps not 0", 0, b1.numJumps());
    }

    @Test
    public void testUndo() {
        Board b0 = new Board();
        Board b1 = new Board(b0);
        makeMoves(b0, GAME1);
        Board b2 = new Board(b0);
        for (int i = 0; i < GAME1.length; i += 1) {
            b0.undo();
        }
        assertEquals("failed to return to start", b1, b0);
        makeMoves(b0, GAME1);
        assertEquals("second pass failed to reach same position", b2, b0);

        b2.makeMove('b', '7', 'c', '7');
        b2.undo();
        assertEquals("copied board undone incorrectly", b2, b0);

        b2.makeMove('b', '7', 'd', '7');
        b2.undo();
        assertEquals("copied board undone incorrectly", b2, b0);
    }

    @Test
    public void testUndo2() {
        Board b = new Board();
        makeMoves(b, UNDO2MOVES);
        checkBoard(b, GAMEJUMP3);
        assertEquals("not BLUE's move", BLUE, b.whoseMove());
        assertEquals("wrong numMoves", 19, b.numMoves());
        assertEquals("wrong numJumps", 0, b.numJumps());
        assertEquals("wrong redPieces", 4, b.redPieces());
        assertEquals("wrong bluePieces", 12, b.bluePieces());
        b.undo();
        checkBoard(b, GAMEJUMP3UNDO);
        assertEquals("not RED's move", RED, b.whoseMove());
        assertEquals("wrong numMoves", 18, b.numMoves());
        assertEquals("wrong numJumps", 0, b.numJumps());
        assertEquals("wrong redPieces", 3, b.redPieces());
        assertEquals("wrong bluePieces", 12, b.bluePieces());
        b.makeMove('f', '1', 'f', '2');
        checkBoard(b, GAMEJUMP3ALT);
        assertEquals("not BLUE's move", BLUE, b.whoseMove());
        assertEquals("wrong numMoves", 19, b.numMoves());
        assertEquals("wrong numJumps", 0, b.numJumps());
        assertEquals("wrong redPieces", 4, b.redPieces());
        assertEquals("wrong bluePieces", 12, b.bluePieces());
        b.undo();
        checkBoard(b, GAMEJUMP3UNDO);
        assertEquals("not RED's move", RED, b.whoseMove());
        assertEquals("wrong numMoves", 18, b.numMoves());
        assertEquals("wrong numJumps", 0, b.numJumps());
        assertEquals("wrong redPieces", 3, b.redPieces());
        assertEquals("wrong bluePieces", 12, b.bluePieces());
        b.makeMove('g', '2', 'e', '1');
        checkBoard(b, GAMEJUMP3ALT2);
        assertEquals("not BLUE's move", BLUE, b.whoseMove());
        assertEquals("wrong numMoves", 19, b.numMoves());
        assertEquals("wrong numJumps", 1, b.numJumps());
        assertEquals("wrong redPieces", 3, b.redPieces());
        assertEquals("wrong bluePieces", 12, b.bluePieces());
        b.makeMove('d', '3', 'f', '2');
        checkBoard(b, GAMEJUMP4ALT);
        assertEquals("wrong red pieces", 0, b.redPieces());
        assertEquals("wrong numMoves", 20, b.numMoves());
        assertEquals("wrong bluePieces", 15, b.bluePieces());
        assertEquals("wrong numJumps", 2, b.numJumps());
        assertEquals("blue should win", BLUE, b.getWinner());
        b.undo();
        checkBoard(b, GAMEJUMP3ALT2);
        assertEquals("not BLUE's move", BLUE, b.whoseMove());
        assertEquals("wrong numMoves", 19, b.numMoves());
        assertEquals("wrong numJumps", 1, b.numJumps());
        assertEquals("wrong redPieces", 3, b.redPieces());
        assertEquals("wrong bluePieces", 12, b.bluePieces());
        assertNull("no winner after undo", b.getWinner());
        b.undo();
        assertEquals("not RED's move", RED, b.whoseMove());
        assertEquals("wrong numMoves", 18, b.numMoves());
        assertEquals("wrong numJumps", 0, b.numJumps());
        assertEquals("wrong redPieces", 3, b.redPieces());
        assertEquals("wrong bluePieces", 12, b.bluePieces());
    }

    private static final String[] GAME1 = {
        "a7-b7", "a1-a2",
        "a7-a6", "a2-a3",
        "a6-a5", "a3-a4"
    };

    private static final String[] UNDO2MOVES = {
        "a7-a6", "a1-b1",
        "g1-f1", "g7-f6",
        "a6-a5", "a1-a2",
        "g1-g2", "b1-b2",
        "a5-a3", "a1-b3",
        "a6-a4", "b2-b4",
        "a7-a5", "b3-b5",
        "g1-f2", "b5-c4",
        "f2-d3", "b4-c3",
        "f1-e1"
    };

    private static final PieceColor[][] GAME1RESULT = {
        {RED, RED, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceColor[][] INITIAL = {
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceColor[][] REDEXTEND1 = {
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceColor[][] BLUEEXTEND1 = {
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceColor[][] REDJUMP1 = {
        {EMPTY, EMPTY, RED, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceColor[][] BLUEJUMP1 = {
        {EMPTY, EMPTY, RED, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceColor[][] BLOCKS1 = {
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {BLOCKED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCKED},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, BLOCKED, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLOCKED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCKED},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceColor[][] GAMEEXTENDS = {
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, RED, RED}
    };

    private static final PieceColor[][] GAMEJUMP1 = {
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {RED, RED, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, RED, RED}
    };

    private static final PieceColor[][] GAMEJUMP2 = {
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {EMPTY, BLUE, EMPTY, EMPTY, EMPTY, RED, RED}
    };

    private static final PieceColor[][] GAMEJUMP3 = {
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, BLUE, BLUE, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {EMPTY, BLUE, EMPTY, EMPTY, RED, RED, RED}
    };

    private static final PieceColor[][] GAMEJUMP3UNDO = {
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, BLUE, BLUE, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {EMPTY, BLUE, EMPTY, EMPTY, EMPTY, RED, RED}
    };

    private static final PieceColor[][] GAMEJUMP3ALT = {
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, BLUE, BLUE, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, RED, RED},
        {EMPTY, BLUE, EMPTY, EMPTY, EMPTY, RED, RED}
    };

    private static final PieceColor[][] GAMEJUMP3ALT2 = {
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, BLUE, BLUE, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, BLUE, EMPTY, EMPTY, RED, RED, RED}
    };

    private static final PieceColor[][] GAMEJUMP4 = {
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, BLUE},
        {EMPTY, BLUE, EMPTY, EMPTY, BLUE, BLUE, BLUE}
    };

    private static final PieceColor[][] GAMEJUMP4ALT = {
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
        {BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, BLUE, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, BLUE, EMPTY, EMPTY, EMPTY, EMPTY},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, EMPTY},
        {EMPTY, BLUE, EMPTY, EMPTY, BLUE, BLUE, BLUE}
    };

    private static final PieceColor[][] JUMPSINITIAL = {
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceColor[][] JUMPS1 = {
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };

    private static final PieceColor[][] JUMPS2 = {
        {RED, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLUE},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED},
        {BLUE, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, RED}
    };
}
