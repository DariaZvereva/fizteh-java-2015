package ru.fizteh.fivt.students.DariaZvereva.CQL.Aggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Dasha on 18.12.2015.
 */
public class Count<T> implements Aggregator<T, Long> {
    private Function<T, ?> function;
    public Count(Function<T, ?> statement) {
        function = statement;
    }

    @Override
    public Long apply(List<T> elements) {
        return elements.stream().map(function).distinct().count();
    }

    @Override
    public Long apply(T t) {
        return null;
    }
}
