package ru.fizteh.fivt.students.DariaZvereva.MyBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Dasha on 07.12.2015.
 */
public class MyBlockingQueue<T> {
    private Queue<T> queue = new LinkedList<T>();
    private int maxSize = 200;
    private Lock lock = new ReentrantLock();
    private final Condition notFull  = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public MyBlockingQueue(int n) {
        maxSize = n;
    }

    public synchronized void offer(List<T> e) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() + e.size() > maxSize) {
                notFull.await();
            }
            e.stream().forEach(queue::add);
            System.out.println(queue.size());
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public synchronized List<T> take(int n) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() < n) {
                notEmpty.await();
            }
            List<T> result = new LinkedList<T>();
            for (int i = 0; i < n; ++i) {
                result.add(queue.remove());
            }
            System.out.println(queue.size());
            notFull.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }
}


