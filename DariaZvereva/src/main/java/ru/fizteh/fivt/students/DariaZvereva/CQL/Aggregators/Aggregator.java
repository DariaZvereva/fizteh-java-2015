package ru.fizteh.fivt.students.DariaZvereva.CQL.Aggregators;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Dasha on 18.12.2015.
 */
public interface Aggregator<T, R> extends Function<T, R> {
    R apply(List<T> elements);
}
