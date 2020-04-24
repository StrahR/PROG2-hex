
import logika.Igra;
import koordinati.Koordinati;

/**
 * Hello world!
 *
 */
public class Hex {
    public static void main(String[] args) {
        Igra game = new Igra();
        game.odigraj(new Koordinati(1, 2));
        game.odigraj(new Koordinati(3, 2));
        System.out.println(game);
    }
}
