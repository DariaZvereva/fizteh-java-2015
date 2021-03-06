package ru.fizteh.fivt.students.DariaZvereva.CQL.Aggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Dasha on 18.12.2015.
 */
public class Min<T, R extends Comparable<R>> implements Aggregator<T, R> {

    private Function<T, R> function;
    public Min(Function<T, R> statement) {
        function = statement;
    }

    @Override
    public R apply(List<T> elements) {
        return elements.stream().map(function).min(R::compareTo).get();
    }

    @Override
    public R apply(T t) {
        return null;
    }
}
