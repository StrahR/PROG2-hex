package logika;

public enum Player {
    RED, BLUE, None;

    public enum Type {
        HUMAN, AI, None;
    }

    public static Player onTurn = None;

    public Player opponent() {
        return switch (this) {
            case RED -> BLUE;
            case BLUE -> RED;
            case None -> None;
        };
    }

    public static void toggleTurn() {
        onTurn = onTurn.opponent();
    }

    @Override
    public String toString() {
        return switch (this) {
            case RED -> "X";
            case BLUE -> "O";
            case None -> " ";
        };
    }
}