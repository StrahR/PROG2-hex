package logika;

import splosno.Koordinati;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.Set;

import runner.Runner;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

public class Igra {

    public enum Status {
        WIN, IN_PROGRESS;

        public Player winner = Player.None;

        public void setWinner(final Player p) {
            winner = p;
        }
    }

    public static int size = 11;
    public Player[][] board;
    private ArrayList<Koordinati> past_moves = new ArrayList<Koordinati>();
    public Set<Koordinati> possible_moves = new HashSet<Koordinati>();
    public Status status = Status.IN_PROGRESS;
    public Player onTurn = Player.None;

    public void toggleTurn() {
        onTurn = onTurn.opponent();
    }

    public Set<Koordinati> possibleMoves() {
        return possible_moves;
    }

    /**
     * Izprazni igralno ploščo
     */
    private void emptyBoard() {
        board = new Player[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = Player.None;
            }
        }
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                possible_moves.add(new Koordinati(x, y));
            }
        }
    }

    public Igra() {
        emptyBoard();
        onTurn = Player.RED;
        status = Status.IN_PROGRESS;
        status.winner = Player.None;
    }

    public Igra(final int n) {
        size = n;
        emptyBoard();
        onTurn = Player.RED;
        status = Status.IN_PROGRESS;
        status.winner = Player.None;
    }

    public Igra(final Igra igra) {
        board = new Player[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = igra.board[i][j];
            }
        }
        past_moves = new ArrayList<Koordinati>(igra.past_moves);
        possible_moves = new HashSet<Koordinati>(igra.possible_moves);
        status = igra.status;
        onTurn = igra.onTurn;

    }

    /**
     * Preveri, ali je poteza slučajno izven plošče.
     */
    public static boolean isValidMove(final int x, final int y) {
        return !(x >= size || x < 0 || y >= size || y < 0);
    }

    public static boolean isValidMove(final Koordinati p) {
        return isValidMove(p.getX(), p.getY());
    }

    private boolean checkWin() {
        final int[][] smeri = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { -1, 1 }, { 1, -1 } };

        final class Node implements Comparator<Node> {
            Koordinati koord;
            int dist;
            Node predecessor;
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

        // init node LUT
        final Node[][] node = new Node[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                node[i][j] = new Node(new Koordinati(i, j), Integer.MIN_VALUE);
            }
        }

        Player player = onTurn;

        // init starting positions
        Queue<Node> q = new PriorityQueue<Node>(size, new Node());
        if (player == Player.RED) {
            for (int i = 0; i < size; i++) {
                if (board[i][0] == player) {
                    node[i][0].settled = true;
                    node[i][0].dist = 0;
                    q.add(node[i][0]);
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (board[0][i] == player) {
                    node[0][i].settled = true;
                    node[0][i].dist = 0;
                    q.add(node[0][i]);
                }
            }
        }

        while (q.size() > 0) {
            Node curr = q.poll();

            for (final int[] s : smeri) {
                final int x = curr.koord.getX() + s[0];
                final int y = curr.koord.getY() + s[1];

                if (!isValidMove(x, y)) {
                    continue;
                }
                if (!node[x][y].settled) {
                    if (board[x][y] != player) {
                        continue;
                    }

                    if (node[x][y].dist < curr.dist) {
                        node[x][y].dist = curr.dist + 1;
                        node[x][y].predecessor = curr;
                    }
                    q.add(node[x][y]);

                    if (player == Player.RED && y == size - 1 || player == Player.BLUE && x == size - 1) {
                        curr = node[x][y];
                        ArrayList<Koordinati> path = new ArrayList<Koordinati>();
                        while (curr != null) {
                            path.add(curr.koord);
                            curr = curr.predecessor;
                        }
                        Runner.winning_path = path;
                        return true;
                    }
                }
            }
            curr.settled = true;
        }
        return false;
    }

    public Player getHexColor(final int i, final int j) {
        return board[i][j];
    }

    /**
     * Odigraj potezo p.
     * 
     * @param p
     * @return true, če je bila poteza uspešno odigrana
     */
    public boolean odigraj(final Koordinati p) {
        if (!isValidMove(p.getX(), p.getY()))
            return false;
        if (board[p.getX()][p.getY()] != Player.None)
            return false;
        past_moves.add(p);
        possible_moves.remove(p);
        board[p.getX()][p.getY()] = onTurn;
        if (checkWin()) {
            status = Status.WIN;
            status.setWinner(onTurn);
        }
        toggleTurn();
        return true;
    }

    public Koordinati getLastMove() {
        if (past_moves.isEmpty()) {
            return null;
        } else {
            return past_moves.get(past_moves.size() - 1);
        }
    }

    /**
     * Razveljavi zadnjo potezo.
     */
    public void razveljavi() {
        final Koordinati p = getLastMove();
        if (p != null) {
            board[p.getX()][p.getY()] = Player.None;
            past_moves.remove(p);
            possible_moves.add(p);
            toggleTurn();

            // we can only make a move when game is in progress
            status = Status.IN_PROGRESS;
            status.winner = Player.None;
        }
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                str.append(board[x][y]);
            }
            str.append("\n");
        }
        return str.toString();
    }
}
