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
            return 1;
        }
        if (number % TEN > 1 && number % TEN < FIVE && (number < FIVE
                || number > FIFTEEN)) {
            return 2;
        }
        return 3;
    }
/*

    public static String strMin(long min) {
        if (min % TEN == 1 && min != ELEVEN) {
            return " минуту ";
        }
        if (min % TEN > 1 && min % TEN < FIVE && (min < FIVE
                || min > FIFTEEN)) {
            return " минуты ";
        }
        return " минут ";
    }


    public static String strHour(long hour) {
        if (hour % TEN == 1 && hour % HUNDRED != ELEVEN) {
            return " час ";
        }
        if (hour % TEN > 1 && hour % TEN < FIVE
                && (hour < FIVE || hour > FIFTEEN)) {
            return " часа ";
        }
        return " часов ";
    }

    public static String strDay(long day) {
        if (day % TEN == 1 && day != ELEVEN) {
            return " день ";
        }
        if (day % TEN > 1 && day % TEN < FIVE
                && (day < FIVE || day > FIFTEEN)) {
            return " дня ";
        }
        return " дней ";
    }

    public static String strRetweet(long retweets) {
        if (retweets == 0) {
            return "";
        }
        if (retweets % TEN == 1 && retweets != ELEVEN) {
            return " (" + retweets + " ретвит)";
        }
        if (retweets % TEN > 1 && retweets % TEN < FIVE
                && (retweets < FIVE || retweets > FIFTEEN)) {
            return " (" + retweets + " ретвита)";
        }
        return " (" + retweets + " ретвитов)";
    }*/

}
