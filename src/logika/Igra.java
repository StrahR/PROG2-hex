package logika;

import splosno.Koordinati;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;

public class Igra {

    public static Player[][] board;
    public static int size = 11;

    private static ArrayList<Koordinati> moves = new ArrayList<Koordinati>();

    public static enum Status {
        WIN, TIE, IN_PROGRESS;

        public static Player winner = Player.None;

        public static void setWinner(final Player p) {
            winner = p;
        }
    }

    public static Status status = Status.IN_PROGRESS;

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
    }

    public Igra() {
        emptyBoard();
        Player.onTurn = Player.RED;
        status = Status.IN_PROGRESS;
        Status.winner = Player.None;
    }

    public Igra(final int n) {
        size = n;
        emptyBoard();
        Player.onTurn = Player.RED;
        status = Status.IN_PROGRESS;
        Status.winner = Player.None;
    }

    /**
     * Preveri, ali je poteza slučajno izven plošče.
     */
    public static boolean isValidMove(int x, int y) {
        if (x >= size || x < 0 || y >= size || y < 0)
            return false;
        return true;
    }

    /**
     * Vrne množico koordinat vseh mogočih potez za igralca, ki je trenutno na
     * potezi
     */
    public static Set<Koordinati> possibleMoves() {
        Set<Koordinati> moves = new HashSet<Koordinati>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board[x][y] == Player.None)
                    moves.add(new Koordinati(x, y));
            }
        }
        return moves;
    }

    public boolean checkWin(Koordinati p) {
        boolean start = false;
        boolean end = false;
        final int[][] smeri = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { -1, 1 }, { 1, -1 } };
        final boolean[][] visited = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                visited[i][j] = false;
            }
        }

        final Player colour = board[p.getX()][p.getY()];

        // flood-fill
        final Stack<Koordinati> stack = new Stack<Koordinati>();
        stack.add(p);
        while (!stack.isEmpty()) {
            final Koordinati tmp = stack.pop();
            final int x = tmp.getX();
            final int y = tmp.getY();
            visited[x][y] = true;

            if (colour == Player.RED) {
                if (y == 0) {
                    start = true;
                }
                if (y == size - 1) {
                    end = true;
                }
            } else {
                if (x == 0) {
                    start = true;
                }
                if (x == size - 1) {
                    end = true;
                }
            }

            if (start && end) {
                return true;
            }

            for (final int[] s : smeri) {
                final int dx = s[0];
                final int dy = s[1];
                if (!isValidMove(x + dx, y + dy))
                    continue;
                if (board[x + dx][y + dy] == Player.onTurn && !visited[x + dx][y + dy]) {
                    stack.add(new Koordinati(x + dx, y + dy));
                }
            }
        }
        return false;
    }

    public static Player getHexColor(final int i, final int j) {
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
        moves.add(p);
        board[p.getX()][p.getY()] = Player.onTurn;
        if (checkWin(p)) {
            status = Status.WIN;
            Status.setWinner(Player.onTurn);
        }
        Player.toggleTurn();
        return true;
    }

    /**
     * Razveljavi zadnjo potezo.
     */
    public void razveljavi() {
        final int last = moves.size() - 1;
        final Koordinati p = moves.get(last);
        board[p.getX()][p.getY()] = Player.None;
        moves.remove(last);
        Player.toggleTurn();

        // we can only make a move when game is in progress
        status = Status.IN_PROGRESS;
        Status.winner = Player.None;
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
