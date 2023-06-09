package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class FrameCalculatorTest {
    // happy path
    @Test
    void calculate() {
        String[] result = FrameCalculator.calculate(new String[]{"7", "2", "5", "3", "6", "/", "7", "1", "X", "2", "4"});
        String[] expected = new String[]{"9", "8", "17", "8", "16", "6"};
        Assertions.assertArrayEquals(expected,result);
    }

    @Test
    void calculate_givenTestCase1() {
        String[] result = FrameCalculator.calculate(new String[]{"4", "5", "X", "8"});
        String[] expected = new String[]{"9", null, null};
        Assertions.assertArrayEquals(expected,result);
    }

    @Test
    void calculate_givenTestCase2() {
        String[] result = FrameCalculator.calculate(new String[]{"4", "5", "X", "8", "1"});
        String[] expected = new String[]{"9", "19", "9"};
        Assertions.assertArrayEquals(expected,result);
    }



    // incomplete frames
    @Test
    void calculate_givenIncompleteStrikeFrame_shouldReturnNullFinalTwoFrames() {
        String[] result1 = FrameCalculator.calculate(new String[]{"7","2","X"});
        String[] result2 = FrameCalculator.calculate(new String[]{"7","2","X", "3"});
        String[] expected1 = new String[] {"9", null};
        String[] expected2 = new String[] {"9", null, null};
        Assertions.assertArrayEquals(expected1, result1);
        Assertions.assertArrayEquals(expected2, result2);
    }

    @Test
    void calculate_givenIncompleteSpareFrame_shouldReturnNullFinalFrame() {
        String[] result = FrameCalculator.calculate(new String[]{"7","2","5", "/"});
        String[] expected = new String[] {"9", null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_givenIncompleteNumericalFrame_shouldReturnNullFinalFrame() {
        String[] result = FrameCalculator.calculate(new String[]{"7","2","5"});
        String[] expected = new String[] {"9", null};
        Assertions.assertArrayEquals(expected, result);
    }

    // Validation
    @Test()
    void calculate_givenNonnumericalRoll_shouldThrowException() {
        Assertions.assertThrows(Exception.class, () -> FrameCalculator.calculate(new String[] {"7","2","a","1"}));
    }

    @Test
    void calculate_givenTwoRollsThatSumToMoreThanTen_shouldThrowException() {
        Assertions.assertThrows(Exception.class, () -> FrameCalculator.calculate(new String[] {"7","4"}));
    }

    @Test
    void calculate_givenSpareAsFirstBall_shouldThrowException() {
        Assertions.assertThrows(Exception.class, () -> FrameCalculator.calculate(new String[] {"7","3","/"}));
    }

    @Test
    void calculate_givenStrikeAsSecondBall_shouldThrowException() {
        Assertions.assertThrows(Exception.class, () -> FrameCalculator.calculate(new String[] {"7","3", "6","X"}));
    }

    // corner cases
    @Test
    void calculate_givenPerfectGame_totalScoreShouldEqual300() {
        String[] result = FrameCalculator.calculate(new String[]{"X","X","X","X","X","X","X","X","X","X","X","X",});
        String[] expected = new String[]{"30","30","30","30","30","30","30","30","30","30",null, null};
        int resultSum = 0;
        for(String frameScore : result) {
            if(frameScore != null){
                resultSum += Integer.parseInt(frameScore);
            }
        }
        Assertions.assertEquals(300, resultSum);
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_givenStrikeFollowedBySpare_frameShouldBeCalculatedCorrectly() {
        String[] result = FrameCalculator.calculate(new String[]{"X", "3", "/", "4", "5"});
        String[] expected = new String[]{"20","14","9"};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_numbersFollowedByNumbers() {
        String[] result = FrameCalculator.calculate(new String[]{"7", "2", "6", "3"});
        String[] expected = new String[]{"9","9"};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_numbersFollowedByStrike() {
        String[] result = FrameCalculator.calculate(new String[]{"7", "2", "X"});
        String[] expected = new String[]{"9",null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_spareFollowedByNumbers() {
        String[] result = FrameCalculator.calculate(new String[]{"7", "/", "6","3"});
        String[] expected = new String[]{"16","9"};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_spareFollowedByStrike() {
        String[] result = FrameCalculator.calculate(new String[]{"7", "/", "X"});
        String[] expected = new String[]{"20",null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_strikeFollowedByNumbers() {
        String[] result = FrameCalculator.calculate(new String[]{"X", "7", "2"});
        String[] expected = new String[]{"19","9"};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_strikeFollowedBySoloNumber() {
        String[] result = FrameCalculator.calculate(new String[]{"X", "7"});
        String[] expected = new String[]{null, null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_strikeFollowedBySpare() {
        String[] result = FrameCalculator.calculate(new String[]{"X", "7", "/"});
        String[] expected = new String[]{"20",null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_strikeFollowedByStrikeFollowedByNumber() {
        String[] result = FrameCalculator.calculate(new String[]{"X", "X", "7"});
        String[] expected = new String[]{"27",null,null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_strikeFollowedByStrikeFollowedByStrike() {
        String[] result = FrameCalculator.calculate(new String[]{"X", "X", "X"});
        String[] expected = new String[]{"30",null,null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_strikeFollowedByStrike() {
        String[] result = FrameCalculator.calculate(new String[]{"X", "X"});
        String[] expected = new String[]{null,null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_soloSpare() {
        String[] result = FrameCalculator.calculate(new String[]{"7", "/"});
        String[] expected = new String[]{null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_soloStrike() {
        String[] result = FrameCalculator.calculate(new String[]{"X"});
        String[] expected = new String[]{null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_soloNumber() {
        String[] result = FrameCalculator.calculate(new String[]{"7"});
        String[] expected = new String[]{null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_emptyInput() {
        String[] result = FrameCalculator.calculate(new String[]{});
        String[] expected = new String[]{};
        Assertions.assertArrayEquals(expected, result);
    }





}