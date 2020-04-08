package com.developintelligence.devchallenge.solutions.solutions.timer;

/**
 * @author whynot
 */
public class TimerResult<T> {
    private T result;
    private long time;

    public TimerResult(T t, long time) {
        this.result = t;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public T getResult() {
        return result;
    }
}
