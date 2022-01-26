package blocks;

import ucb.gui2.Pad;

import java.util.concurrent.ArrayBlockingQueue;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.MouseEvent;
import static java.awt.RenderingHints.*;

import static blocks.BoardWidget.Style.*;
import static blocks.Utils.*;

/** A widget that displays a Signpost puzzle.
 *  @author P. N. Hilfinger
 */
class BoardWidget extends Pad {

    /* Parameters controlling sizes, speeds, colors, and fonts. */

    /** Colors of squares, arrows, and grid lines. */
    static final Color
        BACKGROUND_COLOR = Color.white,
        GRID_LINE_COLOR = Color.black,
        EMPTY_COLOR = Color.white,
        FILLED_COLOR = Color.blue,
        UNAVAILABLE_COLOR = Color.LIGHT_GRAY,
        PREVIEW_COLOR = new Color(0, 0, 255, 128),
        DONE_COLOR = new Color(0, 0, 255, 64);

    /** Dimensions of features and spacing. */
    static final int
        CELL_SIDE = 25,
        SMALL_CELL_SIDE = CELL_SIDE / 2,
        GRID_LINE_WIDTH = 1,
        OFFSET = 2,
        CELL_OFFSET = (GRID_LINE_WIDTH + 1) / 2,
        HAND_VERT_SEP = 8,
        FLOAT_HEIGHT = CELL_SIDE,
        SHRINK_TO_SMALL = CELL_SIDE / 4;

    /** Number of drag events per repainting of piece position. */
    static final int DRAG_EVENT_COLLAPSE = 10;

    /** Strokes for ordinary grid lines and those that are parts of
     *  boundaries. */
    static final BasicStroke
        GRIDLINE_STROKE = new BasicStroke(GRID_LINE_WIDTH);

    /** Styles in which pieces and their cells are drawn. */
    enum Style {
        NORMAL, REMOVEABLE, FADE_OUT, PREVIEW, IN_HAND, IN_HAND_UNAVAILABLE,
        FLOATING
    }

    /** A graphical representation of a Signpost board that sends commands
     *  derived from mouse clicks to COMMANDS. */
    BoardWidget(ArrayBlockingQueue<String> commands) {
        _commands = commands;
        setMouseHandler("press", this::mousePressed);
        setMouseHandler("drag", this::mouseDragged);
        setMouseHandler("release", this::mouseReleased);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        _selectedPiece = -1;
        _numDrags = 0;
    }

    /** Draw the grid lines on G. */
    private void drawGrid(Graphics2D g) {
        g.setColor(GRID_LINE_COLOR);
        g.setStroke(GRIDLINE_STROKE);
        g.drawLine(cx(0), cy(0), cx(_width), cy(0));
        g.drawLine(cx(0), cy(0), cx(0), cy(_height));
        g.drawLine(cx(0), cy(_height), cx(_width), cy(_height));
        g.drawLine(cx(_width), cy(0), cx(_width), cy(_height));

        for (int row = 0; row < _height; row += 1) {
            for (int col = 0; col < _width; col += 1) {
                if (col + 1 < _width) {
                    g.drawLine(cx(col + 1), cy(row), cx(col + 1), cy(row + 1));
                }
                if (row + 1 < _height) {
                    g.drawLine(cx(col), cy(row + 1), cx(col + 1), cy(row + 1));
                }
            }
        }
    }

    /** Set the cell colors on G from the model. */
    private void drawCells(Graphics2D g) {
        Piece p = _model.piece(_selectedPiece);
        int srow = refRow(p, _sx, _sy),
            scol = refCol(p, _sx, _sy);
        int[][] counts;
        Style style;

        counts = null;
        style = NORMAL;
        if (_model.placeable(p, srow, scol)) {
            _model.pushState();
            _model.place(p, srow, scol);
            counts = _model.rowColumnCounts();
            _model.undo();
            drawGridPiece(g, p, PREVIEW, srow, scol);
        } else if (_model.roundOver()) {
            style = FADE_OUT;
        }

        g.setColor(FILLED_COLOR);
        for (int row = 0; row < _height; row += 1) {
            for (int col = 0; col < _width; col += 1) {
                if (_model.get(row, col)) {
                    if (counts != null
                        && (counts[0][row] == _model.width()
                            || counts[1][col] == _model.height())) {
                        drawGridPiece(g, ONE_CELL, REMOVEABLE, row, col);
                    } else {
                        drawGridPiece(g, ONE_CELL, style, row, col);
                    }
                }
            }
        }
    }

    /** Draw the remaining Pieces of the hand on G. */
    private void drawHand(Graphics2D g) {
        for (int k = 0; k < _model.handSize(); k += 1) {
            Piece p = _model.piece(k);
            if (p != null && k != _selectedPiece) {
                drawPiece(g, p,
                          _model.placeable(p) ? IN_HAND : IN_HAND_UNAVAILABLE,
                          hx(k), hy(k));
            }
        }
    }

