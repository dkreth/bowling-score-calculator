package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class FrameCalculatorTest {
    FrameCalculator frameCalculator = new FrameCalculator();

    // happy path
    @Test
    void calculate() {
        String[] result = frameCalculator.calculate(new String[]{"7", "2", "5", "3", "6", "/", "7", "1", "X", "2", "4"});
        String[] expected = new String[]{"9", "8", "17", "8", "16", "6"};
        Assertions.assertArrayEquals(expected,result);
    }

    // incomplete frames
    @Test
    void calculate_givenIncompleteStrikeFrame_shouldReturnNullFinalTwoFrames() {
        String[] result1 = frameCalculator.calculate(new String[]{"7","2","X"});
        String[] result2 = frameCalculator.calculate(new String[]{"7","2","X", "3"});
        String[] expected1 = new String[] {"9", null};
        String[] expected2 = new String[] {"9", null, null};
        Assertions.assertArrayEquals(expected1, result1);
        Assertions.assertArrayEquals(expected2, result2);
    }

    @Test
    void calculate_givenIncompleteSpareFrame_shouldReturnNullFinalFrame() {
        String[] result = frameCalculator.calculate(new String[]{"7","2","5", "/"});
        String[] expected = new String[] {"9", null};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void calculate_givenIncompleteNumericalFrame_shouldReturnNullFinalFrame() {
        String[] result = frameCalculator.calculate(new String[]{"7","2","5"});
        String[] expected = new String[] {"9", null};
        Assertions.assertArrayEquals(expected, result);
    }

    // Validation
    @Test()
    void calculate_givenNonnumericalRoll_shouldThrowException() {
        Assertions.assertThrows(Exception.class, () -> frameCalculator.calculate(new String[] {"7","2","a","1"}));
    }

    @Test
    void calculate_givenTwoRollsThatSumToMoreThanTen_shouldThrowException() {
        Assertions.assertThrows(Exception.class, () -> frameCalculator.calculate(new String[] {"7","4"}));
    }

    @Test
    void calculate_givenSpareAsFirstBall_shouldThrowException() {
        Assertions.assertThrows(Exception.class, () -> frameCalculator.calculate(new String[] {"7","3","/"}));
    }

    @Test
    void calculate_givenStrikeAsSecondBall_shouldThrowException() {
        Assertions.assertThrows(Exception.class, () -> frameCalculator.calculate(new String[] {"7","3", "6","X"}));
    }

    // weird cases
    @Test
    void calculate_givenPerfectGame_totalScoreShouldEqual300() {
        String[] result = frameCalculator.calculate(new String[]{"X","X","X","X","X","X","X","X","X","X","X","X",});
        String[] expected = new String[]{"30","30","30","30","30","30","30","30","30","30",null, null};
        Assertions.assertArrayEquals(expected, result);
    }
}