package util;

import model.Frame;
import model.Roll;

import java.util.*;
import java.util.stream.Collectors;

public class FrameCalculator {
    public static String[] calculate(String[] stringRolls) {
        List<Roll> rolls = Arrays.stream(stringRolls).map(Roll::convertStringToRoll).collect(Collectors.toList());
        List<Frame> frames = FrameParser.parseRollsIntoFrames(rolls);

        List<String> results = new ArrayList<>();

        ListIterator<Roll> iterator = rolls.listIterator();
        while (iterator.hasNext()) {
            Roll currRoll = iterator.next();
            String frameResult = calculateFrameResult(iterator, currRoll);
            results.add(frameResult);
        }

        return results.toArray(new String[0]);
    }

    private static String calculateFrameResult(ListIterator<Roll> iterator, Roll currRoll) {
        String frameResult;
        if(currRoll.isNumeric()) {
            frameResult = handleNumericFrame(iterator, currRoll);
        } else {
            // didn't start with a number, so it must be a strike
            frameResult = handleStrikeFrame(iterator);
        }
        return frameResult;
    }

    private static String handleNumericFrame(ListIterator<Roll> iterator, Roll currRoll) {
        String frameResult;
        if(!iterator.hasNext()) {
            // incomplete numeric frame
            frameResult = null;
        } else {
            Roll nextRoll = iterator.next();
            // no need to unwind the iterator.next() we did to get nextRoll since we'll handle the entire frame
            // nextRoll can either be a number or a spare (/)
            if(nextRoll.isNumeric()){
                frameResult = String.valueOf(currRoll.getValue()+nextRoll.getValue());
            } else { // spare
                if(iterator.hasNext()) {
                    // for a spare, the total pins knocked down is always 10, regardless of what each roll was
                    frameResult = String.valueOf(10 + getSpareBonusPoints(iterator));
                } else {
                    // incomplete frame because nextRoll is a spare and there is no follow-up roll
                    frameResult = null;
                }
            }
        }
        return frameResult;
    }

    private static int getSpareBonusPoints(ListIterator<Roll> iterator) {
        Roll rollAfterNext = iterator.next();
        // unwind the iterator.next() we did to peek rollAfterNext's value, since that roll still needs to be processed
        iterator.previous();

        // rollAfterNext can only be a number or a strike. Either way, we can just add its value
        return rollAfterNext.getValue();
    }

    private static String handleStrikeFrame(ListIterator<Roll> iterator) {
        String frameResult;
        // strike is 10 + nextRoll + rollAfterNext
        boolean allBonusRollsForStrikeWereCompleted = containsKMoreRolls(iterator, 2);
        if(allBonusRollsForStrikeWereCompleted) {
            frameResult = String.valueOf(10 + getStrikeBonusPoints(iterator));
        } else {
            // strike followed by nothing
            frameResult = null;
        }
        return frameResult;
    }

    private static boolean containsKMoreRolls(ListIterator<Roll> iterator, int k) {
        boolean result = true;

        // check for k more rolls
        int i = 0; // need to save count for unwinding
        for (; i < k; i++) {
            if (iterator.hasNext()) {
                iterator.next();
            } else {
                result = false;
                break;
            }
        }

        // unwind iterator
        for (; i> 0; i--) {
            iterator.previous();
        }

        return result;
    }

    private static int getStrikeBonusPoints(ListIterator<Roll> iterator) {
        Roll nextRoll = iterator.next();
        Roll rollAfterNext = iterator.next();

        // unwinding the two iterator.next() calls we did to peek nextRoll's and rollAfterNext's values, since those
        //  rolls still needs to be processed
        iterator.previous();
        iterator.previous();

        int bonusPoints;
        // nextRoll-rollAfterNext pairing can either be: number-number, number-/, X-number, X-X
        if(nextRoll.isNumeric() && rollAfterNext.equals(Roll.SPARE)){
            // in case of number-/, (nextRoll + rollAfterNext) == 10
            bonusPoints = 10;
        } else {
            // in case of number-number, X-number, or X-X, we can just add the two values (using 10 for the value of X)
            bonusPoints = nextRoll.getValue() + rollAfterNext.getValue();
        }

        return bonusPoints;
    }
}