    /** Draw the currently selected Piece, if any, on G. */
    private void drawSelected(Graphics2D g) {
        if (_selectedPiece == -1) {
            return;
        }
        Piece p = _model.piece(_selectedPiece);
        assert p != null;
        drawPiece(g, p, FLOATING, _sx, _sy);
    }

    /** Draw PIECE on G at with format STYLE so that its center
     *  is at (CX, CY). */
    private static void drawPiece(Graphics2D g, Piece piece, Style style,
                                  int cx, int cy) {
        int size, shrink;
        size = shrink = 0;
        switch (style) {
        case IN_HAND:
            g.setColor(FILLED_COLOR);
            size = SMALL_CELL_SIDE;
            break;
        case IN_HAND_UNAVAILABLE:
            g.setColor(UNAVAILABLE_COLOR);
            size = SMALL_CELL_SIDE;
            break;
        case FLOATING:
            g.setColor(FILLED_COLOR);
            size = CELL_SIDE;
            shrink = 1;
            break;
        case PREVIEW:
            g.setColor(PREVIEW_COLOR);
            size = CELL_SIDE;
            break;
        case NORMAL:
            g.setColor(FILLED_COLOR);
            size = CELL_SIDE;
            break;
        case REMOVEABLE:
            g.setColor(FILLED_COLOR);
            size = CELL_SIDE;
            shrink = SHRINK_TO_SMALL;
            break;
        case FADE_OUT:
            g.setColor(DONE_COLOR);
            size = CELL_SIDE;
            break;
        default:
            assert false;
        }
        int x = cx - piece.width() * size / 2,
            y = cy - piece.height() * size / 2;

        for (int row = 0; row < piece.height(); row += 1) {
            for (int col = 0; col < piece.width(); col += 1) {
                if (piece.get(row, col)) {
                    drawCell(g, x + col * size, y + row * size, size, shrink);
                }
            }
        }
    }

    /** Draw PIECE on G at with format STYLE so that its reference point is
     *  is at grid position (ROW, COL). */
    private static void drawGridPiece(Graphics2D g, Piece piece, Style style,
                                      int row, int col) {
        drawPiece(g, piece, style,
                  col * CELL_SIDE + piece.width() * CELL_SIDE / 2 + OFFSET,
                  row * CELL_SIDE + piece.height() * CELL_SIDE / 2 + OFFSET);
    }

    /** Fill in a cell of size SIZE on G whose upper-left corner is at (X, Y)
     *  in the current color, with side of size SIZE, leaving an additional
     *  SHRINK pixels around the filled area unchanged.  */
    private static void drawCell(Graphics2D g, int x, int y, int size,
                                 int shrink) {
        g.fillRect(x + CELL_OFFSET + shrink, y + CELL_OFFSET + shrink,
                   size - GRID_LINE_WIDTH - 2 * shrink,
                   size - GRID_LINE_WIDTH - 2 * shrink);
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, _boardWidth, _boardHeight);

