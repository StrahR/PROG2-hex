package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import logika.Igra;
import logika.Player;
import runner.Runner;
import splosno.Koordinati;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener {

    public static class Colour {
        public static Color bg = Color.WHITE;
        public static Color fg = Color.BLACK;
        public static Color P1 = Color.RED;
        public static Color P2 = Color.BLUE;
        public static Color accent = Color.YELLOW;
    }

    private final static double LINE_WIDTH = 0.16;

    public Platno() {
        setBackground(Colour.bg);
        this.addMouseListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(560, 560);
    }

    private double sideLength() {
        return (1 - LINE_WIDTH) * Math.min(2 * getWidth() / (Math.sqrt(3) * (3 * Igra.size - 1)),
                2 * getHeight() / (3 * Igra.size + 1.0));
    }

    private double[] topLeft(final int i, final int j) {
        final double w = sideLength();
        final int cx = getWidth() / 2;
        final int cy = getHeight() / 2;
        final int x = (int) (Igra.size * w * Math.sqrt(3) / 2);
        final int y = (int) ((Igra.size * w * 1.5 + 0.5 * w) / 2);
        final int o = (int) (((Igra.size - 1) / 2 * sideLength() * Math.sqrt(3) / 2));
        final double[] r = { cx - x - o + w * (i + j / 2.0) * Math.sqrt(3), cy - y + w * j * 3 / 2.0 };
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
        outlineHex(g2, i, j, Colour.fg);
    }

    private void outlineHex(final Graphics2D g2, final int i, final int j, final Color colour) {
        final int[][] p = extremalPoints(i, j);
        final double w = sideLength();

        g2.setColor(colour);
        g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
        g2.drawPolygon(p[0], p[1], 6);
    }

    private void paintBG(final Graphics2D g2, final int cx, final int cy) {
        g2.setBackground(Colour.bg);
        g2.clearRect(0, 0, getWidth(), getHeight());

        final int x = (int) (Igra.size * sideLength() * Math.sqrt(3) / 2);
        final int y = (int) ((Igra.size * sideLength() * 1.5 + 0.5 * sideLength()) / 2 + LINE_WIDTH * sideLength());
        final int o = (int) ((Igra.size - 1) / 2 * sideLength() * Math.sqrt(3) / 2);
        final int o2 = (int) (sideLength() * Math.sqrt(3) / 6 - LINE_WIDTH * sideLength());
        final int o3 = (int) (sideLength() * Math.sqrt(5) / 2);

        final int[] xs = { cx - x - o - o3, cx - x + o + o2, cx + x + o + o3, cx + x - o - o2 };
        final int[] ys = { cy - y, cy + y, cy + y, cy - y };

        final int[][] p0 = { { xs[0], xs[1], cx }, { ys[0], ys[1], cy } };
        final int[][] p1 = { { xs[1], xs[2], cx }, { ys[1], ys[2], cy } };
        final int[][] p2 = { { xs[2], xs[3], cx }, { ys[2], ys[3], cy } };
        final int[][] p3 = { { xs[3], xs[0], cx }, { ys[3], ys[0], cy } };

        g2.setColor(Colour.P2);
        g2.fillPolygon(p0[0], p0[1], 3);
        g2.fillPolygon(p2[0], p2[1], 3);

        g2.setColor(Colour.P1);
        g2.fillPolygon(p1[0], p1[1], 3);
        g2.fillPolygon(p3[0], p3[1], 3);

        g2.setStroke(new BasicStroke((float) (sideLength() * LINE_WIDTH)));
        g2.setColor(Colour.bg);
        g2.drawLine(xs[0], ys[0], xs[2], ys[2]);
        g2.drawLine(xs[1], ys[1], xs[3], ys[3]);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        if (Runner.igra != null) {
            super.paintComponent(g);
            final Graphics2D g2 = (Graphics2D) g;

            Colour.P1 = Runner.playerColour.get(Player.RED);
            Colour.P2 = Runner.playerColour.get(Player.BLUE);

            final int cx = getWidth() / 2;
            final int cy = getHeight() / 2;

            paintBG(g2, cx, cy);

            for (int i = 0; i < Igra.size; i++) {
                for (int j = 0; j < Igra.size; j++) {
                    paintHex(g2, i, j, Runner.igra.getHexColor(i, j));
                    outlineHex(g2, i, j);
                }
            }
            Koordinati last_move = Runner.igra.getLastMove();
            if (last_move != null) {
                outlineHex(g2, last_move.getX(), last_move.getY(), Colour.accent);
            }
            // g2.rotate(Math.toRadians(30));
            var t = new AffineTransform(1, 0, 0, 1, 0, 0);
            t.rotate(Math.toRadians(90));
            g2.setTransform(t);
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