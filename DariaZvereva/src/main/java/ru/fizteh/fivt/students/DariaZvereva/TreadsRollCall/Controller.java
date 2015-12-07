package ru.fizteh.fivt.students.DariaZvereva.TreadsRollCall;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Controller {
    private int numberOfThreads = 0;
    private List<CallingThreads> listOfThreads;
    private CyclicBarrier cyclicBarrier;
    private boolean finish = false;

    Controller(int n) {
        numberOfThreads = n;
        listOfThreads = new ArrayList<>(n);
        cyclicBarrier = new CyclicBarrier(numberOfThreads + 1, new Runnable() {
            @Override
            public void run() {
                finish = true;
                for (int i = 0; i < listOfThreads.size(); ++i) {
                    finish &= listOfThreads.get(i).getAnswer();
                }
                if (!finish) {
                    System.out.println("Are you ready?");
                } else {
                    return;
                }
            }
        });
    }

    public boolean isFinish() {
        return finish;
    }

    public void start() throws BrokenBarrierException, InterruptedException {
        System.out.println("Are you ready?");
        for (int i = 0; i < numberOfThreads; ++i) {
            listOfThreads.add(new CallingThreads(this));
            listOfThreads.get(i).start();
        }
        while (!finish) {
            cyclicBarrier.await();
        }
    }

    public CyclicBarrier getCyclicBarrier() {
        return cyclicBarrier;
    }
}
