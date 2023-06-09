package util;

import model.Frame;
import model.Roll;

import java.util.*;

public class FrameParser {
    public static List<Frame> parseRollsIntoFrames(List<Roll> rolls) {
        List<Frame> frames = new ArrayList<>();
        Iterator<Roll> iterator = rolls.iterator();
        while (iterator.hasNext()) {
            Roll currRoll = iterator.next();
            if(currRoll.isNumeric()){
                handleNumericFrame(frames, iterator, currRoll);
            } else if(currRoll.equals(Roll.STRIKE)) {
                handleStrikeFrame(frames);
            } else { // spare
                // spare can only be the roll of a frame
                throw new IllegalArgumentException();
            }
        }
        return frames;
    }

    private static void handleStrikeFrame(List<Frame> frames) {
        Frame frameToAdd = new Frame();
        frameToAdd.setFirstRoll(Roll.STRIKE);
        frameToAdd.setSecondRoll(null);
        frames.add(frameToAdd);
    }

    private static void handleNumericFrame(List<Frame> frames, Iterator<Roll> iterator, Roll currRoll) {
        Frame tempFrame = new Frame();
        tempFrame.setFirstRoll(currRoll);
        if(iterator.hasNext()){
            handleSecondBallOfNumericFrame(iterator, currRoll, tempFrame);
        } else {
            tempFrame.setSecondRoll(null);
        }
        frames.add(tempFrame);
    }

    private static void handleSecondBallOfNumericFrame(Iterator<Roll> iterator, Roll currRoll, Frame tempFrame) {
        Roll nextRoll = iterator.next();
        if(currRoll.equals(Roll.STRIKE)){
            // second ball of frame can only be number or spare
            throw new IllegalArgumentException();
        }
        if(currRoll.isNumeric() && currRoll.getValue() + nextRoll.getValue() > 10) {
            // max of 10 pins in a frame
            throw new IllegalArgumentException();
        }
        tempFrame.setSecondRoll(nextRoll);
    }
}
