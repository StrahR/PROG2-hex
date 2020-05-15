package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import logika.Igra;
import logika.Player;

public class Platno extends JPanel implements MouseListener {

    private static Color bg = Color.WHITE;
    private static Color fg = Color.BLACK;
    private static Color P1 = Color.RED;
    private static Color P2 = Color.BLUE;

    public Platno() {
        setBackground(bg);
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
        return Math.min(2 * getWidth() / (Math.sqrt(3) * (3 * Igra.size - 1)), 2 * getHeight() / (3 * Igra.size + 1));
    }

    private Color getPlayerColor(final Player player) {
        switch (player) {
            case RED:
                return P1;
            case BLUE:
                return P2;
            default:
                return bg;
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