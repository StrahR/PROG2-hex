package runner;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import gui.Okno;
import logika.Igra;
import logika.Player;
import splosno.Koordinati;
import inteligenca.*;

import java.awt.Color;

public class Runner {
    public static EnumMap<Player, Player.Type> playerType;
    public static EnumMap<Player, String> playerName;
    public static EnumMap<Player, Color> playerColour;
    public static EnumMap<Player, String> aiAlgorithm;

    public static Okno okno;
    public static Igra igra = null;
    public static int size = 5;
    public static MCTS tree = null;

    public static int minimax_depth = 2;
    public static int negamax_depth = 4;
    public static int mtdf_depth = 6;
    public static int mtdf_f = 0;
    public static int mcts_time_ms = 3000;

    public static ArrayList<Koordinati> winning_path = null;

    private static SwingWorker<Koordinati, Void> worker = null;

    static {
        playerName = new EnumMap<Player, String>(Player.class);
        playerName.put(Player.RED, "Ludvik");
        playerName.put(Player.BLUE, "Špela");

        playerColour = new EnumMap<Player, Color>(Player.class);
        playerColour.put(Player.RED, Color.RED);
        playerColour.put(Player.BLUE, Color.BLUE);

        aiAlgorithm = new EnumMap<Player, String>(Player.class);
        aiAlgorithm.put(Player.RED, "Negamax");
        aiAlgorithm.put(Player.BLUE, "MCTS");
    }

    public static Player.Type currentPlayerType() {
        return playerType.get(igra.onTurn);
    }

    public static void newGame() {
        igra = new Igra(size);
        tree = new MCTS();
        play();
    }

    public static void play() {
        okno.refreshGUI();
        if (igra.status == Igra.Status.IN_PROGRESS && currentPlayerType() == Player.Type.AI) {
            worker = new SwingWorker<Koordinati, Void>() {
                @Override
                protected Koordinati doInBackground() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (Exception e) {
                    }

                    return switch (aiAlgorithm.get(igra.onTurn)) {
                        case "MCTS" -> tree.play(igra);
                        case "MTD-f" -> MTDF.play(igra);
                        case "Negamax" -> Negamax.play(igra);
                        case "Minimax" -> Minimax.play(igra);
                        default -> Naive.play(igra);
                    };
                }

                @Override
                protected void done() {
                    Koordinati poteza = null;
                    try {
                        poteza = get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    igra.odigraj(poteza);
                    play();
                }
            };
            worker.execute();
        }
    }

    public static void playHuman(final int i, final int j) {
        igra.odigraj(new Koordinati(i, j));
        play();
    }

    public static void undo() {
        if (worker != null) {
            worker.cancel(false);
        }
        igra.razveljavi();
        play();
    }
}