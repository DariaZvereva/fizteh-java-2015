package ru.fizteh.fivt.students.DariaZvereva.CQL.Aggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Dasha on 18.12.2015.
 */
public class Avg<T> implements Aggregator<T, Double> {
    private Function<T, ? extends Number> function;
    public Avg(Function<T, ? extends Number> statement) {
        function = statement;
    }
    @Override
    public Double apply(List<T> elements) {
        return elements.stream().map(function).mapToDouble(element -> (Double) element)
                .average().getAsDouble();
    }

    @Override
    public Double apply(T t) {
        return null;
    }
}
