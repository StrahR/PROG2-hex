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

    private final JMenuItem igraClovekRacunalnik;
    private final JMenuItem igraRacunalnikClovek;
    private final JMenuItem igraClovekClovek;
    private final JMenuItem igraRacunalnikRacunalnik;

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

        igraClovekRacunalnik = new JMenuItem("Človek – računalnik");
        igra_menu.add(igraClovekRacunalnik);
        igraClovekRacunalnik.addActionListener(this);

        igraRacunalnikClovek = new JMenuItem("Računalnik – človek");
        igra_menu.add(igraRacunalnikClovek);
        igraRacunalnikClovek.addActionListener(this);

        igraClovekClovek = new JMenuItem("Človek – človek");
        igra_menu.add(igraClovekClovek);
        igraClovekClovek.addActionListener(this);

        igraRacunalnikRacunalnik = new JMenuItem("Računalnik – računalnik");
        igra_menu.add(igraRacunalnikRacunalnik);
        igraRacunalnikRacunalnik.addActionListener(this);

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
        if (e.getSource() == igraClovekRacunalnik) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.HUMAN);
            Runner.playerType.put(Player.BLUE, Player.Type.AI);
            Runner.playerName = new EnumMap<Player, String>(Player.class);
            Runner.playerName.put(Player.RED, "Jože");
            Runner.playerName.put(Player.BLUE, "Štefan");
            Runner.newGame();
        } else if (e.getSource() == igraRacunalnikClovek) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.AI);
            Runner.playerType.put(Player.BLUE, Player.Type.HUMAN);
            Runner.playerName = new EnumMap<Player, String>(Player.class);
            Runner.playerName.put(Player.RED, "Jaka");
            Runner.playerName.put(Player.BLUE, "Alfonz");
            Runner.newGame();
        } else if (e.getSource() == igraClovekClovek) {
            Runner.playerType = new EnumMap<Player, Player.Type>(Player.class);
            Runner.playerType.put(Player.RED, Player.Type.HUMAN);
            Runner.playerType.put(Player.BLUE, Player.Type.HUMAN);
            Runner.playerName = new EnumMap<Player, String>(Player.class);
            Runner.playerName.put(Player.RED, "Ludvik");
            Runner.playerName.put(Player.BLUE, "Špela");
            Runner.newGame();
        } else if (e.getSource() == igraRacunalnikRacunalnik) {
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
        }
    }

    public void refreshGUI() {
        if (Runner.igra == null) {
            status.setText("Igra ni v teku.");
        } else {
            switch (Igra.status) {
                case IN_PROGRESS -> status.setText("Na potezi je " + Runner.playerName.get(Player.onTurn) + " - "
                        + Runner.playerType.get(Player.onTurn));
                case WIN -> status.setText("Bravo " + Runner.playerName.get(Player.onTurn.opponent()) + " - "
                        + Runner.playerType.get(Player.onTurn.opponent()));
            }
        }
        canvas.repaint();
    }
}