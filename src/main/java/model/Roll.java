package model;

public enum Roll {
    ZERO (0, true),
    ONE (1, true),
    TWO(2, true),
    THREE(3, true),
    FOUR(4, true),
    FIVE(5, true),
    SIX(6, true),
    SEVEN(7, true),
    EIGHT(8, true),
    NINE(9, true),
    STRIKE(10, false),
    SPARE(0, false); // spare value is equal to (10 - previousBall + nextBall)

    private int value;
    private boolean isNumeric;

    Roll(int value, boolean isNumeric) {
        this.value = value;
        this.isNumeric = isNumeric;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public int getValue() {
        return value;
    }

    public static Roll convertStringToRoll(String stringRoll) {
        switch (stringRoll) {
            case "X":
                return Roll.STRIKE;
            case "/":
                return Roll.SPARE;
            case "9":
                return Roll.NINE;
            case "8":
                return Roll.EIGHT;
            case "7":
                return Roll.SEVEN;
            case "6":
                return Roll.SIX;
            case "5":
                return Roll.FIVE;
            case "4":
                return Roll.FOUR;
            case "3":
                return Roll.THREE;
            case "2":
                return Roll.TWO;
            case "1":
                return Roll.ONE;
            case "0":
                return Roll.ZERO;
            default:
                // roll must be X, /, or 0-9
                throw new IllegalArgumentException();
        }
    }

}
