package com.developintelligence.devchallenge.solutions.solutions.timer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

/**
 * @author whynot
 */
public class MyTimer {

    public static <T> TimerResult<T> measureTime(Supplier<T> supplier) {
        Instant start = Instant.now();

        //Run the Runnable
        T t = supplier.get();

        Instant end = Instant.now();
        return new TimerResult(t, start.until(end, ChronoUnit.MILLIS));
    }

    public static void main(String[] args) {
        TimerResult<Integer> result = measureTime(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 9999;
        });

        System.out.println("Result: " + result.getResult() + ", took(ms): " + result.getTime());
    }
}
