package ru.fizteh.fivt.students.DariaZvereva.TreadsRollCall;

import com.beust.jcommander.JCommander;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;

public class ThreadsRollCall {
    public static void main(String[] args) throws IOException, BrokenBarrierException, InterruptedException {
        int numberOfThreads = 0;
        Settings settings = new Settings();
        JCommander cmd;
        try {
            cmd = new JCommander(settings, args);
        } catch (Exception ex) {
            cmd = new JCommander(settings, new String[] {"-h"});
            cmd.setProgramName("ThreadsRollCall");
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
        Controller controller = new Controller(numberOfThreads);
        controller.start();
    }
}
