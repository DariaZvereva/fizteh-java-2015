package ru.fizteh.fivt.students.DariaZvereva.CQL;

/**
 * Created by Dasha on 10.11.2015.
 */

import java.util.Arrays;
import java.util.List;

/**
 * Helper methods to create collections.
 *
 * @author akormushin
 */
public class Sources {

    @SafeVarargs
    public static <T> List<T> list(T... items) {
        return Arrays.asList(items);
    }
}
