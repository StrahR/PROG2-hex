package inteligenca;

import logika.Igra;
import logika.Player;
import runner.Runner;
import splosno.Koordinati;

import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;

public class Minimax {
    final static int INF = Integer.MAX_VALUE;

    private static int modified_bfs(final Igra igra, final Player player, final Koordinati origin) {
        final int size = Igra.size;

        final int[][] smeri = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { -1, 1 }, { 1, -1 } };
        final boolean[][] visited = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                visited[i][j] = false;
            }
        }
        final int[][] distance = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                distance[i][j] = INF;
            }
        }

        // BFS
        final Queue<Koordinati> q = new LinkedList<>();
        distance[origin.getX()][origin.getY()] = 0;
        visited[origin.getX()][origin.getY()] = true;
        q.add(origin);
        while (q.size() > 0) {
            final Koordinati tmp = q.remove();

            for (final int[] s : smeri) {
                final int x = tmp.getX() + s[0];
                final int y = tmp.getY() + s[1];
                if (!Igra.isValidMove(x, y)) {
                    continue;
                }
                if (!visited[x][y]) {
                    visited[x][y] = true;

                    if (igra.board[x][y] == Player.None) {
                        distance[x][y] = Math.min(distance[x][y], distance[tmp.getX()][tmp.getY()] + 1);
                        q.add(new Koordinati(x, y));
                    } else if (igra.board[x][y] == player) {
                        distance[x][y] = Math.min(distance[x][y], distance[tmp.getX()][tmp.getY()]);
                        q.add(new Koordinati(x, y));
                    } else {
                        distance[x][y] = INF;
                    }

                    if (player == Player.RED) {
                        if (y == size - 1) {
                            return distance[x][y];
                        }
                    } else {
                        if (x == size - 1) {
                            return distance[x][y];
                        }
                    }
                }
            }
        }
        return INF;
    }

    /**
     * Hevristična funkcija za evaluacijo trenutnega stanja na plošči
     */
    private static double evaluate(final Igra igra, final Player player) {
        switch (igra.status) {
            case WIN:
                if (igra.status.winner == player)
                    return INF;
                else
                    return -INF;
            default: // case IN_PROGRESS:
                double score = 0;
                for (int i = 0; i < Igra.size; i++) {
                    final int dist_x = modified_bfs(igra, Player.RED, new Koordinati(i, 0));
                    final int dist_y = modified_bfs(igra, Player.BLUE, new Koordinati(0, i));
                    score += (dist_y - dist_x) / (Igra.size + 1.0);
                }

                if (player == Player.BLUE) {
                    return score;
                } else {
                    return -score;
                }
        }
    }

    private static double alpha_beta(final Igra igra, final int depth, final Player player, double alpha, double beta) {
        if (depth == 0 || igra.status != Igra.Status.IN_PROGRESS) {
            return evaluate(igra, player);
        }

        if (igra.onTurn == player) {
            double score = -INF;
            final Set<Koordinati> moves = igra.possibleMoves();
            for (final Koordinati move : moves) {
                igra.odigraj(move);
                score = Math.max(score, alpha_beta(igra, depth - 1, player.opponent(), alpha, beta));
                igra.razveljavi();
                alpha = Math.max(alpha, score);
                if (alpha >= beta) {
                    break;
                }
            }
            return score;

        } else {
            double score = INF;
            final Set<Koordinati> moves = igra.possibleMoves();
            for (final Koordinati move : moves) {
                igra.odigraj(move);
                score = Math.min(score, alpha_beta(igra, depth - 1, player.opponent(), alpha, beta));
                igra.razveljavi();
                beta = Math.min(beta, score);
                if (alpha >= beta) {
                    break;
                }
            }
            return score;
        }
    }

    /**
     * Vrne najboljšo potezo
     */
    public static Koordinati play(final Igra igra) {
        Koordinati best_move = new Koordinati(-1, -1);
        double max_score = -INF;
        final Set<Koordinati> moves = igra.possibleMoves();
        for (final Koordinati move : moves) {
            igra.odigraj(move);
            final double score = alpha_beta(igra, Runner.minimax_depth, igra.onTurn.opponent(), -INF, INF);
            igra.razveljavi();
            if (score > max_score) {
                max_score = score;
                best_move = move;
            }
        }
        return best_move;
    }
}