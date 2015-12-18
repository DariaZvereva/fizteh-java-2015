package ru.fizteh.fivt.students.DariaZvereva.MiniORM;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Dasha on 18.12.2015.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name() default "";
}
