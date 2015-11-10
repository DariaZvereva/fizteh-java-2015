package ru.fizteh.fivt.students.DariaZvereva.TwitterStream;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimePrinter {

    public static String printTime(long tweetTimeToFormat, long currentTimeToFormat) {

        Declenser timeDeclension = new Declenser();

        LocalDateTime currentTime = new Date(currentTimeToFormat).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime tweetTime = new Date(tweetTimeToFormat).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();


        if (ChronoUnit.MINUTES.between(tweetTime, currentTime) < 2) {
            return "только что";
        } else {
            if (ChronoUnit.HOURS.between(tweetTime, currentTime) < 1) {
                return new StringBuilder().append(ChronoUnit.MINUTES.between(tweetTime, currentTime))
                        .append(timeDeclension.MINUTES[timeDeclension.strForm(
                                ChronoUnit.MINUTES.between(tweetTime, currentTime))])
                        .append("назад").toString();
            } else {
                if (ChronoUnit.DAYS.between(tweetTime, currentTime) < 1) {
                    return new StringBuilder().append(ChronoUnit.HOURS.between(tweetTime, currentTime))
                            .append(timeDeclension.HOURS[timeDeclension.strForm(
                                    ChronoUnit.HOURS.between(tweetTime, currentTime))])
                            .append("назад").toString();
                } else {
                    LocalDateTime tweetDateTime = tweetTime.toLocalDate().atStartOfDay();
                    LocalDateTime currentDateTime = currentTime.toLocalDate().atStartOfDay();
                    if (ChronoUnit.DAYS.between(tweetDateTime, currentDateTime) == 1) {
                        return "вчера";
                    } else {
                        return new StringBuilder().append(ChronoUnit.DAYS.between(tweetDateTime, currentDateTime))
                                .append(timeDeclension.DAYS[timeDeclension.strForm(
                                        ChronoUnit.DAYS.between(tweetDateTime, currentDateTime))])
                                .append("назад").toString();
                    }
                }
            }
        }
    }

}
