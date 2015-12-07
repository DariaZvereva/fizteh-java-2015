package ru.fizteh.fivt.students.DariaZvereva.MyBlockingQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dasha on 07.12.2015.
 */
public class MyBlockingQueue<T> {
    private List<T> queue = new LinkedList<T>();
    private int limit = 10;

    public MyBlockingQueue(int n) {
        limit = n;
    }

    public void offer(List<T> e) {

    }

    public void take(int n) {

    }

}
