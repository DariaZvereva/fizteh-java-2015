package ru.fizteh.fivt.students.DariaZvereva.TwitterStream;

/**
 * Created by Dasha on 22.09.2015.
 */

import com.beust.jcommander.JCommander;
import twitter4j.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class TwitterStream {

    public static final long PAUSE = 1000;


    public static void main(String[] args)
            throws Exception {

        Settings settings = new Settings();
        JCommander cmd;
        try {
            cmd = new JCommander(settings, args);
        } catch (Exception ex) {
            cmd = new JCommander(settings, new String[] {"-h"});
            cmd.setProgramName("TwitterStream");
            cmd.usage();
            return;
        }
        if (settings.isHelp()) {
            cmd.usage();
            return;
        }
        if (settings.isStream()) {
            streamPrint(settings);
        } else {
            print(settings);
        }
    }


    public static void printTime(Status tweet) {

        Declenser timeDeclension = new Declenser();

        long currentTimeToFormat = System.currentTimeMillis();
        long tweetTimeToFormat = tweet.getCreatedAt().getTime();

        LocalDateTime currentTime = new Date(currentTimeToFormat).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime tweetTime = new Date(tweetTimeToFormat).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        System.out.print("[");
        if (ChronoUnit.MINUTES.between(tweetTime, currentTime) <= 2) {
            System.out.print("только что");
        } else {
            if (ChronoUnit.HOURS.between(tweetTime, currentTime) < 1) {
                System.out.print(new StringBuilder().append(ChronoUnit.MINUTES.between(tweetTime, currentTime))
                        .append(timeDeclension.MINUTES[timeDeclension.strForm(
                                ChronoUnit.MINUTES.between(tweetTime, currentTime))])
                        .append("назад").toString());
            } else {
                if (ChronoUnit.DAYS.between(tweetTime, currentTime) < 1) {
                    System.out.print(new StringBuilder().append(ChronoUnit.HOURS.between(tweetTime, currentTime))
                            .append(timeDeclension.HOURS[timeDeclension.strForm(
                                    ChronoUnit.HOURS.between(tweetTime, currentTime))])
                            .append("назад").toString());
                } else {
                    if (ChronoUnit.DAYS.between(tweetTime, currentTime) == 1) {
                        System.out.print("вчера");
                    } else {
                        System.out.print(new StringBuilder().append(ChronoUnit.DAYS.between(tweetTime, currentTime))
                                .append(timeDeclension.DAYS[timeDeclension.strForm(
                                        ChronoUnit.DAYS.between(tweetTime, currentTime))])
                                .append("назад").toString());
                    }
                }
            }
        }
        System.out.print("]");
    }

    public static void printRetweetsCount(Status status) {
        Declenser retweetDeclension = new Declenser();
        System.out.println(new StringBuilder().append("(").append(status.getRetweetCount())
                .append(retweetDeclension.RETWEETS[retweetDeclension.strForm(status.getRetweetCount())]).toString());
    }

    public static void printTweet(Status status) {
        System.out.print(new StringBuilder().append("@").append(status.getUser().getName()).append(": ")
                .append(status.getText()));
    }

    public static void printRetweet(Status status) {
        System.out.println(new StringBuilder().append("@").append(status.getUser().getName())
                .append(" ретвитнул: @").append(status.getRetweetedStatus().getUser().getName()).append(": ")
                .append(status.getRetweetedStatus().getText()).toString());
        System.out.println();
    }

    //Twitter Stream
    public static void streamPrint(Settings settings)
            throws Exception {

        twitter4j.TwitterStream twitterStream;
        twitterStream = new TwitterStreamFactory().getInstance();


        StatusAdapter listener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                try {
                    Thread.sleep(PAUSE);
                } catch (InterruptedException e) {
                    System.out.print(e.getMessage());
                }
                if (!status.isRetweet()) {
                    printTweet(status);
                    System.out.println();
                } else {
                    if (!settings.isRetweetsHidden()) {
                        printRetweet(status);
                    }
                }
            }
        };
        twitterStream.addListener(listener);
        if (settings.getQuery() == "" && settings.getPlace() == "") {
            twitterStream.sample();
        } else {
            FilterQuery filter;
            //set filter
            String[] track = new String[1];
            track[0] = settings.getQuery();
            filter = new FilterQuery(0, new long[0], track);
            if (!settings.getPlace().equals("")) {
                GoogleGeoLocation findPlace;
                findPlace = new GoogleGeoLocation((settings.getPlace()));
                double[][] bounds = {{findPlace.getBounds().southwest.lng,
                        findPlace.getBounds().southwest.lat},  //широта южная
                        {findPlace.getBounds().northeast.lng,  //долгота северная
                                findPlace.getBounds().northeast.lat}};
                filter.locations(bounds);
            }
            twitterStream.filter(filter);
        }
    }

    //Search
    public static void print(Settings settings) {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            Query query = new Query(settings.getQuery());
            if (!settings.getPlace().equals("")) {
                if (!settings.getPlace().equals("nearby")) {
                    GoogleGeoLocation googleFindPlace;
                    googleFindPlace = new GoogleGeoLocation(settings.getPlace());
                    GeoLocation geoLocation;
                    geoLocation = new GeoLocation(googleFindPlace.getLocation().lat,
                            googleFindPlace.getLocation().lng);
                    query.setGeoCode(geoLocation,
                            googleFindPlace.getRadius(), Query.KILOMETERS);

                }
            }
            query.setCount(settings.getLimit());
            QueryResult result;
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            if (tweets.isEmpty()) {
                System.out.println("По данному запросу не найдено результатов");
                return;
            }
            for (Status tweet : tweets) {
            Thread.sleep(PAUSE);
                if (!tweet.isRetweet()) {
                    printTime(tweet);
                    printTweet(tweet);
                    printRetweetsCount(tweet);
                    System.out.println();
                } else {
                    if (!settings.isRetweetsHidden()) {
                        printTime(tweet);
                        printRetweet(tweet);
                    }
                }
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
