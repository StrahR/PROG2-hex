package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import logika.Igra;
import logika.Player;
import runner.Runner;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener {

    private static class Colour {
        private static Color bg = Color.WHITE;
        private static Color fg = Color.BLACK;
        private static Color P1 = Color.RED;
        private static Color P2 = Color.BLUE;
    }

    public Platno() {
        setBackground(Colour.bg);
        this.addMouseListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(560, 560);
    }

    // Relativna širina črte
    private final static double LINE_WIDTH = 0.08;

    // Dolžina stranice hexa
    private double sideLength() {
        return Math.min(2 * getWidth() / (Math.sqrt(3) * (3 * Igra.size - 1)), 2 * getHeight() / (3 * Igra.size + 1.0));
    }

    private double[] topLeft(final int i, final int j) {
        final double w = sideLength();
        final double[] r = { w * (i + j / 2.0) * Math.sqrt(3), w * j * 3 / 2.0 };
        return r;
    }

    private int[][] extremalPoints(final int i, final int j) {
        final double w = sideLength();
        final double[] tl = topLeft(i, j);
        final int x1 = (int) (tl[0]);
        final int y1 = (int) (tl[1] + w / 2);
        final int x2 = (int) (tl[0] + w * Math.sqrt(3) / 2);
        final int y2 = (int) (tl[1]);
        final int x3 = (int) (tl[0] + w * Math.sqrt(3));
        final int y3 = (int) (tl[1] + w / 2);
        final int x4 = (int) (tl[0] + w * Math.sqrt(3));
        final int y4 = (int) (tl[1] + w * 3 / 2);
        final int x5 = (int) (tl[0] + w * Math.sqrt(3) / 2);
        final int y5 = (int) (tl[1] + w * 2);
        final int x6 = (int) (tl[0]);
        final int y6 = (int) (tl[1] + w * 3 / 2);
        // { { x1, y1 }, { x2, y2 }, { x3, y3 }, { x4, y4 }, { x5, y5 }, { x6, y6 } };
        final int[][] r = { { x1, x2, x3, x4, x5, x6 }, { y1, y2, y3, y4, y5, y6 } };
        return r;
    }

    private Color getPlayerColor(final Player player) {
        return switch (player) {
            case RED -> Colour.P1;
            case BLUE -> Colour.P2;
            default -> Colour.bg;
        };
    }

    /**
     * V grafičnem kontekstu g2 pobarva polje (i,j)
     * 
     * @param g2
     * @param i
     * @param j
     * @param player
     */
    private void paintHex(final Graphics2D g2, final int i, final int j, final Player player) {
        final double w = sideLength();

        final int[][] p = extremalPoints(i, j);

        g2.setColor(getPlayerColor(player));
        g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
        g2.fillPolygon(p[0], p[1], 6);
    }

    private void outlineHex(final Graphics2D g2, final int i, final int j) {
        final int[][] p = extremalPoints(i, j);
        final double w = sideLength();

        g2.setColor(Colour.fg);
        g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
        g2.drawPolygon(p[0], p[1], 6);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        if (Runner.igra != null) {
            super.paintComponent(g);
            final Graphics2D g2 = (Graphics2D) g;

            for (int i = 0; i < Igra.size; i++) {
                for (int j = 0; j < Igra.size; j++) {
                    paintHex(g2, i, j, Runner.igra.getHexColor(i, j));
                    outlineHex(g2, i, j);
                }
            }
        }
    }

    public boolean checkTile(final int i, final int j, final int x, final int y) {
        final int[][] p = extremalPoints(i, j);
        final var hex = new Polygon(p[0], p[1], 6);
        return hex.contains(x, y);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        if (Runner.igra != null && Runner.igra.status == Igra.Status.IN_PROGRESS
                && Runner.currentPlayerType() == Player.Type.HUMAN) {
            final int x = e.getX();
            final int y = e.getY();
            for (int i = 0; i < Igra.size; i++) {
                for (int j = 0; j < Igra.size; j++) {
                    if (checkTile(i, j, x, y)) {
                        Runner.playHuman(i, j);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        // empty
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        // empty
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        // empty
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        // empty
    }
}