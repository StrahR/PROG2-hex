package logika;

import splosno.Koordinati;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Simulator {

    public static int size = 11;
    private static Player[][] board = new Player[size][size];
    final static boolean[][] visited = new boolean[size][size];
    private static List<Koordinati> randomMoves = new ArrayList<Koordinati>();
    final static int[][] smeri = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { -1, 1 }, { 1, -1 } };
    private final static Random random = new Random();

    public static Player simulate(Igra igra) {

        // monte carlo board
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (igra.board[i][j] != Player.None) {
                    board[i][j] = igra.board[i][j];
                }
            }
        }

        randomMoves.clear();
        randomMoves.addAll(igra.possible_moves);
        Collections.shuffle(randomMoves, random);
        Player other;
        if (igra.onTurn == Player.RED) {
            other = Player.BLUE;
        } else {
            other = Player.RED;
        }
        for (int i = 0; i < randomMoves.size(); i++) {
            int x = randomMoves.get(i).getX();
            int y = randomMoves.get(i).getY();
            if (i % 2 == 0) {
                board[x][y] = igra.onTurn;
            } else {
                board[x][y] = other;
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