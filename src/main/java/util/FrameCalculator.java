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

        return results.toArray(new String[0]);
    }
}
