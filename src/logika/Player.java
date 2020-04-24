package logika;

public enum Player {
    RED, BLUE, None;

    public static Player onTurn = None;

    public static void toggleTurn() {
        switch (onTurn) {
            case RED:
                onTurn = BLUE;
                break;
            case BLUE:
                onTurn = RED;
                break;
            case None:
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case RED:
                return "X";
            case BLUE:
                return "O";
            default:
                return " ";
        }
    }
}