package gui;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Okno extends JFrame {

    private Platno canvas;

    public Okno(String name, Platno canvas) {
        this.setTitle(name);
        this.canvas = canvas;
        this.add(canvas, BorderLayout.CENTER);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
    }
}