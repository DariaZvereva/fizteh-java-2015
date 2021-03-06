package ru.fizteh.fivt.students.DariaZvereva.CQL;

import ru.fizteh.fivt.students.DariaZvereva.CQL.Aggregators.Aggregator;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Dasha on 18.12.2015.
 */
public class Select<T, R> {
    private Iterable<T> elements;
    private Class<R> resultClass;
    private int limit = 0;

    private Predicate<T> wherePredicate;
    private Predicate<R> havingPredicate;
    private Function<T, ?>[] groupByFunctions;
    private Function<T, ?>[] functions;
    private Comparator<R>[] comparators;

    private Boolean isDistinct;

    public Select(Iterable<T> elems, Class<R> initClass, boolean distinct,
                  Function<T, ?>[] initFunctions) {
        elements = elems;
        resultClass = initClass;
        isDistinct = distinct;
        functions = initFunctions;
    }

    public Select<T, R> where(Predicate<T> predicate) {
        wherePredicate = predicate;
        return this;
    }

    public Select<T, R> having(Predicate<R> initPredicate) {
        havingPredicate = initPredicate;
        return this;
    }

    public Select<T, R> groupBy(Function<T, ?>... expressions) {
        groupByFunctions = expressions;
        if (expressions.length == 0) {
            throw new IllegalStateException("Group by is made only by non-zero parameters");
        }
        isDistinct = true;
        return this;
    }

    public Select<T, R> orderBy(Comparator<R>... initComparators) {
        comparators = initComparators;
        return this;
    }

    public Select<T, R> limit(int n) {
        limit = n;
        return this;
    }

    public List<R> execute() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<T> tmpList = new ArrayList<>();
        for (T it : elements) {
            tmpList.add(it);
        }

        if (wherePredicate != null) {
            tmpList = tmpList.stream().filter(wherePredicate).collect(Collectors.toList());
        }

        if (isDistinct) {
            tmpList = tmpList.stream().distinct().collect(Collectors.toList());
        }

        if (limit != 0) {
            tmpList = tmpList.stream().limit(limit).collect(Collectors.toList());
        }

        Map<Integer, List<T>> resultMap = new HashMap<>();
        if (groupByFunctions != null) {
            Map<Integer, List<T>> groupByResult = tmpList.stream().collect(Collectors.groupingBy((T elem) -> {
                List<Object> list = new ArrayList<Object>();
                for (int i = 0; i < groupByFunctions.length; ++i) {
                    list.add(groupByFunctions[i].apply(elem));
                }
                return list.hashCode();
            }));
            for (Integer key : groupByResult.keySet()) {
                resultMap.put(key, groupByResult.get(key));
            }
        } else {
            resultMap.put(0, tmpList);
        }

        List<R> result = new ArrayList<>();
        for (Integer key : resultMap.keySet()) {
            List<Object> currentResult = new ArrayList<>();
            if (groupByFunctions == null) {
                for (T element : resultMap.get(key)) {
                    for (int i = 0; i < functions.length; ++i) {
                        currentResult.add(functions[i].apply(element));
                    }
                }
            } else {
                for (int i = 0; i < functions.length; ++i) {
                    if (functions[i] instanceof Aggregator) {
                        currentResult.add(((Aggregator) functions[i]).apply(resultMap.get(key)));
                    } else {
                        currentResult.add(functions[i].apply(resultMap.get(key).get(0)));
                    }
                }
            }

            Class[] returnClasses = new Class[functions.length];
            for (int j = 0; j < currentResult.size()
                    / functions.length; ++j) {
                Object[] arguments = new Object[functions.length];
                for (int i = 0; i < arguments.length; ++i) {
                    arguments[i] = currentResult.get(j * arguments.length + i);
                    if (arguments[i] != null) {
                        returnClasses[i] = arguments[i].getClass();
                    } else {
                        throw new IllegalStateException("Null result of operation");
                    }
                }

                R addItem = null;
                try {
                    addItem = resultClass
                            .getConstructor(returnClasses)
                            .newInstance(arguments);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    e.getMessage();
                }
                if (havingPredicate == null || havingPredicate.test(addItem)) {
                    result.add(addItem);
                }
            }
        }
        if (comparators != null) {
            for (Comparator<R> compare : comparators) {
                result.sort(compare);
            }
        }
        return result;
    }
    public Union<T, R> union() {
        return new Union(this);
    }
}
