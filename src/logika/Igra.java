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

    public Boolean checkWin() {
        Boolean top = false;
        Boolean bottom = false;
        int[][] smeri = {{1,0}, {0,1}, {-1,0}, {0,-1}, {-1,1}, {1,-1}};
        Boolean[][] visited = new Boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                visited[i][j] = false;
            }
        }

        private static void floodFill(int i, int j) {
            visited[i][j] = true;
            if (Player.onTurn == Player.RED) {
                if (j == 0) top = true;
                if (j == size - 1) bottom = true;
            } else {
                if (i == 0) top = true;
                if (i == size - 1) bottom = true;
            }
            if (top && bottom) return;
            for (int[] s : smeri) {
                int dx = s[0];
                int dy = s[1];
                if (board[i + dx][j + dy] == Player.onTurn  && !visited[i + dx][j + dy]) floodFill( + dx, j + dy); 
        }

        for (int i =0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == Player.onTurn  && !visited[i][j]) {
                    top = false;
                    bottom = false;
                    floodFill(i, j);
                    if (top && bottom) return true;
                }
            }
        }
        
        
    }

    

}
