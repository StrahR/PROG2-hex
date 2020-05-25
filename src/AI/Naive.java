package AI;

import logika.Igra;
import splosno.Koordinati;

import java.util.Random;
import java.util.Set;

public class Naive {
    public static Koordinati play(Igra igra) {
        Set<Koordinati> moves = Igra.possibleMoves();
        int rand_int = new Random().nextInt(moves.size());
        int i = 0;
        for (Koordinati move: moves) {
            if (i == rand_int)
                return move;
            i++;
        }
        return new Koordinati(-1, -1);
    }
}