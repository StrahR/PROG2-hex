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
    private Node previous_root;

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
        Node current = selected;
        while (current != root) {
            current.update_value(outcome, player);
            current = current.parent;
        }
        root.update_value(outcome, player);
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
                        outcome = 1;
                    else
                        outcome = -1;
                    break;
                default: // case IN_PROGRESS:
                    expand(selected);
                    Set<Koordinati> moves = selected.igra.possibleMoves();
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
        System.out.println("iterations: " + j);
    }

    private void clean_tree(Node root, Node current) {
        for (Node child : root.children) {
            if (child != null && child != current) {
                clean_tree(child, current);
            }
        }
        visited_nodes.remove(root.igra);
        root = null;
    }

    public Koordinati play(Igra igra) {
        this.player = igra.onTurn;

        Node origin;
        if (visited_nodes.containsKey(igra)) {
            origin = visited_nodes.get(igra);
            clean_tree(previous_root, origin);
            previous_root = origin;
        } else {
            origin = new Node(igra, null, igra.onTurn, null);
            origin.value = simulate(origin);
            expand(origin);
            previous_root = origin;
        }
        search(origin);
        Set<Node> children = origin.children;
        Node best = null;
        double max_score = -INF;
        for (Node child : children) {
            if (child.visits == 0) {
                continue;
            }
            double v = child.value;
            if (v > max_score) {
                max_score = v;
                best = child;
            }
        }
        Koordinati move = best.move;
        return move;
    }
}