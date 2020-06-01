package logika;

import splosno.Koordinati;
import java.util.Stack;
import java.util.Random;

public class Simulator {
    private static  int size = 11;
    private static Player[][] board = new Player[size][size];
    final static boolean[][] visited = new boolean[size][size];
    final static int[][] smeri = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { -1, 1 }, { 1, -1 } };
    private final static Random generator = new Random();
    
    public static Player simulate(Igra igra) {
        // monte carlo board
        for (int i =0; i < size; i++) {
            for (int j =0; j < size; j++) {
                if (igra.board[i][j] == Player.None) {
                    if (generator.nextInt(2) == 1) {
                        board[i][j] = Player.RED;
                    } else {
                        board[i][j] = Player.BLUE;
                    }
                } else {
                    board[i][j] = igra.board[i][j];
                }
            }
        }

        // init visited
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                visited[i][j] = false;
            }
        }

        // check who won
        for (int i = 0; i < size; i++) {
            // upam da je taprav player
           if (dfs(i, 0)) {
               return Player.RED;
           }
        }
        return Player.BLUE;
    }

    private static boolean dfs(int a, int b) {
        final Stack<Koordinati> stack = new Stack<Koordinati>();
        stack.add(new Koordinati(a, b));
        while (!stack.isEmpty()) {
            final Koordinati tmp = stack.pop();
            final int x = tmp.getX();
            final int y = tmp.getY();
            visited[x][y] = true;

            if (y == size - 1) {
                return true;
            }

            for (final int[] s : smeri) {
                final int dx = s[0];
                final int dy = s[1];
                if (!Igra.isValidMove(x + dx, y + dy))
                    continue;
                if (board[x + dx][y + dy] == Player.RED && !visited[x + dx][y + dy]) {
                    stack.add(new Koordinati(x + dx, y + dy));
                }
            }
        }
        return false;
    }

}