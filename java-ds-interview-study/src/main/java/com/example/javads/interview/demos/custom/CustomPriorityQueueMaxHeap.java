package com.example.javads.interview.demos.custom;

import java.util.ArrayList;
import java.util.List;

/**
 * Max-heap priority queue (largest at root); used for top-k style extractions in demos.
 */
public final class CustomPriorityQueueMaxHeap {

    private final List<Integer> heap = new ArrayList<>();

    public void offer(int v) {
        heap.add(v);
        up(heap.size() - 1);
    }

    public int poll() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("empty heap");
        }
        int max = heap.getFirst();
        int last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            down(0);
        }
        return max;
    }

    private void up(int i) {
        while (i > 0) {
            int p = (i - 1) / 2;
            if (heap.get(p) >= heap.get(i)) {
                break;
            }
            swap(i, p);
            i = p;
        }
    }

    private void down(int i) {
        int n = heap.size();
        while (true) {
            int l = i * 2 + 1;
            int r = l + 1;
            int largest = i;
            if (l < n && heap.get(l) > heap.get(largest)) {
                largest = l;
            }
            if (r < n && heap.get(r) > heap.get(largest)) {
                largest = r;
            }
            if (largest == i) {
                return;
            }
            swap(i, largest);
            i = largest;
        }
    }

    private void swap(int i, int j) {
        int t = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, t);
    }
}
