package inteligenca;

import logika.Igra;
import logika.Player;
import logika.Simulator;
import splosno.Koordinati;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class MCTS {
    final static int INF = Integer.MAX_VALUE;
    final static int TIME_LIMIT = 750;

    private Player player;
    private Map<Igra, Node> visited_nodes = new HashMap<Igra, Node>();
    private Node previous_root = null;

    public MCTS(Player player) {
        this.player = player;
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
                child.value = simulate(child);
                parent.children.add(child);
                visited_nodes.put(child.igra, child);
            }
        }
    }

    private int simulate(Node child) {
        child.visits++;
        Player winner = Simulator.simulate(child.igra);
        if (winner == player) {
            return 100;
        }
        return -100;
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
        while (System.currentTimeMillis() - start < TIME_LIMIT) {
            double outcome = 0;
            Node selected = root;
            Set<Koordinati> moves = selected.igra.possibleMoves();
            while (selected.children.size() > 0 && selected.igra.status == Igra.Status.IN_PROGRESS) {
                selected = selectFavouriteChild(selected);
                moves = selected.igra.possibleMoves();
            }
            switch (selected.igra.status) {
                case WIN:
                    if (selected.igra.status.winner == player)
                        outcome = 100;
                    else
                        outcome = -100;
                    break;
                default: // case IN_PROGRESS:
                    expand(selected);
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
            System.out.println("already known");
            origin = visited_nodes.get(igra);
            clean_tree(previous_root, origin);
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