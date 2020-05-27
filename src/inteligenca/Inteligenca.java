package inteligenca;

import logika.Igra;
import splosno.KdoIgra;
import splosno.Koordinati;

public class Inteligenca extends KdoIgra {
    public Inteligenca() {
        super("Hungry Seas - Jakob Zmrzlikar in Rok Strah");
    }

    public Koordinati izberiPotezo(Igra igra) {
        return Minimax.play(igra);
    }
}