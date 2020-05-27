package inteligenca;

import logika.Igra;
import logika.Player;

import java.util.HashSet;
import java.util.Set;

public class Node {
    public Igra igra;
    public Node parent;
    public Set<Node> children = new HashSet<Node>();
    public double prior_probability;
    public double value;
    public int visits;
    private static double UCB_factor;

    public Node(Igra igra, Node parent, double prior_probability) {
        this.igra = igra;
        this.parent = parent;
        this.prior_probability = prior_probability;
        visits = 0;
        UCB_factor = 2;
    }

    public void update_value(double outcome) {
        value = (visits * value + outcome) / (visits + 1);
        visits += 1;
    }

    /**
     * UCB formula za score
     */
    public double UCB_score(int parent_visits, Player player) {
        if (player != igra.onTurn) {
            return (1 - value) + UCB_factor * Math.sqrt(parent_visits) / (1 + visits);
        }
        return value + UCB_factor * Math.sqrt(parent_visits) / (1 + visits);
    }

}