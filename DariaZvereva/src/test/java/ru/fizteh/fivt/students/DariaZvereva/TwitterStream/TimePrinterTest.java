package ru.fizteh.fivt.students.DariaZvereva.TwitterStream;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Dasha on 10.11.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class TimePrinterTest extends TestCase {
    @Test
    public void timePrinterTest() {
        assertThat(TimePrinter.printTime(LocalDate.of(2015, Month.DECEMBER, 17).atTime(23, 59, 59)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                LocalDate.of(2015, Month.DECEMBER, 18).atTime(0, 0, 0)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), is("только что"));
        assertThat(TimePrinter.printTime(LocalDate.of(2015, Month.DECEMBER, 17).atTime(0, 0, 1)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                LocalDate.of(2015, Month.DECEMBER, 18).atTime(0, 0, 0)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), is("23 часа назад"));
        assertThat(TimePrinter.printTime(LocalDate.of(2015, Month.DECEMBER, 17).atTime(0, 0, 1)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                LocalDate.of(2015, Month.DECEMBER, 18).atTime(0, 0, 2)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), is("вчера"));
        assertThat(TimePrinter.printTime(LocalDate.of(2015, Month.DECEMBER, 17).atTime(23, 59, 59)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                LocalDate.of(2015, Month.DECEMBER, 18).atTime(0, 2, 0)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), is("2 минуты назад"));
        assertThat(TimePrinter.printTime(LocalDate.of(2015, Month.DECEMBER, 17).atTime(23, 59, 59)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                LocalDate.of(2015, Month.DECEMBER, 19).atTime(0, 0, 0)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()), is("2 дня назад"));
    }
}