
import logika.Igra;

import gui.Okno;
import gui.Platno;

/**
 * Hello world!
 *
 */
public class Hex {
    public static void main(String[] args) {
        Igra game = new Igra();
        Platno platno = new Platno(game);
        Okno okno = new Okno("Hex", platno);
    }
}
