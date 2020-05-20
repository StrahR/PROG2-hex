package logika;

public enum Player {
    RED, BLUE, None;

    public enum Type {
        HUMAN, AI, None;
    }

    public static Player onTurn = None;

    public static Player opponent() {
        switch (onTurn) {
            case RED:
                return BLUE;
            case BLUE:
                return RED;
            default:
                return None;
        }
    }

    public static void toggleTurn() {
        onTurn = opponent();
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