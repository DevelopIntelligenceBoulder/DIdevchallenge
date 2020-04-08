package com.developintelligence.devchallenge.solutions.solutions.words;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author whynot
 */
public class Tuple2<A, B> {
    private final A a;
    private final B b;

    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return Objects.equals(a, tuple2.a) &&
                Objects.equals(b, tuple2.b);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Tuple2.class.getSimpleName() + "[",
                "]")
                .add("a=" + a)
                .add("b=" + b)
                .toString();
    }
}
