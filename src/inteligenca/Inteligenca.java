package inteligenca;

import logika.Igra;
import splosno.KdoIgra;
import splosno.Koordinati;

public class Inteligenca extends KdoIgra {

    private MCTS tree;

    public Inteligenca() {
        super("Hungry Seas - Jakob Zmrzlikar in Rok Strah");
        tree = new MCTS();
    }

    public Koordinati izberiPotezo(Igra igra) {
        return tree.play(igra);
    }
}