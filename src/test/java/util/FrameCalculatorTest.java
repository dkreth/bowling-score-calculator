package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrameCalculatorTest {

    @Test
    void calculate() {
        String[] result = FrameCalculator.calculate(new String[]{"7", "2", "5", "3", "6", "/", "7", "1", "X", "2", "4"});
        String[] expected = new String[]{"9", "8", "17", "8", "16", "6"};
        Assertions.assertArrayEquals(expected,result);
    }
}