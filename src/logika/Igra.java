package logika;

import splosno.Koordinati;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;

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
        // TODO: a je treba deepcopy narest?
        board = igra.board;
        past_moves = igra.past_moves;
        possible_moves = igra.possible_moves;
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
                if (board[x + dx][y + dy] == onTurn && !visited[x + dx][y + dy]) {
                    stack.add(new Koordinati(x + dx, y + dy));
                }
            }
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
        if (checkWin(p)) {
            status = Status.WIN;
            status.setWinner(onTurn);
        }
        toggleTurn();
        return true;
    }

    /**
     * Razveljavi zadnjo potezo.
     */
    public void razveljavi() {
        final int last = past_moves.size() - 1;
        final Koordinati p = past_moves.get(last);
        board[p.getX()][p.getY()] = Player.None;
        past_moves.remove(last);
        possible_moves.add(p);
        toggleTurn();

        // we can only make a move when game is in progress
        status = Status.IN_PROGRESS;
        status.winner = Player.None;
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
