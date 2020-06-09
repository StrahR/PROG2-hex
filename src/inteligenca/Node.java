package inteligenca;

import logika.Igra;
import logika.Player;
import splosno.Koordinati;

import java.util.HashSet;
import java.util.Set;

public class Node {
    public Igra igra;
    public Node parent;
    public Set<Node> children = new HashSet<Node>();
    public double value = 0;
    public int visits = 0;
    public Player player;
    public Koordinati move;
    private static double UCB_factor = 2;

    public Node(Igra igra, Node parent, Player player, Koordinati move) {
        this.igra = igra;
        this.parent = parent;
        this.player = player;
        this.move = move;
    }

    public void update_value(double outcome, Player player) {
        value += outcome;
        visits += 1;
    }

    /**
     * Vrne izračunano vrednost po UCB formuli
     */
    public double UCB_score(Player player) {
        return value / (1e-6 + visits) + Math.sqrt(UCB_factor * Math.log(1 + parent.visits) / (1e-6 + visits));
    }
}