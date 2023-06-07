package util;

import java.util.ArrayList;
import java.util.List;

public class FrameCalculator {
    public static String[] calculate(String[] rolls) {

        List<String> results = new ArrayList<>();
        boolean secondBallOfFrame = false;

        for(int i=0; i< rolls.length; i++) {
            String roll = rolls[i];
            switch (roll) {
                case "X":
                    //process strike
                    System.out.println(roll);
                    results.add(getStrikeValue(rolls, i));
                    secondBallOfFrame = false;
                    break;
                case "/":
                    // process spare
                    System.out.println(roll);
                    results.remove(results.size()-1); // remove null
                    results.add(getSpareValue(rolls,i));
                    secondBallOfFrame = false;
                    break;
                default:
                    try {
                        int parsedRoll = Integer.parseInt(roll);
                        assert parsedRoll >=0;
                        assert parsedRoll <= 9;
                        System.out.println(parsedRoll);
                        if(secondBallOfFrame) {
                            int frameScore = getFaceValue(rolls[i - 1]) + getFaceValue(roll);
                            results.remove(results.size()-1);
                            results.add(String.valueOf(frameScore));
                            secondBallOfFrame = false;
                        } else {
                            results.add(null);
                            secondBallOfFrame = true;
                        }
                        // TODO: 6/6/2023 what if roll is 15
                    } catch (NumberFormatException | AssertionError ex) {
                        ex.printStackTrace();
                        throw new IllegalArgumentException(ex);
                    }
            }
        }
        return results.toArray(new String[0]);
    }

    private static String getSpareValue(String[] rolls, int i) {
        try {
            return String.valueOf(10 + getFaceValue(rolls[i+1]));
        } catch (IndexOutOfBoundsException ex) {
            // happens when Spare frame is incomplete
            return null;
        }
    }

    private static String getStrikeValue(String[] rolls, int i) {
        try {
            return String.valueOf(10 + getFaceValue(rolls[i+1]) + getFaceValue(rolls[i+2]));
        } catch (IndexOutOfBoundsException ex) {
            // happens when Strike frame is incomplete
            return null;
        }
    }

    private static int getFaceValue(String roll) {
        switch (roll) {
            case "X":
            case "/":
                return 10;
            case "9":
            case "8":
            case "7":
            case "6":
            case "5":
            case "4":
            case "3":
            case "2":
            case "1":
            case "0":
                return Integer.parseInt(roll);
        }
        return 0;
    }
}
