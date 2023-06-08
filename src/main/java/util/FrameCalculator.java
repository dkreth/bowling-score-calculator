package util;

import model.Frame;
import model.Roll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FrameCalculator {
    FrameParser frameParser = new FrameParser();
    public String[] calculate(String[] stringRolls) {
        List<Roll> rolls = Arrays.stream(stringRolls).map(Roll::convertStringToRoll).collect(Collectors.toList());
        List<Frame> frames = frameParser.parseRollsIntoFrames(rolls);

        List<String> results = new ArrayList<>();
        boolean secondBallOfFrame = false;

        for(int i=0; i< stringRolls.length; i++) {
            String roll = stringRolls[i];
            switch (roll) {
                case "X":
                    //process strike
                    System.out.println(roll);
                    results.add(getStrikeValue(stringRolls, i));
                    secondBallOfFrame = false;
                    break;
                case "/":
                    // process spare
                    System.out.println(roll);
                    results.remove(results.size()-1); // remove null
                    results.add(getSpareValue(stringRolls,i));
                    secondBallOfFrame = false;
                    break;
                default:
                    try {
                        int parsedRoll = Integer.parseInt(roll);
                        assert parsedRoll >=0;
                        assert parsedRoll <= 9;
                        System.out.println(parsedRoll);
                        if(secondBallOfFrame) {
                            int frameScore = getFaceValue(stringRolls[i - 1]) + getFaceValue(roll);
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

    private String getSpareValue(String[] rolls, int i) {
        try {
            return String.valueOf(10 + getFaceValue(rolls[i+1]));
        } catch (IndexOutOfBoundsException ex) {
            // happens when Spare frame is incomplete
            return null;
        }
    }

    private String getStrikeValue(String[] rolls, int i) {
        try {
            return String.valueOf(10 + getFaceValue(rolls[i+1]) + getFaceValue(rolls[i+2]));
        } catch (IndexOutOfBoundsException ex) {
            // happens when Strike frame is incomplete
            return null;
        }
    }

    private int getFaceValue(String roll) {
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
