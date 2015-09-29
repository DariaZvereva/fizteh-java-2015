package ru.fizteh.fivt.students.DariaZvereva.TwitterStream;

/**
 * Created by Dasha on 22.09.2015.
 */

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import twitter4j.*;

import java.util.Date;
import java.util.List;

//Class for commandline parameters
class Settings {

    @Parameter(names = {"-q", "--query"}, description = "Query or keywords"
            + " for stream", required = true)
    private String query = "";

    @Parameter(names = {"-p", "--place"}, description = "Place")
    private String place = "";

    @Parameter(names = {"-s", "--stream"}, description = "Twitterstream")
    private boolean stream = false;

    @Parameter(names = "--hideRetweets", description = "Don`t show retweets")
    private boolean hideRetweets = false;

    @Parameter(names = {"-l", "--limit"}, description = "Number of tweets")
    private Integer limit = Integer.MAX_VALUE;

    @Parameter(names = {"-h", "--help"}, help = true)
    private boolean help = false;

    public boolean isHelp() {
        return help;
    }

    public boolean isRetweetsHidden() {
        return hideRetweets;
    }

    public boolean isStream() {
        return stream;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getPlace() {
        return place;
    }

    public String getQuery() {
        return query;
    }

}

public class TwitterStream {

    public static final String ANSI_WHITE = "\u001B[0m";
    public static final String ANSI_BLUE =  "\u001b[34m";
    public static final long MIN = 1000 * 60;
    public static final long HOUR = 1000 * 60 * 60;
    public static final long DAY = 1000 * 60 * 60 * 24;
    public static final long TEN = 10;
    public static final long ELEVEN = 11;
    public static final long FIVE = 5;
    public static final long FIVTEEN = 15;
    public static final long PAUSE = 1000;




    public static void main(String[] args) {

        Settings settings = new Settings();
        JCommander cmd = null;
        try {
            cmd = new JCommander(settings, args);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        if (settings.isHelp()) {
            cmd.usage();
            System.exit(0);
        }

        if (settings.isStream()) {
            streamPrint(settings);
        } else {
            print(settings);
        }


    }

    public static String strMin(long min) {
        if (min % TEN == 1 && min != ELEVEN) {
            return " миниуту ";
        }
        if (min % TEN > 1 && min % TEN < FIVE && (min < FIVE
                || min > FIVTEEN)) {
            return " минуты ";
        }
        return " минут ";
    }

    public static String strHour(long hour) {
        if (hour % TEN == 1 && hour != ELEVEN) {
            return " час ";
        }
        if (hour % TEN > 1 && hour % TEN < FIVE
                && (hour < FIVE || hour > FIVTEEN)) {
            return " часа ";
        }
        return " часов ";
    }

    public static String strDay(long day) {
        if (day % TEN == 1 && day != ELEVEN) {
            return " день ";
        }
        if (day % TEN > 1 && day % TEN < FIVE
                && (day < FIVE || day > FIVTEEN)) {
            return " дня ";
        }
        return " дней ";
    }

    public static String strRetweet(long retweets) {
        if (retweets % TEN == 1 && retweets != ELEVEN) {
            return " ретвит)";
        }
        if (retweets % TEN > 1 && retweets % TEN < FIVE
                && (retweets < FIVE || retweets > FIVTEEN)) {
            return " ретвита)";
        }
        return " ретвитов)";
    }


    public static boolean today(Status status) {
        Date date = new Date();
        long currentTime = date.getTime();
        long tweetTime = status.getCreatedAt().getTime();
        long currentDay = currentTime / DAY;
        return (currentTime - currentDay * DAY) >= currentTime - tweetTime;
    }

    public static boolean yesterday(Status status) {
        Date date = new Date();
        long currentTime = date.getTime();
        long tweetTime = status.getCreatedAt().getTime();
        long previousDay = (currentTime - DAY) / DAY;
        return (currentTime - previousDay * DAY) >= currentTime - tweetTime;
    }

    public static void printTime(Status tweet) {
        Date date = new Date();
        long currentTime = date.getTime();
        long tweetTime = tweet.getCreatedAt().getTime();
        long minDif = (currentTime - tweetTime) / MIN;
        long hourDif = (currentTime - tweetTime) / HOUR;
        long dayDif = (currentTime - tweetTime) / DAY;
        System.out.print("[");
        if (minDif <= 2) {
            System.out.print("только что");
        } else {
            if (hourDif < 1) {
                System.out.print(minDif + strMin(minDif) + "назад");
            } else {
                if (today(tweet)) {
                    System.out.print(hourDif + strHour(hourDif) + "назад");
                } else {
                    if (yesterday(tweet)) {
                        System.out.print("вчера");
                    } else {
                        System.out.print(dayDif + strDay(dayDif) + "назад");
                    }
                }
            }
        }
        System.out.print("]");
    }

    public static void printTweet(Status status, boolean hideRetweets) {
        if (status.isRetweet()) {
            if (!hideRetweets) {
                printTime(status);
                System.out.println("@" + status.getUser().getName()
                        + " ретвитнул: @"
                        + status.getRetweetedStatus().getUser().getName()
                        + ": " + status.getRetweetedStatus().getText());
            }
        } else {
            printTime(status);
            System.out.println("@" + status.getUser().getName()
                    + ": " + status.getText() + " (" + status.getRetweetCount()
                    + strRetweet(status.getRetweetCount()));
            System.out.println();
        }
    }
    //Twitter Stream
    public static void streamPrint(Settings settings) {

        twitter4j.TwitterStream twitterStream;
        twitterStream = new TwitterStreamFactory().getInstance();

        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                try {
                    Thread.sleep(PAUSE);
                } catch (InterruptedException e) {
                    System.out.print(e.getMessage());
                }
                printTweet(status, settings.isRetweetsHidden());
            }
            @Override
            public void onDeletionNotice(StatusDeletionNotice
                                                 statusDeletionNotice) { }
            @Override
            public void onTrackLimitationNotice(int i) { }
            @Override
            public void onScrubGeo(long l, long l1) { }
            @Override
            public void onStallWarning(StallWarning stallWarning) { }
            @Override
            public void onException(Exception e) { }
        };

        twitterStream.addListener(listener);
        twitterStream.sample();
        FilterQuery filter;
        //set filter
        String[] track = new String[1];
        track[0] = settings.getQuery();
        long[] follow = new long[0];
        filter = new FilterQuery(0, follow, track);

        twitterStream.filter(filter);


    }
    //Search
    public static void print(Settings settings) {
        Twitter twitter = new TwitterFactory().getInstance();
        int limit = settings.getLimit();
        Integer counter = 0;
        try {
            Query query = new Query(settings.getQuery());
            QueryResult result;
            do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    printTweet(tweet, settings.isRetweetsHidden());
                    limit--;
                    counter++;
                    if (limit == 0) {
                        break;
                    }
                }
                query = result.nextQuery();
            } while (query != null && limit > 0);
        } catch (TwitterException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        if (counter == 0) {
            System.out.println("По данному запросу не найдено результатов");
            System.exit(-1);
        }
    }


}
