package runner;

import java.util.EnumMap;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import gui.Okno;
import logika.Igra;
import logika.Player;
import splosno.Koordinati;
import inteligenca.*;

public class Runner {
    public static EnumMap<Player, Player.Type> playerType;
    public static EnumMap<Player, String> playerName;

    public static Okno okno;
    public static Igra igra;

    public static Player.Type currentPlayerType() {
        return playerType.get(igra.onTurn);
    }

    public static void newGame() {
        igra = new Igra(5);
        play();
    }

    public static void play() {
        okno.refreshGUI();
        if (igra.status == Igra.Status.IN_PROGRESS) {
            if (currentPlayerType() == Player.Type.AI) {
                SwingWorker<Koordinati, Void> worker = new SwingWorker<Koordinati, Void>() {
                    @Override
                    protected Koordinati doInBackground() {
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (Exception e) {
                        }
                        return MTDF.play(igra);
                    }

                    @Override
                    protected void done() {
                        Koordinati poteza = null;
                        try {
                            poteza = get();
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                        igra.odigraj(poteza);
                        play();
                    }
                };
                worker.execute();
            }
        }
    }

    public static void playHuman(final int i, final int j) {
        igra.odigraj(new Koordinati(i, j));
        play();
    }

    public static void undo() {
        igra.razveljavi();
        play();
    }
}