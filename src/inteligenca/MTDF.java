package inteligenca;

import logika.Igra;
import runner.Runner;
import splosno.Koordinati;

import java.util.AbstractMap;

public class MTDF extends Negamax {
    public static AbstractMap.SimpleEntry<Koordinati, Integer> MTD(final Igra igra, final int f, final int depth) {
        int g = f;
        int upper_bound = INF;
        int lower_bound = -INF;
        Koordinati move = null;

        while (lower_bound < upper_bound) {
            final int beta = Math.max(g, lower_bound + 1);
            final Object[] ab = alpha_beta(igra, depth, igra.onTurn, beta - 1, beta);
            move = (Koordinati) ab[0];
            g = (int) ab[1];
            if (g < beta) {
                upper_bound = g;
            } else {
                lower_bound = g;
            }
        }
        if (Igra.isValidMove(move)) {
            return new AbstractMap.SimpleEntry<>(move, g);
        } else {
            return new AbstractMap.SimpleEntry<>(Naive.play(igra), -INF);
        }
    }

    public static Koordinati play(final Igra igra) {
        int val = Runner.mtdf_f;
        Koordinati move = new Koordinati(-1, -1);
        for (int i = 2; i <= Runner.mtdf_depth; i += 2) {
            final var ab = MTD(igra, val, i);
            move = ab.getKey();
            val = ab.getValue();
        }
        return move;
    }
}