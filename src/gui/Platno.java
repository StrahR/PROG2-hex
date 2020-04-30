package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Platno extends JPanel implements MouseListener {
    public Platno() {
        setBackground(Color.WHITE);
        this.addMouseListener(this);
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