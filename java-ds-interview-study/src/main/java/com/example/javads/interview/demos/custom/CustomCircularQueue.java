package com.example.javads.interview.demos.custom;

import java.util.ArrayList;
import java.util.List;

/**
 * Bounded FIFO queue using a ring buffer (circular indices over a fixed int[]).
 *
 * <p><b>Interview:</b> distinguish {@code head}/{@code tail} advance with modulo vs shifting elements O(n).
 */
public final class CustomCircularQueue {

    private final int[] buf;
    private int head;
    private int tail;
    private int size;

    public CustomCircularQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.buf = new int[capacity];
    }

    /**
     * Enqueues if not full.
     *
     * @return false when full (no overwrite)
     */
    public boolean offer(int v) {
        if (size == buf.length) {
            return false;
        }
        buf[tail] = v;
        tail = (tail + 1) % buf.length;
        size++;
        return true;
    }

    /** Dequeues front or returns null if empty. */
    public Integer poll() {
        if (size == 0) {
            return null;
        }
        int v = buf[head];
        head = (head + 1) % buf.length;
        size--;
        return v;
    }

    /** Logical order from front to back for logging. */
    public List<Integer> snapshot() {
        List<Integer> out = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            out.add(buf[(head + i) % buf.length]);
        }
        return out;
    }
}
