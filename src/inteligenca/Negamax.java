package inteligenca;

import logika.Igra;
import logika.Player;
import runner.Runner;
import splosno.Koordinati;

import java.util.Set;

import java.util.Queue;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Negamax {
    final static int INF = Integer.MAX_VALUE;

    private static int dijkstra(Igra igra, Player player) {
        int size = Igra.size;

        final int[][] smeri = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { -1, 1 }, { 1, -1 } };

        final class Node implements Comparator<Node> {
            Koordinati koord;
            int dist;
            // Node predecessor;
            boolean settled;

            public Node() {
            }

            public Node(Koordinati koord, int dist) {
                this.koord = koord;
                this.dist = dist;
                this.settled = false;
            }

            @Override
            public int compare(Node node1, Node node2) {
                if (node1.dist < node2.dist)
                    return -1;
                if (node1.dist > node2.dist)
                    return 1;
                return 0;
            }
        }
        final Node[][] node = new Node[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                node[i][j] = new Node(new Koordinati(i, j), INF);
            }
        }

        // BFS
        Queue<Node> q = new PriorityQueue<Node>(size, new Node());
        if (player == Player.RED) {
            for (int i = 0; i < size; i++) {
                node[i][0].settled = true;
                q.add(node[i][0]);
            }
        } else {
            for (int i = 0; i < size; i++) {
                node[0][i].settled = true;
                q.add(node[0][i]);
            }
        }

        while (q.size() > 0) {
            Node curr = q.poll();

            for (final int[] s : smeri) {
                final int x = curr.koord.getX() + s[0];
                final int y = curr.koord.getY() + s[1];

                if (!Igra.isValidMove(x, y)) {
                    continue;
                }
                if (!node[x][y].settled) {
                    if (Igra.board[x][y] == Player.None) {
                        if (node[x][y].dist > curr.dist + 1) {
                            node[x][y].dist = curr.dist + 1;
                            // node[x][y].predecessor = curr;
                        }
                        q.add(node[x][y]);
                    } else if (Igra.board[x][y] == player) {
                        if (node[x][y].dist > curr.dist) {
                            node[x][y].dist = curr.dist;
                            // node[x][y].predecessor = curr;
                        }
                        q.add(node[x][y]);
                    } else {
                        continue;
                    }

                    if (player == Player.RED) {
                        if (y == size - 1) {
                            return node[x][y].dist;
                        }
                    } else {
                        if (x == size - 1) {
                            return node[x][y].dist;
                        }
                    }
                }
            }
            curr.settled = true;
        }
        return INF;
    }

    /**
     * Hevristična funkcija za evaluacijo trenutnega stanja na plošči Red positive
     */
    private static int evaluate(Igra igra) {
        switch (Igra.status) {
            case WIN:
                if (Igra.Status.winner == Player.RED)
                    return INF;
                else
                    return -INF;
            default: // case IN_PROGRESS:
                int score_red = dijkstra(igra, Player.RED);
                int score_blue = dijkstra(igra, Player.BLUE);
                return score_blue - score_red;
        }
    }

    public static Object[] alpha_beta(Igra igra, int depth, Player player, int alpha, int beta) {
        // Mogoce je dobro (potrebno) naredit kopijo igre ne uporabljat lih isto igro
        int sign = 1;
        Koordinati best_move = new Koordinati(-1, -1);

        if (player == Player.BLUE) {
            sign = -1;
        }
        Set<Koordinati> moves = Igra.possibleMoves();

        if (depth == 0 || Igra.status != Igra.Status.IN_PROGRESS || moves.isEmpty()) {
            return new Object[] { best_move, sign * evaluate(igra) };
        }

        int best_score = -INF;
        for (Koordinati move : moves) {
            igra.odigraj(move);
            int score = -(int) alpha_beta(igra, depth - 1, player.opponent(), -beta, -alpha)[1];
            igra.razveljavi();
            if (score > best_score) {
                best_move = move;
                best_score = score;
            }
            alpha = Math.max(alpha, best_score);
            if (alpha >= beta) {
                break;
            }
        }
        if (Igra.isValidMove(best_move)) {
            return new Object[] { best_move, best_score };
        } else {
            return new Object[] { Naive.play(igra), -INF };
        }
    }

    /**
     * Vrne najboljšo potezo
     */
    public static Koordinati play(Igra igra) {
        int depth = 7;
        Runner.okno.status.setText(String.valueOf(evaluate(igra)));
        return (Koordinati) alpha_beta(igra, depth, Player.onTurn, -INF, INF)[0];
    }
}