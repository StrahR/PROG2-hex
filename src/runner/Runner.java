package runner;

import java.util.EnumMap;

import gui.Okno;
import logika.Igra;
import logika.Player;
import splosno.Koordinati;

public class Runner {
    public static EnumMap<Player, Player.Type> playerType;
    public static EnumMap<Player, String> playerName;

    public static Okno okno;
    public static Igra igra;

    public static Player.Type currentPlayerType() {
        return playerType.get(Player.onTurn);
    }

    public static void newGame() {
        igra = new Igra();
        play();
    }

    public static void play() {
        okno.refreshGUI();
        switch (Igra.status) {
            case WIN:
            case TIE:
                return;
            case IN_PROGRESS:
                if (currentPlayerType() == Player.Type.AI) {
                    // TODO: add AI handling
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