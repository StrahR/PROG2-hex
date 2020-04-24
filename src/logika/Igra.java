package logika;

import koordinati.Koordinati;

public class Igra {

    public static enum Player {
        P1, P2, None;

        private static Player onTurn = None;

        private static void toggleTurn() {
            switch (onTurn) {
                case P1:
                    onTurn = P2;
                    break;
                case P2:
                    onTurn = P1;
                    break;
                case None:
            }
        }
    }

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
        Player.onTurn = Player.P1;
    }

    public Igra(final int n) {
        size = n;
        emptyBoard();
        Player.onTurn = Player.P1;
    }

    // Odigra potezo `koordinati`, če je možna. Metoda naj vrne `true`, če je poteza
    // možna, sicer pa `false`.
    public boolean odigraj(final Koordinati koordinati) {
        if (board[koordinati.getX()][koordinati.getY()] != Player.None)
            return false;
        board[koordinati.getX()][koordinati.getY()] = Player.onTurn;
        Player.toggleTurn();
        return true;
    }
}
