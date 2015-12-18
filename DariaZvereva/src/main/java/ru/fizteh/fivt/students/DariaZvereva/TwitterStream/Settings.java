package ru.fizteh.fivt.students.DariaZvereva.TwitterStream;

import com.beust.jcommander.Parameter;

/**
 * Created by Dasha on 11.10.2015.
 */
//Class for commandline parameters
public class Settings {

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

    public int getLimit() {
        return limit;
    }

    public String getPlace() {
        return place;
    }

    public String getQuery() {
        return query;
    }


    public void setQuery(String query) {
        this.query = query;
    }

    public void setRetweetsHidden(boolean flag) {
        hideRetweets = flag;
    }

    public void setStream(boolean flag) {
        this.stream = flag;
    }

    public void setLimit(Integer n) {
        this.limit = n;
    }

    public void setPlace(String placeName) {
        place = placeName;
    }

}

