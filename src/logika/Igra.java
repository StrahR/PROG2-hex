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
