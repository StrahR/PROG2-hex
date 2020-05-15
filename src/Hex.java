
import logika.Igra;
import splosno.Koordinati;

/**
 * Hello world!
 *
 */
public class Hex {
    public static void main(String[] args) {
        Igra game = new Igra();
        game.odigraj(new Koordinati(0, 2));
        game.odigraj(new Koordinati(0, 3));
        game.odigraj(new Koordinati(1, 2));
        game.odigraj(new Koordinati(1, 3));
        game.odigraj(new Koordinati(2, 2));
        game.odigraj(new Koordinati(2, 3));
        game.odigraj(new Koordinati(3, 2));
        game.odigraj(new Koordinati(3, 3));
        game.odigraj(new Koordinati(4, 2));
        game.odigraj(new Koordinati(4, 3));
        game.odigraj(new Koordinati(5, 2));
        game.odigraj(new Koordinati(5, 3));
        game.odigraj(new Koordinati(6, 2));
        game.odigraj(new Koordinati(6, 3));
        game.odigraj(new Koordinati(7, 2));
        game.odigraj(new Koordinati(7, 3));
        game.odigraj(new Koordinati(8, 2));
        game.odigraj(new Koordinati(8, 3));
        game.odigraj(new Koordinati(9, 2));
        game.odigraj(new Koordinati(9, 3));
        game.odigraj(new Koordinati(10, 2));
        game.odigraj(new Koordinati(10, 3));
        System.out.println(game.checkWin(new Koordinati(10, 3)));
        System.out.println(game);
    }
}
