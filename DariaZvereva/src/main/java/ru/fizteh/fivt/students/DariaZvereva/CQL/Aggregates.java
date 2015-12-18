package ru.fizteh.fivt.students.DariaZvereva.CQL;

/**
 * Created by Dasha on 10.11.2015.
 */

import ru.fizteh.fivt.students.DariaZvereva.CQL.Aggregators.Avg;
import ru.fizteh.fivt.students.DariaZvereva.CQL.Aggregators.Count;
import ru.fizteh.fivt.students.DariaZvereva.CQL.Aggregators.Max;
import ru.fizteh.fivt.students.DariaZvereva.CQL.Aggregators.Min;

import java.util.function.Function;

/**
 * Aggregate functions.
 *
 * @author akormushin
 */
public class Aggregates {

    public static <C, T extends Comparable<T>> Function<C, T> max(Function<C, T> expression) {
        return new Max<>(expression);
    }

    public static <C, T extends Comparable<T>> Function<C, T> min(Function<C, T> expression) {
        return new Min<>(expression);
    }

    public static <T> Function<T, Long> count(Function<T, ?> expression) {
        return new Count<>(expression);
    }

    public static <T> Function<T, Double> avg(Function<T, ? extends Number> expression) {
        return new Avg<>(expression);
    }

}
