package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FrameCalculatorTest {

    @Test
    void calculate() {
        String[] result = FrameCalculator.calculate(new String[]{"7", "2", "5", "3", "6", "/", "7", "1", "X", "2", "4"});
        String[] expected = new String[]{"9", "8", "17", "8", "16", "6"};
        Assertions.assertArrayEquals(expected,result);
    }

    @Test
    void calculate_givenNonnumericalRoll_shouldThrowException() {
        
    }

    @Test
    void calculate_givenTwoRollsThatSumToMoreThanTen_shouldThrowException() {

    }


    @Test
    void calculate_givenIncompleteStrikeFrame_shouldReturnNullFinalTwoFrames() {

    }

    @Test
    void calculate_givenIncompleteSpareFrame_shouldReturnNullFinalFrame() {

    }

    @Test
    void calculate_givenIncompleteNumericalFrame_shouldReturnNullFinalFrame() {

    }

    @Test
    void calculate_givenSpareAsFirstBall_shouldThrowException() {

    }

    @Test
    void calculate_givenStrikeAsSecondBall_shouldThrowException() {

    }

    @Test
    void calculate_givenPerfectGame_totalScoreShouldEqual300() {

    }
}