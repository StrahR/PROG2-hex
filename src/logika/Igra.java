package logika;

import koordinati.Koordinati;
import java.util.ArrayList;

public class Igra {

    private static Player[][] board;
    private static int size = 11;

    private static ArrayList<Koordinati> moves = new ArrayList<Koordinati>();

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
    }

    public Igra(final int n) {
        size = n;
        emptyBoard();
        Player.onTurn = Player.RED;
    }

    /**
     * Odigraj potezo p.
     * 
     * @param p
     * @return true, če je bila poteza uspešno odigrana
     */
    public boolean odigraj(final Koordinati p) {
        if (board[p.getX()][p.getY()] != Player.None)
            return false;
        moves.add(p);
        board[p.getX()][p.getY()] = Player.onTurn;
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
