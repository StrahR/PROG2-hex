package gui;

import javax.swing.*;

import logika.Igra;
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
    private final JDialog s_pane;
    private final JButton s_save;
    private final JButton s_p1_colour_button;
    private final JButton s_p2_colour_button;
    private final JTextField s_p1_name;
    private final JTextField s_p2_name;

    private final JButton s_fg_colour_button;
    private final JButton s_bg_colour_button;
    private final JButton s_accent_colour_button;

    private final JComboBox<String> s_ai1_algo;
    private final JComboBox<String> s_ai2_algo;

    final JSpinner s_minimax_depth;
    final JSpinner s_negamax_depth;
    final JSpinner s_mtdf_depth;
    final JSpinner s_mtdf_f;
    final JSpinner s_mcts_time;

    private Color t_p1_colour;
    private Color t_p2_colour;
    private Color t_fg_colour;
    private Color t_bg_colour;
    private Color t_accent_colour;

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

        status = new JLabel("Izberite igro!");
        status.setFont(new Font(Font.SANS_SERIF, 0b01, 20));
        final GridBagConstraints status_layout = new GridBagConstraints();
        status_layout.gridx = 0;
        status_layout.gridy = 1;
        status_layout.anchor = GridBagConstraints.CENTER;
        getContentPane().add(status, status_layout);

        undo = new JButton("Razveljavi");
        final GridBagConstraints undo_layout = new GridBagConstraints();
        undo_layout.gridx = 0;
        undo_layout.gridy = 1;
        undo_layout.anchor = GridBagConstraints.EAST;
        getContentPane().add(undo, undo_layout);
        undo.addActionListener(this);

        // Okno z nastavitvami
        s_pane = new JDialog(this, "Nastavitve", /* modal */ true);
        s_pane.setLayout(new GridBagLayout());

        // Nastavitve imena in barve 1. igralca
        final JLabel s_p1_name_label = new JLabel("P1:");
        final GridBagConstraints s_p1_name_label_layout = new GridBagConstraints();
        s_p1_name_label_layout.gridx = 0;
        s_p1_name_label_layout.gridy = 0;
        s_p1_name_label_layout.anchor = GridBagConstraints.WEST;
        s_pane.add(s_p1_name_label, s_p1_name_label_layout);

        s_p1_name = new JTextField();
        s_p1_name.setColumns(15);
        final GridBagConstraints s_p1_name_layout = new GridBagConstraints();
        s_p1_name_layout.gridx = 1;
        s_p1_name_layout.gridy = 0;
        s_p1_name_layout.anchor = GridBagConstraints.WEST;
        s_pane.add(s_p1_name, s_p1_name_layout);

        s_p1_colour_button = new JButton("Barva");
        final GridBagConstraints s_p1_colour_button_layout = new GridBagConstraints();
        s_p1_colour_button_layout.gridx = 2;
        s_p1_colour_button_layout.gridy = 0;
        s_p1_colour_button_layout.anchor = GridBagConstraints.WEST;
        s_pane.add(s_p1_colour_button, s_p1_colour_button_layout);
        s_p1_colour_button.addActionListener(this);

        // Nastavitve imena in barve 2. igralca
        final JLabel s_p2_name_label = new JLabel("P2:");
        final GridBagConstraints s_p2_name_label_layout = new GridBagConstraints();
        s_p2_name_label_layout.gridx = 0;
        s_p2_name_label_layout.gridy = 1;
        s_p2_name_label_layout.anchor = GridBagConstraints.WEST;
        s_pane.add(s_p2_name_label, s_p2_name_label_layout);

        s_p2_name = new JTextField();
        s_p2_name.setColumns(15);
        final GridBagConstraints s_p2_name_layout = new GridBagConstraints();
        s_p2_name_layout.gridx = 1;
        s_p2_name_layout.gridy = 1;
        s_p2_name_layout.anchor = GridBagConstraints.WEST;
        s_pane.add(s_p2_name, s_p2_name_layout);

        s_p2_colour_button = new JButton("Barva");
        final GridBagConstraints s_p2_colour_button_layout = new GridBagConstraints();
        s_p2_colour_button_layout.gridx = 2;
        s_p2_colour_button_layout.gridy = 1;
        s_p2_colour_button_layout.anchor = GridBagConstraints.WEST;
        s_pane.add(s_p2_colour_button, s_p2_colour_button_layout);
        s_p2_colour_button.addActionListener(this);

        // Nastavitve barv igre
        s_fg_colour_button = new JButton("Glavna barva");
        final GridBagConstraints s_fg_colour_button_layout = new GridBagConstraints();
        s_fg_colour_button_layout.gridx = 0;
        s_fg_colour_button_layout.gridy = 2;
        s_fg_colour_button_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_fg_colour_button, s_fg_colour_button_layout);
        s_fg_colour_button.addActionListener(this);
        s_bg_colour_button = new JButton("Barva ozadja");
        final GridBagConstraints s_bg_colour_button_layout = new GridBagConstraints();
        s_bg_colour_button_layout.gridx = 1;
        s_bg_colour_button_layout.gridy = 2;
        s_bg_colour_button_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_bg_colour_button, s_bg_colour_button_layout);
        s_bg_colour_button.addActionListener(this);
        s_accent_colour_button = new JButton("Barva poudarek");
        final GridBagConstraints s_accent_colour_button_layout = new GridBagConstraints();
        s_accent_colour_button_layout.gridx = 2;
        s_accent_colour_button_layout.gridy = 2;
        s_accent_colour_button_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_accent_colour_button, s_accent_colour_button_layout);
        s_accent_colour_button.addActionListener(this);

        // Izbira algoritma za AI igralca
        final String[] ai_list = { "Naiven", "Minimax", "Negamax", "MTD-f", "MCTS" };
        // 1. AI igralec
        final JLabel s_ai1_algo_label = new JLabel("AI1:");
        final GridBagConstraints s_ai1_algo_label_layout = new GridBagConstraints();
        s_ai1_algo_label_layout.gridx = 0;
        s_ai1_algo_label_layout.gridy = 3;
        s_ai1_algo_label_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_ai1_algo_label, s_ai1_algo_label_layout);

        s_ai1_algo = new JComboBox<String>(ai_list);
        final GridBagConstraints s_ai1_algo_layout = new GridBagConstraints();
        s_ai1_algo_layout.gridx = 1;
        s_ai1_algo_layout.gridy = 3;
        s_ai1_algo_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_ai1_algo, s_ai1_algo_layout);

        // 2 AI igralec
        final JLabel s_ai2_algo_label = new JLabel("AI2:");
        final GridBagConstraints s_ai2_algo_label_layout = new GridBagConstraints();
        s_ai2_algo_label_layout.gridx = 0;
        s_ai2_algo_label_layout.gridy = 4;
        s_ai2_algo_label_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_ai2_algo_label, s_ai2_algo_label_layout);

        s_ai2_algo = new JComboBox<String>(ai_list);
        final GridBagConstraints s_ai2_algo_layout = new GridBagConstraints();
        s_ai2_algo_layout.gridx = 1;
        s_ai2_algo_layout.gridy = 4;
        s_ai2_algo_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_ai2_algo, s_ai2_algo_layout);

        // Parametri za Minimax algoritem
        final JLabel s_minimax_depth_label = new JLabel("Minimax globina:");
        final GridBagConstraints s_minimax_depth_label_layout = new GridBagConstraints();
        s_minimax_depth_label_layout.gridx = 0;
        s_minimax_depth_label_layout.gridy = 5;
        s_minimax_depth_label_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_minimax_depth_label, s_minimax_depth_label_layout);

        s_minimax_depth = new JSpinner(new SpinnerNumberModel(Runner.minimax_depth, 1, 6, 1));
        final GridBagConstraints s_minimax_depth_layout = new GridBagConstraints();
        s_minimax_depth_layout.gridx = 1;
        s_minimax_depth_layout.gridy = 5;
        s_minimax_depth_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_minimax_depth, s_minimax_depth_layout);
        s_minimax_depth.setValue(Runner.minimax_depth);

        // Parametri za Negamax algoritem
        final JLabel s_negamax_depth_label = new JLabel("Negamax globina:");
        final GridBagConstraints s_negamax_depth_label_layout = new GridBagConstraints();
        s_negamax_depth_label_layout.gridx = 0;
        s_negamax_depth_label_layout.gridy = 6;
        s_negamax_depth_label_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_negamax_depth_label, s_negamax_depth_label_layout);

        s_negamax_depth = new JSpinner(new SpinnerNumberModel(Runner.negamax_depth, 1, 8, 1));
        final GridBagConstraints s_negamax_depth_layout = new GridBagConstraints();
        s_negamax_depth_layout.gridx = 1;
        s_negamax_depth_layout.gridy = 6;
        s_negamax_depth_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_negamax_depth, s_negamax_depth_layout);
        s_negamax_depth.setValue(Runner.negamax_depth);

        // Parametri za MTD-f algoritem
        final JLabel s_mtdf_depth_label = new JLabel("MTD-f globina in f:");
        final GridBagConstraints s_mtdf_depth_label_layout = new GridBagConstraints();
        s_mtdf_depth_label_layout.gridx = 0;
        s_mtdf_depth_label_layout.gridy = 7;
        s_mtdf_depth_label_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_mtdf_depth_label, s_mtdf_depth_label_layout);

        s_mtdf_depth = new JSpinner(new SpinnerNumberModel(Runner.mtdf_depth, 2, 10, 2));
        final GridBagConstraints s_mtdf_depth_layout = new GridBagConstraints();
        s_mtdf_depth_layout.gridx = 1;
        s_mtdf_depth_layout.gridy = 7;
        s_mtdf_depth_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_mtdf_depth, s_mtdf_depth_layout);
        s_mtdf_depth.setValue(Runner.mtdf_depth);
        s_mtdf_f = new JSpinner(new SpinnerNumberModel(Runner.mtdf_f, -Igra.size, Igra.size, 1));
        final GridBagConstraints s_mtdf_f_layout = new GridBagConstraints();
        s_mtdf_f_layout.gridx = 2;
        s_mtdf_f_layout.gridy = 7;
        s_mtdf_f_layout.anchor = GridBagConstraints.WEST;
        s_pane.add(s_mtdf_f, s_mtdf_f_layout);
        s_mtdf_f.setValue(Runner.mtdf_f);

        // // Parametri za MCTS algoritem
        final JLabel s_mcts_depth_label = new JLabel("MCTS timeout [ms]:");
        final GridBagConstraints s_mcts_depth_label_layout = new GridBagConstraints();
        s_mcts_depth_label_layout.gridx = 0;
        s_mcts_depth_label_layout.gridy = 8;
        s_mcts_depth_label_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_mcts_depth_label, s_mcts_depth_label_layout);

        s_mcts_time = new JSpinner(new SpinnerNumberModel(Runner.mcts_time_ms, 100, 10000, 100));
        final GridBagConstraints s_mcts_time_layout = new GridBagConstraints();
        s_mcts_time_layout.gridx = 1;
        s_mcts_time_layout.gridy = 8;
        s_mcts_time_layout.anchor = GridBagConstraints.CENTER;
        s_pane.add(s_mcts_time, s_mcts_time_layout);

        // Gumb za shranjevanje
        s_save = new JButton("Shrani");
        final GridBagConstraints s_save_layout = new GridBagConstraints();
        s_save_layout.gridx = 0;
        s_save_layout.gridy = 50;
        s_save_layout.anchor = GridBagConstraints.PAGE_END;
        s_pane.add(s_save, s_save_layout);
        s_save.addActionListener(this);

        s_pane.setSize(500, 500);

        refreshGUI();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == game_HUMAN_AI) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.HUMAN);
            Runner.playerType.put(Player.BLUE, Player.Type.AI);
            Runner.newGame();
        } else if (e.getSource() == game_AI_HUMAN) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.AI);
            Runner.playerType.put(Player.BLUE, Player.Type.HUMAN);
            Runner.newGame();
        } else if (e.getSource() == game_HUMAN_HUMAN) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.HUMAN);
            Runner.playerType.put(Player.BLUE, Player.Type.HUMAN);
            Runner.newGame();
        } else if (e.getSource() == game_AI_AI) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.AI);
            Runner.playerType.put(Player.BLUE, Player.Type.AI);
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
            t_p1_colour = null;
            t_p2_colour = null;
            t_fg_colour = null;
            t_bg_colour = null;
            t_accent_colour = null;

            s_p1_name.setText(Runner.playerName.get(Player.RED));
            s_p2_name.setText(Runner.playerName.get(Player.BLUE));

            s_ai1_algo.setSelectedItem(Runner.aiAlgorithm.get(Player.RED));
            s_ai2_algo.setSelectedItem(Runner.aiAlgorithm.get(Player.BLUE));

            s_minimax_depth.setValue(Runner.minimax_depth);
            s_negamax_depth.setValue(Runner.negamax_depth);
            s_mtdf_depth.setValue(Runner.mtdf_depth);
            s_mtdf_f.setValue(Runner.mtdf_f);
            s_mcts_time.setValue(Runner.mcts_time_ms);

            s_pane.setVisible(true);
        } else if (e.getSource() == s_save) {
            if (t_p1_colour != null) {
                Runner.playerColour.put(Player.RED, t_p1_colour);
            }
            if (t_p2_colour != null) {
                Runner.playerColour.put(Player.BLUE, t_p2_colour);
            }
            if (t_fg_colour != null) {
                Platno.Colour.fg = t_fg_colour;
            }
            if (t_bg_colour != null) {
                Platno.Colour.bg = t_bg_colour;
            }
            if (t_accent_colour != null) {
                Platno.Colour.accent = t_accent_colour;
            }

            if (!s_p1_name.getText().isEmpty()) {
                Runner.playerName.put(Player.RED, s_p1_name.getText());
            }
            if (!s_p2_name.getText().isEmpty()) {
                Runner.playerName.put(Player.BLUE, s_p2_name.getText());
            }

            // check if ai algo was modified and restart game if it was
            if (!Runner.aiAlgorithm.get(Player.RED).equals(s_ai1_algo.getSelectedItem().toString())
                    || !Runner.aiAlgorithm.get(Player.BLUE).equals(s_ai2_algo.getSelectedItem().toString())) {
                Runner.aiAlgorithm.put(Player.RED, s_ai1_algo.getSelectedItem().toString());
                Runner.aiAlgorithm.put(Player.BLUE, s_ai2_algo.getSelectedItem().toString());
                if (Runner.igra != null) {
                    Runner.newGame();
                }
            }

            // set the AI parameters, can be adjusted on the fly
            Runner.minimax_depth = (int) s_minimax_depth.getValue();
            Runner.negamax_depth = (int) s_negamax_depth.getValue();
            Runner.mtdf_depth = (int) s_mtdf_depth.getValue();
            Runner.mtdf_f = (int) s_mtdf_f.getValue();
            Runner.mcts_time_ms = (int) s_mcts_time.getValue();

            s_pane.dispose();
            refreshGUI();
            // System.out.println(s_p1_name.getText());
        } else if (e.getSource() == s_p1_colour_button) {
            Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", Runner.playerColour.get(Player.RED));
            if (c != null) {
                t_p1_colour = c;
            }
        } else if (e.getSource() == s_p2_colour_button) {
            Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", Runner.playerColour.get(Player.BLUE));
            if (c != null) {
                t_p2_colour = c;
            }
        } else if (e.getSource() == s_fg_colour_button) {
            Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", Platno.Colour.fg);
            if (c != null) {
                t_fg_colour = c;
            }
        } else if (e.getSource() == s_bg_colour_button) {
            Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", Platno.Colour.bg);
            if (c != null) {
                t_bg_colour = c;
            }
        } else if (e.getSource() == s_accent_colour_button) {
            Color c = JColorChooser.showDialog(s_pane, "Izberi barvo", Platno.Colour.accent);
            if (c != null) {
                t_accent_colour = c;
            }
        }
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