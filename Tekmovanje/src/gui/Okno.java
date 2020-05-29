package gui;

import javax.swing.*;

import logika.Player;
import runner.Runner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {

    private final Platno canvas;
    public final JLabel status;
    private final JButton undo;

    private final JMenuItem game_HUMAN_AI;
    private final JMenuItem game_AI_HUMAN;
    private final JMenuItem game_HUMAN_HUMAN;
    private final JMenuItem game_AI_AI;

    private final JMenuItem size_05;
    private final JMenuItem size_07;
    private final JMenuItem size_09;
    private final JMenuItem size_11;
    private final JMenuItem size_13;
    private final JMenuItem size_15;

    private final JMenuItem settings;

    public Okno(final String name, final Platno canvas) {
        this.setTitle(name);
        this.canvas = canvas;
        this.add(this.canvas, BorderLayout.CENTER);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();

        final JMenuBar menu_bar = new JMenuBar();
        this.setJMenuBar(menu_bar);
        final JMenu igra_menu = new JMenu("Nova igra");
        menu_bar.add(igra_menu);
        final JMenu velikost_menu = new JMenu("Velikost igre");
        menu_bar.add(velikost_menu);
        final JMenu nastavitve_menu = new JMenu("Nastavitve");
        menu_bar.add(nastavitve_menu);

        game_HUMAN_AI = new JMenuItem("Človek – računalnik");
        igra_menu.add(game_HUMAN_AI);
        game_HUMAN_AI.addActionListener(this);

        game_AI_HUMAN = new JMenuItem("Računalnik – človek");
        igra_menu.add(game_AI_HUMAN);
        game_AI_HUMAN.addActionListener(this);

        game_HUMAN_HUMAN = new JMenuItem("Človek – človek");
        igra_menu.add(game_HUMAN_HUMAN);
        game_HUMAN_HUMAN.addActionListener(this);

        game_AI_AI = new JMenuItem("Računalnik – računalnik");
        igra_menu.add(game_AI_AI);
        game_AI_AI.addActionListener(this);

        size_05 = new JMenuItem("5x5");
        velikost_menu.add(size_05);
        size_05.addActionListener(this);

        size_07 = new JMenuItem("7x7");
        velikost_menu.add(size_07);
        size_07.addActionListener(this);

        size_09 = new JMenuItem("9x9");
        velikost_menu.add(size_09);
        size_09.addActionListener(this);

        size_11 = new JMenuItem("11x11");
        velikost_menu.add(size_11);
        size_11.addActionListener(this);

        size_13 = new JMenuItem("13x13");
        velikost_menu.add(size_13);
        size_13.addActionListener(this);

        size_15 = new JMenuItem("15x15");
        velikost_menu.add(size_15);
        size_15.addActionListener(this);

        settings = new JMenuItem("Nastavitve");
        nastavitve_menu.add(settings);
        settings.addActionListener(this);

        this.setLayout(new GridBagLayout());

        final GridBagConstraints canvas_layout = new GridBagConstraints();
        canvas_layout.gridx = 0;
        canvas_layout.gridy = 0;
        canvas_layout.fill = GridBagConstraints.BOTH;
        canvas_layout.weightx = 1.0;
        canvas_layout.weighty = 1.0;
        getContentPane().add(canvas, canvas_layout);

        status = new JLabel();
        status.setFont(new Font(Font.SANS_SERIF, 0b01, 20));
        final GridBagConstraints status_layout = new GridBagConstraints();
        status_layout.gridx = 0;
        status_layout.gridy = 1;
        status_layout.anchor = GridBagConstraints.CENTER;
        getContentPane().add(status, status_layout);
        status.setText("Izberite igro!");

        undo = new JButton();
        final GridBagConstraints undo_layout = new GridBagConstraints();
        undo_layout.gridx = 0;
        undo_layout.gridy = 1;
        undo_layout.anchor = GridBagConstraints.EAST;
        getContentPane().add(undo, undo_layout);
        undo.setText("Razveljavi");
        undo.addActionListener(this);

    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == game_HUMAN_AI) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.HUMAN);
            Runner.playerType.put(Player.BLUE, Player.Type.AI);
            Runner.playerName = new EnumMap<Player, String>(Player.class);
            Runner.playerName.put(Player.RED, "Jože");
            Runner.playerName.put(Player.BLUE, "Štefan");
            Runner.newGame();
        } else if (e.getSource() == game_AI_HUMAN) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.AI);
            Runner.playerType.put(Player.BLUE, Player.Type.HUMAN);
            Runner.playerName = new EnumMap<Player, String>(Player.class);
            Runner.playerName.put(Player.RED, "Jaka");
            Runner.playerName.put(Player.BLUE, "Alfonz");
            Runner.newGame();
        } else if (e.getSource() == game_HUMAN_HUMAN) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.HUMAN);
            Runner.playerType.put(Player.BLUE, Player.Type.HUMAN);
            Runner.playerName = new EnumMap<Player, String>(Player.class);
            Runner.playerName.put(Player.RED, "Ludvik");
            Runner.playerName.put(Player.BLUE, "Špela");
            Runner.newGame();
        } else if (e.getSource() == game_AI_AI) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.AI);
            Runner.playerType.put(Player.BLUE, Player.Type.AI);
            Runner.playerName = new EnumMap<Player, String>(Player.class);
            Runner.playerName.put(Player.RED, "Karl");
            Runner.playerName.put(Player.BLUE, "Elizabeta");
            Runner.newGame();
        } else if (e.getSource() == undo) {
            if (Runner.igra != null) {
                Runner.undo();
            }
        } else if (e.getSource() == size_05) {
            Runner.size = 5;
            if (Runner.igra != null) {
                Runner.newGame();
            }
        } else if (e.getSource() == size_07) {
            Runner.size = 7;
            if (Runner.igra != null) {
                Runner.newGame();
            }
        } else if (e.getSource() == size_09) {
            Runner.size = 9;
            if (Runner.igra != null) {
                Runner.newGame();
            }
        } else if (e.getSource() == size_11) {
            Runner.size = 11;
            if (Runner.igra != null) {
                Runner.newGame();
            }
        } else if (e.getSource() == size_13) {
            Runner.size = 13;
            if (Runner.igra != null) {
                Runner.newGame();
            }
        } else if (e.getSource() == size_15) {
            Runner.size = 15;
            if (Runner.igra != null) {
                Runner.newGame();
            }
        } else if (e.getSource() == settings) {
            displaySettings();
        }
    }

    private void displaySettings() {
    }

    public void refreshGUI() {
        if (Runner.igra == null) {
            status.setText("Igra ni v teku.");
        } else {
            switch (Runner.igra.status) {
                case IN_PROGRESS -> status.setText("Na potezi je " + Runner.playerName.get(Runner.igra.onTurn) + " - "
                        + Runner.playerType.get(Runner.igra.onTurn));
                case WIN -> status.setText("Bravo " + Runner.playerName.get(Runner.igra.onTurn.opponent()) + " - "
                        + Runner.playerType.get(Runner.igra.onTurn.opponent()));
            }
        }
        canvas.repaint();
    }
}