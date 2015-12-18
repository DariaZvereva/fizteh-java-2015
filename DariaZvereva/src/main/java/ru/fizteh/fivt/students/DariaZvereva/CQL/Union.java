package ru.fizteh.fivt.students.DariaZvereva.CQL;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by Dasha on 18.12.2015.
 */
public class Union<T, R> {
    private List<Select<T, R>> selections = new ArrayList<>();
    private Select<T, R> curSelect;
    private From<T> curFrom;

    public Union(Select<T, R> tmpSelect) {
        selections.add(tmpSelect);
    }

    public Union<T, R> from(Iterable<T> iterable) {
        curFrom = new From(iterable);
        return this;
    }

    public Union<T, R> select(Class<R> resultClass,
                              Function<T, ?>... constructorFunctions)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        curSelect = curFrom.select(resultClass, constructorFunctions);
        return this;
    }

    public Union<T, R> selectDistinct(Class<R> resultClass,
                                      Function<T, ?>... constructorFunctions) {
        curSelect = curFrom.selectDistinct(resultClass, constructorFunctions);
        return this;
    }

    public Union<T, R> orderBy(Comparator<R>... comparators) {
        curSelect = curSelect.orderBy(comparators);
        return this;
    }

    public Union<T, R> limit(int n) {
        curSelect = curSelect.limit(n);
        return this;
    }

    public Union<T, R> where(Predicate<T> predicate) {
        curSelect = curSelect.where(predicate);
        return this;
    }

    public Union<T, R> having(Predicate<R> predicate) {
        curSelect = curSelect.having(predicate);
        return this;
    }

    public Union<T, R> groupBy(Function<T, ?>... groupByFunctions) {
        curSelect = curSelect.groupBy(groupByFunctions);
        return this;
    }

    public Union<T, R> union() {
        selections.add(curSelect);
        return this;
    }

    public List<R> execute() throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, InvocationTargetException {
        List<R> result = new ArrayList<>();
        selections.add(curSelect);
        for (Select<T, R> select : selections) {
            result.addAll(select.execute());
        }
        return result;
    }
}
