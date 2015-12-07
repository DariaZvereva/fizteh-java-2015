package ru.fizteh.fivt.students.DariaZvereva.ThreadsCounter;

/**
 * Created by Dasha on 07.12.2015.
 */
public class ThreadForCount extends Thread {

    private static int numberOfThreads;
    private int numberOfThisThread;
    private static Object mutex = new Object();
    private static volatile int currentThread = 1;

    ThreadForCount(int n, int number) {
        numberOfThisThread = n;
        numberOfThreads = number;
    }

    @Override
    public void run() {
        synchronized (mutex) {
            while (true) {
                if (currentThread == numberOfThisThread) {
                    System.out.println("Thread-" + currentThread);
                    currentThread %= numberOfThreads;
                    currentThread++;
                    mutex.notifyAll();
                }
                try {
                    mutex.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
