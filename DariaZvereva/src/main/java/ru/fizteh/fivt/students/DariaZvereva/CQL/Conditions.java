package ru.fizteh.fivt.students.DariaZvereva.CQL;

/**
 * Created by Dasha on 10.11.2015.
 */

import java.util.function.Function;
import java.util.function.Predicate;

public class Conditions<T> {

    public static <T> Predicate<T> rlike(Function<T, String> expression, String regexp) {
        return (item -> expression.apply(item).matches(regexp));
    }

    public static <T> Predicate<T> like(Function<T, String> expression, String pattern) {
        return (item -> expression.apply(item).equals(pattern));
    }

    public static <T> Predicate<T> notNull(Function<T, Object> expression) {
        return (item -> !expression.apply(item).equals(null));
    }

}
