package inteligenca;

import logika.Igra;
import logika.Player;
import splosno.Koordinati;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class MCTS {
    final static int INF = Integer.MAX_VALUE;
    final static int TIME_LIMIT = 5000;

    private Player player;
    private Map<Igra, Node> visited_nodes = new HashMap<Igra, Node>();

    public MCTS(Player player) {
        this.player = player;
    }

    private Node selectFavouriteChild(Node parent) {
        Set<Node> children = parent.children;
        Node favorite_child = null;
        double max_score = -INF;
        for (Node child : children) {
            double v = child.UCB_score(parent.visits, player);
            if (v > max_score) {
                max_score = v;
                favorite_child = child;
            }
        }
        return favorite_child;
    }

    private Node expand(Node parent) {
        for (Koordinati move : parent.igra.possibleMoves()) {
            Igra igra = new Igra(parent.igra);
            igra.odigraj(move);
            Node child = new Node(igra, parent, parent.prior_probability);
            child.value = simulate(child);
            parent.children.add(child);
        }
        visited_nodes.put(parent.igra, parent);
        return parent;
    }

    private int simulate(Node child) {
        child.visits++;
        Igra igra = new Igra(child.igra);
        while (igra.status == Igra.Status.IN_PROGRESS) {
            Koordinati move = Naive.play(igra);
            igra.odigraj(move);
        }
        if (igra.status.winner == player) {
            return 1;
        }
        return -1;
    }

    private void backprop(Node selected, Node root, double outcome) {
        Node current = selected;
        while (current != root) {
            current.update_value(outcome);
            current = current.parent;
        }
        root.update_value(outcome);
    }

    private void search(Node root) {
        long start = System.currentTimeMillis();
        int j = 0;
        int k = 0;
        Node prev = null;
        while (System.currentTimeMillis() - start < TIME_LIMIT) {
            double outcome = 0;
            Node selected = root;
            Set<Koordinati> moves = selected.igra.possibleMoves();
            j++;
            // System.out.println(visited_nodes.size());
            // System.out.println(System.currentTimeMillis() - start);
            while (selected.children.size() > 0 && selected.igra.status == Igra.Status.IN_PROGRESS) {
                selected = selectFavouriteChild(selected);
                moves = selected.igra.possibleMoves();
            }
            switch (selected.igra.status) {
                case WIN:
                    if (prev == selected) {
                        k++;
                    }
                    prev = selected;
                    if (selected.igra.status.winner == player)
                        outcome = 1;
                    else
                        outcome = -1;
                    break;
                default: // case IN_PROGRESS:
                    selected = expand(selected);
                    int rand_int = new Random().nextInt(moves.size());
                    int i = 0;
                    for (Node child : selected.children) {
                        if (i == rand_int) {
                            outcome = simulate(child);
                            break;
                        }
                        i++;
                    }
            }
            backprop(selected, root, outcome);
        }
        System.out.println("Iterations: " + j);
        System.out.println("Same terminal: " + k);
    }

    public Koordinati play(Igra igra) {
        Node origin;
        if (visited_nodes.containsKey(igra)) {
            origin = visited_nodes.get(igra);
        } else {
            origin = new Node(igra, null, 0);
            origin.value = simulate(origin);
            origin = expand(origin);
        }
        search(origin);
        Koordinati move = selectFavouriteChild(origin).igra.getLastMove();
        return move;
    }
}