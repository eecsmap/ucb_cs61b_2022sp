package timing;

import java.awt.geom.AffineTransform;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphUtil extends JPanel {

    /* Graph variables */
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);

    private int _padding = 25;
    private int _labelPadding = 50;
    private Color _pointColor = new Color(100, 100, 100, 180);
    private Color _gridColor = new Color(200, 200, 200, 200);
    private int _pointWidth = 4;
    private int _numberYDivisions = 10;

    private List<List<Double>> _allScores;
    private List<Double> _xVals;
    private List<String> _dataLabels;
    private String _title;
    private String _xLabel;
    private String _yLabel;
    private Color[] _lineColors = {
            new Color(44, 102, 230, 180),
            new Color(200, 102, 230, 180),
            new Color(200, 40, 20, 180),
            new Color(44, 200, 20, 180),
            new Color(235,125,25)};

    public GraphUtil(List<List<Double>> allScores, List<Double> xVals, List<String> dataLabels, String title, String xLabel, String yLabel) {
        _allScores = allScores;
        _xVals = xVals;
        _dataLabels = dataLabels;
        _title = title;
        _xLabel = xLabel;
        _yLabel = yLabel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * _padding) - _labelPadding) / (getNumberScores() - 1);
        double yScale = ((double) getHeight() - 2 * _padding - _labelPadding) / (getMaxScore() - getMinScore());

        // draw white background
        g2.setColor(Color.WHITE);
        g2.setColor(Color.BLACK);

        // Get font metrics
        FontMetrics metrics = g2.getFontMetrics();

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < _numberYDivisions + 1; i++) {
            int x0 = _padding + _labelPadding;
            int x1 = _pointWidth + _padding + _labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - _padding * 2 - _labelPadding)) / _numberYDivisions + _padding + _labelPadding);
            int y1 = y0;
            if (getNumberScores() > 0) {
                g2.setColor(_gridColor);
                g2.drawLine(_padding + _labelPadding + 1 + _pointWidth, y0, getWidth() - _padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / _numberYDivisions)) * 100)) / 100.0 + "";
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // create y axis label.
        AffineTransform originalTransform = g2.getTransform();
        g2.rotate(-Math.PI/2);
        g2.drawString(_yLabel, -getHeight() / 2 - metrics.stringWidth(_yLabel) / 2, 20);
        g2.setTransform(originalTransform);

        // create x axis label.
        g2.drawString(_xLabel, getWidth() / 2 - metrics.stringWidth(_xLabel) / 2,getHeight() - 20);

        /* Create Legend */
        // nRepetitions label.
        String repString = _title;
        g2.drawString(repString, 100, 50 );

        // Sorters
        int spacing = metrics.getHeight() + 5;
        for (int i = 0; i < _dataLabels.size(); i += 1) {
            g2.setColor(Color.BLACK);
            g2.drawString(_dataLabels.get(i), 100 + _pointWidth * 3 + 5, 50 + spacing * (i + 1));
            g2.setColor(_lineColors[i]);
            g2.fillOval(100,50 + spacing * i + spacing / 2, _pointWidth * 3, _pointWidth * 3);
        }
        g2.setColor(Color.BLACK);

        // and for x axis
        for (int i = 0; i < getNumberScores(); i++) {
            if (getNumberScores() > 1) {
                int x0 = i * (getWidth() - _padding * 2 - _labelPadding) / (getNumberScores() - 1) + _padding + _labelPadding;
                int x1 = x0;
                int y0 = getHeight() - _padding - _labelPadding;
                int y1 = y0 - _pointWidth;
                if ((i % ((int) ((getNumberScores() / 20.0)) + 1)) == 0) {
                    g2.setColor(_gridColor);
                    g2.drawLine(x0, getHeight() - _padding - _labelPadding - 1 - _pointWidth, x1, _padding);
                    g2.setColor(Color.BLACK);
                    String xVal = _xVals.get(i).toString();
                    int labelWidth = metrics.stringWidth(xVal);
                    g2.drawString(xVal, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes
        g2.drawLine(_padding + _labelPadding, getHeight() - _padding - _labelPadding, _padding + _labelPadding, _padding);
        g2.drawLine(_padding + _labelPadding, getHeight() - _padding - _labelPadding, getWidth() - _padding, getHeight() - _padding - _labelPadding);


        // Graph points and lines.
        for (int index = 0; index < _allScores.size(); index += 1) {
            List<Double> scores = _allScores.get(index);
            List<Point> graphPoints = new ArrayList<>();
            for (int i = 0; i < getNumberScores(); i += 1) {
                int x1 = (int) (i * xScale + _padding + _labelPadding);
                int y1 = (int) ((getMaxScore() - scores.get(i)) * yScale + _padding);
                graphPoints.add(new Point(x1, y1));
            }

            Stroke oldStroke = g2.getStroke();
            g2.setColor(_lineColors[index]);
            g2.setStroke(GRAPH_STROKE);
            for (int i = 0; i < graphPoints.size() - 1; i += 1) {
                int x1 = graphPoints.get(i).x;
                int y1 = graphPoints.get(i).y;
                int x2 = graphPoints.get(i + 1).x;
                int y2 = graphPoints.get(i + 1).y;
                g2.drawLine(x1, y1, x2, y2);
            }

            for (Point graphPoint : graphPoints) {
                int x = graphPoint.x - _pointWidth / 2;
                int y = graphPoint.y - _pointWidth / 2;
                int ovalW = _pointWidth;
                int ovalH = _pointWidth;
                g2.fillOval(x, y, ovalW, ovalH);
            }

            g2.setStroke(oldStroke);
        }
    }

    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (List<Double> scores : _allScores) {
            for (Double score : scores) {
                minScore = Math.min(minScore, score);
            }
        }
        return minScore;
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (List<Double> scores : _allScores) {
            for (Double score : scores) {
                maxScore = Math.max(maxScore, score);
            }
        }
        return maxScore;
    }

    private int getNumberScores() {
        assert _allScores.size() >= 1;
        int number = _allScores.get(0).size();
        for (List<Double> scores : _allScores) {
            assert number == scores.size();
        }
        return number;
    }

    public void showGraph() {
        this.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
