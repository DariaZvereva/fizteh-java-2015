package ru.fizteh.fivt.students.DariaZvereva.TreadsRollCall;

import com.beust.jcommander.Parameter;

/**
 * Created by Dasha on 07.12.2015.
 */
//Class for commandline parameters
public class Settings {

    @Parameter(names = {"-n"}, description = "Number of threads - a positive number"
            , required = true)
    private int numberOfThreads = 0;

    @Parameter(names = {"-h", "--help"}, help = true)
    private boolean help = false;

    boolean isHelp() {
        return help;
    }

    int getNumberOfThreads() {
        return  numberOfThreads;
    }

}
