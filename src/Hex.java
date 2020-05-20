import runner.Runner;

import gui.Okno;
import gui.Platno;

/**
 * Hello world!
 *
 */
public class Hex {
    public static void main(String[] args) {
        Platno platno = new Platno();
        Okno okno = new Okno("Hex", platno);
        Runner.okno = okno;
    }
}
