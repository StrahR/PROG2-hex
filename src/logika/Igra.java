package logika;

import koordinati.Koordinati;

public class Igra {

    private static Player[][] board;
    private static int size = 11;

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
        board[p.getX()][p.getY()] = Player.onTurn;
        Player.toggleTurn();
        return true;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                str.append(board[i][j]);
            }
            str.append("\n");
        }
        return str.toString();
    }

}