        drawCells(g);
        drawGrid(g);
        drawHand(g);
        drawSelected(g);
    }

    /** Handle mouse-pressed event E, selecting a Piece from the hand, if
     *  appropriate. */
    private synchronized void mousePressed(String unused, MouseEvent e) {
        if (_model == null) {
            return;
        }
        _selectedPiece = pieceNumber(e.getX(), e.getY());
        if (_selectedPiece != -1 && !_model.placeable(_selectedPiece)) {
            _selectedPiece = -1;
        }

        if (_selectedPiece != -1) {
            Piece p = _model.piece(_selectedPiece);
            _sx = sx(p, e);
            _sy = sy(p, e);
        }

        repaint();
        debug("Mouse pressed at (%d, %d), selecting %d.",
              _sx, _sy, _selectedPiece);
    }

    /** Handle mouse-released event E, selecting a Piece from the hand, if
     *  appropriate. */
    private synchronized void mouseReleased(String unused, MouseEvent e) {
        if (_model == null) {
            return;
        }

        if (_selectedPiece >= 0) {
            Piece p = _model.piece(_selectedPiece);
            int col = refCol(p, _sx, _sy), row = refRow(p, _sx, _sy);
            if (_model.placeable(_selectedPiece, row, col)) {
                _commands.offer(String.format("SET %d %d %d",
                                              _selectedPiece, row, col));
            }
            repaint();
            debug("Mouse released at (%d, %d), square (%d, %d), selecting %d",
                  e.getX(), e.getY(), row, col, _selectedPiece);
        } else {
            debug("Mouse released.");
        }
        _selectedPiece = -1;
    }

    /** Handle mouse-dragged event E, selecting a Piece from the hand, if
     *  appropriate. */
    private synchronized void mouseDragged(String unused, MouseEvent e) {
        if (_model == null || _selectedPiece == -1) {
            return;
        }
        _numDrags += 1;
        if (_numDrags % DRAG_EVENT_COLLAPSE != 0) {
            return;
        }

        Piece p = _model.piece(_selectedPiece);
        _sx = sx(p, e);
        _sy = sy(p, e);
        repaint();
        debug("Mouse dragged to (%d, %d), square (%d, %d)",
              _sx, _sy, refRow(p, _sx, _sy), refCol(p, _sx, _sy));
    }

    /** Revise the displayed board according to MODEL. */
    synchronized void update(Model model) {
        _model = new Model(model);
        _width = model.width();
        _height = model.height();
        _boardWidth = _width * CELL_SIDE + 2 * OFFSET;
        _boardHeight = (_height + Piece.MAX_PIECE_HEIGHT) * CELL_SIDE
            + 2 * OFFSET + HAND_VERT_SEP;
        setPreferredSize(_boardWidth, _boardHeight);
        setMinimumSize(_boardWidth, _boardHeight);
        repaint();
    }

    /** Return pixel coordinates of the top of row ROW relative to window. */
    private int cy(int row) {
        return OFFSET + row * CELL_SIDE;
    }

    /** Return pixel coordinates of the left side of column COL relative
     *  to window. */
    private int cx(int col) {
        return OFFSET + col * CELL_SIDE;
    }

    /** Return the y pixel coordinate of the middle of piece #N in the hand,
     *  relative to window. */
    private int hy(int n) {
        return (_height + Piece.MAX_PIECE_HEIGHT / 2) * CELL_SIDE
            + 2 * OFFSET + HAND_VERT_SEP;
    }

    /** Return the x pixel coordinate of the middle of piece #N in the hand,
     *  relative to window. */
    private int hx(int n) {
        return _boardWidth * (2 * n + 1) / 6;
    }

    /** Return the x coordinate of the center of PIECE, given that it is to be
     *  displayed according to the position of EVENT. */
    private int sx(Piece piece, MouseEvent event) {
        return event.getX();
    }

    /** Return the y coordinate of the center of PIECE, given that it is to be
     *  displayed according to the position of EVENT. */
    private int sy(Piece piece, MouseEvent event) {
        return event.getY() - FLOAT_HEIGHT - piece.height() * CELL_SIDE / 2;
    }

    /** Return the row number in the current model of the grid position of
     *  PIECE at (X, Y)---that is, the grid row closest to the reference
     *  point of PIECE when it is positioned at (X, Y). May be off the grid.
     *  Returns -1 if PIECE is null. */
    private int refRow(Piece piece, int x, int y) {
        if (piece == null) {
            return -1;
        }
        int uly = y - piece.height() * CELL_SIDE / 2;
        return (uly - OFFSET + CELL_SIDE / 2) / CELL_SIDE;
    }

    /** Return the column number in the current model of the grid position of
     *  PIECE at (X, Y)---that is, the grid row closest to the reference
     *  point of PIECE  when positioned at (X, Y).  May be off the grid.
     *  Returns -1 if PIECE is null. */
    private int refCol(Piece piece, int x, int y) {
        if (piece == null) {
            return -1;
        }
        int ulx = x - piece.width() * CELL_SIDE / 2;
        return (ulx - OFFSET + CELL_SIDE / 2) / CELL_SIDE;
    }

    /** Return the X coordinate of PIECE when its reference point is in
     *  column COL. */
    private int cellX(Piece piece, int col) {
        return OFFSET + (2 * col + piece.width()) * CELL_SIDE / 2;
    }

    /** Return the Y coordinate of PIECE when its reference point is in
     *  row ROW. */
    private int cellY(Piece piece, int row) {
        return OFFSET + (2 * row + piece.height()) * CELL_SIDE / 2;
    }

    /** Return true iff PIECE may be placed at the grid position corresponding
     *  to having the center of PIECE at (X, Y). */
    private boolean placeableAt(Piece piece, int x, int y) {
        return _model.placeable(piece,
                                refRow(piece, x, y), refCol(piece, x, y));
    }

    /** Return the index in the hand of Piece at (X, Y), or -1 if this
     *  position does not identify a Piece. */
    private int pieceNumber(int x, int y) {
        if (y < _height * CELL_SIDE + HAND_VERT_SEP) {
            return -1;
        }
        int k = 9 * _model.handSize() * x / _boardWidth;
        if (k % 9 == 0 || k % 9 == 8) {
            return -1;
        } else {
            return k / 9;
        }
    }

    /** Number of rows and of columns. */
    private int _height, _width;

    /** Queue on which to post commands (from mouse clicks). */
    private ArrayBlockingQueue<String> _commands;

    /** Current model being displayed. */
    private Model _model;
    /** Length (in pixels) of the side of the board. */
    private int _boardWidth, _boardHeight;
    /** Index of currently selected Piece from hand, or -1 if none. */
    private int _selectedPiece;
    /** Current coordinates of selected Piece.  Ignored if _selectedPiece == -1.
     */
    private int _sx, _sy;
    /** A 1x1 Piece for setting individual cells. */
    private static final Piece ONE_CELL = new Piece("*");
    /** Number of dragging events. */
    private int _numDrags;
}
