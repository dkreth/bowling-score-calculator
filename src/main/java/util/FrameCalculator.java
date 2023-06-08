package util;

import model.Frame;
import model.Roll;

import java.util.*;
import java.util.stream.Collectors;

public class FrameCalculator {
    private FrameParser frameParser = new FrameParser();
    public String[] calculate(String[] stringRolls) {
        List<Roll> rolls = Arrays.stream(stringRolls).map(Roll::convertStringToRoll).collect(Collectors.toList());
        List<Frame> frames = frameParser.parseRollsIntoFrames(rolls);

        List<String> results = new ArrayList<>();

        ListIterator<Roll> iterator = rolls.listIterator();
        while (iterator.hasNext()) {
            Roll currRoll = iterator.next();

            if(currRoll.isNumeric()) {
                if(iterator.hasNext()) {
                    Roll nextRoll = iterator.next();
                    // nextRoll can either be a number or a spare (/)
                    if(nextRoll.isNumeric()){
                        results.add(String.valueOf(currRoll.getValue()+nextRoll.getValue()));
                        continue;
                    } else {
                        // if nextRoll is a spare, rollAfterNext can either be a number, strike, or not exist
                        if(iterator.hasNext()) {
                            Roll rollAfterNext = iterator.next();
                            // whether rollAfterNext is a number or a strike, we can just add its value to 10 (10 is for the base score of the spare frame)
                            results.add(String.valueOf(10 + rollAfterNext.getValue()));
                            // unwinding the iterator.next() we did to peek rollAfterNext's value
                            iterator.previous();
                        } else {
                            // incomplete frame because nextRoll is a spare and there is no follow-up roll
                            results.add(null);
                        }
                    }
                    // no need to unwind the iterator.next() we did for to get nextRoll since we finished the frame
                } else {
                    // incomplete numeric frame
                    results.add(null);
                }
            } else {
                // didn't start with a number, so it must be a strike
                // strike is 10 + nextRoll + rollAfterNext
                if(iterator.hasNext()) {
                    Roll nextRoll = iterator.next();
                    // nextRoll can either be a strike, a number, or not exist
                    if(iterator.hasNext()) {
                        Roll rollAfterNext = iterator.next();
                        // nextRoll-rollAfterNext pairing can either be: number-number, number-/, X-number, X-X

                        if(nextRoll.isNumeric() && rollAfterNext.equals(Roll.SPARE)){
                            // in case of number-/, (nextRoll + rollAfterNext) == 10
                            results.add(String.valueOf(10+10));
                        } else {
                            // in case of number-number, X-number, or X-X, we can just add the two values (using 10 for the value of X)
                            results.add(String.valueOf(10 + nextRoll.getValue() + rollAfterNext.getValue()));
                        }

                        // unwinding the iterator.next() we did to peek rollAfterNext's value
                        iterator.previous();
                    } else {
                        // strike followed by roll followed by nothing
                        results.add(null);
                    }
                    // unwinding the iterator.next() we did to peek nextRoll's value
                    iterator.previous();
                } else {
                    // strike followed by nothing
                    results.add(null);
                }
            }
        }

//
//        for(int i = 0; i < rolls.size(); i++) {
//            Roll currRoll = rolls.get(i);
//
//            if(currRoll.isNumeric()) {
//                if(i+1 < rolls.size()) {
//                    Roll nextRoll = rolls.get(i + 1);
//                    if(nextRoll.isNumeric()) {
//                        // frame like [4,5]
//                        results.add(String.valueOf(currRoll.getValue()+nextRoll.getValue()));
//                    } else if (nextRoll.equals(Roll.SPARE)) {
//                        // frame like [4,/]
//                        if(i+2 < rolls.size()) {
//                            // next ball after spare has been rolled, so we can calculate the value
//                            int sparePointsFromNextBall = rolls.get(i + 2).getValue();
//                            results.add(String.valueOf(10 + sparePointsFromNextBall));
//                        } else {
//                            // frame ends on a spare, so we can't calculate the value
//                            results.add(null);
//                        }
//                    }
//                    // if frame is completed
//                    results.add(getValueOfTwoRollFrame(currRoll, rolls.get(i+1), rolls.get(i+2)))
//                    results.add(currRoll.getValue() + getFaceValue2(rolls.get(i+1)))
//                } {
//                    // incomplete frame, so null
//                    results.add(null);
//                }
//            }
//        }
//
//        boolean secondBallOfFrame = false;
//
//        for(int i=0; i< rolls.size(); i++) {
//            Roll roll = rolls.get(i);
////            String roll = stringRolls[i];
//            switch (roll) {
//                case STRIKE:
//                    //process strike
//                    System.out.println(roll);
//                    results.add(getStrikeValue(stringRolls, i));
//                    secondBallOfFrame = false;
//                    break;
//                case SPARE:
//                    // process spare
//                    System.out.println(roll);
//                    results.remove(results.size()-1); // remove null
//                    results.add(getSpareValue(stringRolls,i));
//                    secondBallOfFrame = false;
//                    break;
//                default:
//                    try {
//                        int parsedRoll = roll.getValue();
////                        assert parsedRoll >=0;
////                        assert parsedRoll <= 9;
//                        System.out.println(parsedRoll);
//                        if(secondBallOfFrame) {
//                            int frameScore = getFaceValue(stringRolls[i - 1]) + getFaceValue(roll);
//                            results.remove(results.size()-1);
//                            results.add(String.valueOf(frameScore));
//                            secondBallOfFrame = false;
//                        } else {
//                            results.add(null);
//                            secondBallOfFrame = true;
//                        }
//                        // TODO: 6/6/2023 what if roll is 15
//                    } catch (NumberFormatException | AssertionError ex) {
//                        ex.printStackTrace();
//                        throw new IllegalArgumentException(ex);
//                    }
//            }
//        }
        return results.toArray(new String[0]);
    }

//    private int getFaceValue2(Roll roll) {
//        if(roll.isNumeric() || roll.equals(Roll.STRIKE))
//            return roll.getValue();
//        else
//
//        return 0;
//    }

//    private String getSpareValue(String[] rolls, int i) {
//        // spare = (10 - previousRoll + nextRoll) = (currRoll + nextRoll)
//        try {
//            return String.valueOf(10 + getFaceValue(rolls[i+1]));
//        } catch (IndexOutOfBoundsException ex) {
//            // happens when Spare frame is incomplete
//            return null;
//        }
//    }

//    private String getStrikeValue(String[] rolls, int i) {
//        // strike = 10 + next 2 rolls
//        try {
//            return String.valueOf(10 + getFaceValue(rolls[i+1]) + getFaceValue(rolls[i+2]));
//        } catch (IndexOutOfBoundsException ex) {
//            // happens when Strike frame is incomplete
//            return null;
//        }
//    }

//    private int getFaceValue(String roll) {
//        switch (roll) {
//            case "X":
//            case "/":
//                return 10;
//            case "9":
//            case "8":
//            case "7":
//            case "6":
//            case "5":
//            case "4":
//            case "3":
//            case "2":
//            case "1":
//            case "0":
//                return Integer.parseInt(roll);
//        }
//        return 0;
//    }
}
