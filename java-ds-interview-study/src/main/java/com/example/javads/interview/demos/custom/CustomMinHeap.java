package com.example.javads.interview.demos.custom;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary min-heap over ints stored in ArrayList (index formula: parent {@code (i-1)/2}, children {@code 2i+1}, {@code 2i+2}).
 *
 * <p><b>Interview:</b> insert/extract-min are O(log n); peek-min is O(1).
 */
public final class CustomMinHeap {

    private final List<Integer> heap = new ArrayList<>();

    public void add(int v) {
        heap.add(v);
        siftUp(heap.size() - 1);
    }

    public int extractMin() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("empty heap");
        }
        int min = heap.getFirst();
        int last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return min;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /** Restore heap property after insert: bubble small value toward root. */
    private void siftUp(int i) {
        while (i > 0) {
            int p = (i - 1) / 2;
            if (heap.get(p) <= heap.get(i)) {
                break;
            }
            swap(i, p);
            i = p;
        }
    }

    /** Restore heap property after replace root: push large value down. */
    private void siftDown(int i) {
        int n = heap.size();
        while (true) {
            int l = i * 2 + 1;
            int r = l + 1;
            int smallest = i;
            if (l < n && heap.get(l) < heap.get(smallest)) {
                smallest = l;
            }
            if (r < n && heap.get(r) < heap.get(smallest)) {
                smallest = r;
            }
            if (smallest == i) {
                return;
            }
            swap(i, smallest);
            i = smallest;
        }
    }

    private void swap(int i, int j) {
        int t = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, t);
    }
}
