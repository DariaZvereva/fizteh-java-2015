package ru.fizteh.fivt.students.DariaZvereva.TwitterStream;

/**
 * Created by Dasha on 11.10.2015.
 */

public class Declenser {

    public static final long TEN = 10;
    public static final long HUNDRED = 100;
    public static final long ELEVEN = 11;
    public static final long FIVE = 5;
    public static final long FIFTEEN = 15;

    public static final String[] MINUTES = {" минуту ", " минуты ", " минут "};
    public static final String[] HOURS = {" час ", " часа ", " часов "};
    public static final String[] DAYS = {" день ", " дня ", " дней "};
    public static final String[] RETWEETS = {" ретвит)", " ретвита)", " ретвитов)"};

    public static int strForm(long number) {
        if (number % TEN == 1 && number % HUNDRED != ELEVEN) {
            return 0;
        }
        if (number % TEN > 1 && number % TEN < FIVE && (number % HUNDRED < FIVE || number % HUNDRED > FIFTEEN)) {
            return 1;
        }
        return 2;
    }
}
