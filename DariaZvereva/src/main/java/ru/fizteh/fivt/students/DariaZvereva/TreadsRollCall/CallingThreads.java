package ru.fizteh.fivt.students.DariaZvereva.TreadsRollCall;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class CallingThreads extends Thread {
    private boolean answer;
    private Random random = new Random();
    private Controller controller;

    public CallingThreads(Controller cntrl) {
        controller = cntrl;
    }

    public boolean getAnswer() {
        return answer;
    }

    @Override
    public void run() {
        while (!controller.isFinish()) {
            if (random.nextInt(10) != 1) {
                answer = true;
                System.out.println("YES");
            } else {
                answer = false;
                System.out.println("NO");
            }
            try {
                controller.getCyclicBarrier().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
