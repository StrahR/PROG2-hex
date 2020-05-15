package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import logika.Igra;
import logika.Player;

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

    private Color getPlayerColor(final Player player) {
        switch (player) {
            case RED:
                return Colour.P1;
            case BLUE:
                return Colour.P2;
            default:
                return Colour.bg;
        }
    }


    private void outlineHex(final Graphics2D g2, final double tlx, final double tly, final double w) {
        final double x1 = tlx;
        final double y1 = tly + w / 2;
        final double x2 = tlx + w * Math.sqrt(3) / 2;
        final double y2 = tly;
        final double x3 = tlx + w * Math.sqrt(3);
        final double y3 = tly + w / 2;
        final double x4 = tlx + w * Math.sqrt(3);
        final double y4 = tly + w * 3 / 2;
        final double x5 = tlx + w * Math.sqrt(3) / 2;
        final double y5 = tly + w * 2;
        final double x6 = tlx;
        final double y6 = tly + w * 3 / 2;

        g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        g2.drawLine((int) x2, (int) y2, (int) x3, (int) y3);
        g2.drawLine((int) x3, (int) y3, (int) x4, (int) y4);
        g2.drawLine((int) x4, (int) y4, (int) x5, (int) y5);
        g2.drawLine((int) x5, (int) y5, (int) x6, (int) y6);
        g2.drawLine((int) x6, (int) y6, (int) x1, (int) y1);
        }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        final double w = sideLength();

        // črte
        g2.setColor(Colour.fg);
        g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
        for (int i = 0; i < Igra.size; i++) {
            for (int j = 0; j < Igra.size; j++) {
                outlineHex(g2, w * (i + j / 2.0) * Math.sqrt(3), w * j * 3 / 2.0, w);
    }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO: react to mouse click
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // empty
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // empty
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // empty
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // empty
    }
}