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
    final static int TIME_LIMIT = 3000;

    private Player player;
    private Map<Igra, Node> visited_nodes = new HashMap<Igra, Node>();
    private Node previous_root = null;
    private int cleaned = 0;

    public MCTS() {
    }

    private Node selectFavouriteChild(Node parent) {
        Set<Node> children = parent.children;
        Node favorite_child = null;
        double max_score = -INF;
        for (Node child : children) {
            double v = child.UCB_score(player);
            if (v > max_score) {
                max_score = v;
                favorite_child = child;
            }
        }
        return favorite_child;
    }

    private void expand(Node parent) {
        for (Koordinati move : parent.igra.possibleMoves()) {
            Igra igra = new Igra(parent.igra);
            igra.odigraj(move);
            if (!visited_nodes.containsKey(igra)) {
                Node child = new Node(igra, parent, parent.player.opponent(), move);
                // child.value = simulate(child);
                parent.children.add(child);
                visited_nodes.put(child.igra, child);
            }
        }
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
        selected.update_value(outcome, player);
        if (selected == root) {
            return;
        }
        backprop(selected.parent, root, outcome);
    }

    private void search(Node root) {
        long start = System.currentTimeMillis();
        int j = 0;
        while (System.currentTimeMillis() - start < TIME_LIMIT) {
            j++;
            double outcome = 0;
            Node selected = root;
            while (selected.children.size() > 0 && selected.igra.status == Igra.Status.IN_PROGRESS) {
                selected = selectFavouriteChild(selected);
            }
            switch (selected.igra.status) {
                case WIN:
                    if (selected.igra.status.winner == player)
                        outcome = 100;
                    else
                        outcome = -100;
                    break;
                case IN_PROGRESS:
                    expand(selected);
                    Set<Koordinati> moves = selected.igra.possible_moves;
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
        System.out.println(j);
    }

    private void clean_tree(Node root, Node current) {
        for (Node child : root.children) {
            if (child != null && child != current) {
                clean_tree(child, current);
            }
        }
        visited_nodes.remove(root.igra);
        root = null;
        cleaned++;
    }

    public Koordinati play(Igra igra) {
        this.player = igra.onTurn;

        Node origin;
        if (visited_nodes.containsKey(igra)) {
            System.out.println("already known");
            origin = visited_nodes.get(igra);
            clean_tree(previous_root, origin);
            System.gc();
            previous_root = origin;
        } else {
            System.out.println("new tree");
            origin = new Node(igra, null, igra.onTurn, null);
            previous_root = origin;
            origin.value = simulate(origin);
            expand(origin);
        }
        search(origin);

        Set<Node> children = origin.children;
        Node best = null;
        double max_score = -INF;
        for (Node child : children) {
            // if (child.visits == 0) {
            // continue;
            // }
            double v = child.value;
            if (v > max_score) {
                max_score = v;
                best = child;
            }
        }
        expand(best);
        // System.out.println("wins: " + best.value + " visits: " + best.visits);
        System.out.println("Cleaned nodes:   " + cleaned);
        System.out.println("Remaining nodes: " + visited_nodes.size());
        cleaned = 0;
        Koordinati move = best.move;
        return move;
    }
}