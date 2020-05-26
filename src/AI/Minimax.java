package AI;

import logika.Igra;
import logika.Player;
import splosno.Koordinati;

import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;

public class Minimax {
    final static int INF = Integer.MAX_VALUE;

    private static int modified_bfs(Igra igra, Player player, Koordinati origin) {
        int size = Igra.size;
        
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
        Queue<Koordinati> q = new LinkedList<>();
        distance[origin.getX()][origin.getY()] = 0;
        visited[origin.getX()][origin.getY()] = true;
        q.add(origin);
        while (q.size() > 0) {
            Koordinati tmp = q.remove();

            for (final int[] s : smeri) {
                final int x = tmp.getX() + s[0];
                final int y = tmp.getY() + s[1];
                if (!Igra.isValidMove(x, y))
                    continue;
                if (!visited[x][y]) {
                    visited[x][y] = true;

                    if (Igra.board[x][y] == Player.None) {
                        distance[x][y] = Math.min(distance[x][y], distance[tmp.getX()][tmp.getY()] + 1);
                        q.add(new Koordinati(x, y));
                    } else if (Igra.board[x][y] == player) {
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
    private static double evaluate(Igra igra, Player player) {
        switch (Igra.status) {
            case WIN:
                if (Igra.Status.winner == player)
                    return INF;
                else 
                    return -INF;
            case TIE:
                return 0;
            default:    // case IN_PROGRESS:
                double score = 0;
                for (int i = 0; i < Igra.size; i++) {
                    int dist_x = modified_bfs(igra, Player.RED, new Koordinati(i, 0));
                    int dist_y = modified_bfs(igra, Player.BLUE, new Koordinati(0, i));
                    score += (dist_y - dist_x) / (Igra.size + 1.0);
                }

                if (player == Player.RED)
                    return -score;
                return score;    
            }
    }

    private static double alpha_beta(Igra igra, int depth, Player player, double alpha, double beta) {
        // Mogoce je dobro (potrebno) naredit kopijo igre ne uporabljat lih isto igro
        if (depth == 0 || Igra.status != Igra.Status.IN_PROGRESS)
            return evaluate(igra, player);
            
        if (Player.onTurn == player) {
            double score = -INF;
            Set<Koordinati> moves = Igra.possibleMoves();
            for (Koordinati move : moves) {
                igra.odigraj(move);
                score = Math.max(score, alpha_beta(igra, depth - 1, player, alpha, beta));
                igra.razveljavi();  
                alpha = Math.max(alpha, score);
                if (alpha >= beta) break;
            }
            return score;

        } else {
            double score = INF;
            Set<Koordinati> moves = Igra.possibleMoves();
            for (Koordinati move : moves) {
                igra.odigraj(move);
                score = Math.min(score, alpha_beta(igra, depth - 1, player, alpha, beta));
                igra.razveljavi();  
                beta = Math.min(beta, score);
                if (alpha >= beta) break;
            }
            return score;
        }

    }

    /**
     * Vrne najboljšo potezo
     */
    public static Koordinati play(Igra igra) {
        int depth = 1;
        Koordinati best_move = new Koordinati(-1, -1);
        double max_score = -INF;
        Set<Koordinati> moves = Igra.possibleMoves();
        for (Koordinati move : moves) {
            double score = alpha_beta(igra, depth, Player.onTurn, -INF, INF);
            if (score > max_score) {
                max_score = score;
                best_move = move;
            }
        }
        return best_move;
    }
}