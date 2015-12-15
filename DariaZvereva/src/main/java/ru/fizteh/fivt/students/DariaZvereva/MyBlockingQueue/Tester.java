package ru.fizteh.fivt.students.DariaZvereva.MyBlockingQueue;

import java.util.ArrayList;
import java.util.List;

public class Tester {
    private static MyBlockingQueue<Integer> queue = new MyBlockingQueue<Integer>(100);

    static class ToOffer implements Runnable {
        private List<Integer> listToOffer = new ArrayList<Integer>();

        ToOffer(Integer n) {
            for (int i = 1; i <= n; ++i) {
                listToOffer.add(i);
            }
        }

        @Override
        public void run() {
            try {
                queue.offer(listToOffer);
                System.out.print("O");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ToTake implements Runnable {
        private List<Integer> listToTake;
        private Integer limit = 0;

        ToTake(Integer n) {
            limit = n;
        }

        @Override
        public void run() {
            try {
                listToTake = queue.take(limit);
                System.out.print("T");
                listToTake.stream().forEach(System.out::println);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new ToOffer(10)).start();
        new Thread(new ToOffer(97)).start();
        new Thread(new ToTake(8)).start();
    }
}
