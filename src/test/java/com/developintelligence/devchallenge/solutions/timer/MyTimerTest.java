package com.developintelligence.devchallenge.solutions.timer;

import com.developintelligence.devchallenge.solutions.solutions.timer.TimerResult;
import org.junit.jupiter.api.Test;

import static com.developintelligence.devchallenge.solutions.solutions.timer.MyTimer.measureTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author whynot
 */
public class MyTimerTest {


    @Test
    public void testMyTimer() {
        TimerResult result = measureTime(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return 9999;

        });

        System.out.println("Result: " + result.getResult() + ", took(ms): " + result.getTime());
        assertTrue(1000 <= result.getTime() && result.getTime() <= 1005);
        assertEquals(9999, result.getResult());
    }
}
