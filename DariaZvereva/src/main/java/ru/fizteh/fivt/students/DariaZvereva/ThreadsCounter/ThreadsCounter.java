package ru.fizteh.fivt.students.DariaZvereva.ThreadsCounter;

import com.beust.jcommander.JCommander;

import java.io.IOException;

public class ThreadsCounter {

    private static int numberOfThreads;
    public static void main(String[] args) throws IOException {
        Settings settings = new Settings();
        JCommander cmd;
        try {
            cmd = new JCommander(settings, args);
        } catch (Exception ex) {
            cmd = new JCommander(settings, new String[] {"-h"});
            cmd.setProgramName("ThreadsCounter");
            cmd.usage();
            return;
        }
        if (settings.isHelp()) {
            cmd.usage();
            return;
        }
        if (args.length == 0) {
            throw new IOException();
        }
        numberOfThreads = settings.getNumberOfThreads();
        if (numberOfThreads <= 0) {
            cmd.usage();
            throw new IOException();
        }
        createThreads();
    }

    public static void createThreads() {
        for (int i = 1; i <= numberOfThreads; ++i) {
            new ThreadForCount(i, numberOfThreads).start();
        }
    }
}
