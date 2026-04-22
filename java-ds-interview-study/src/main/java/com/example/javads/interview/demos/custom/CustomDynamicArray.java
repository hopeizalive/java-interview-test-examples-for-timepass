package com.example.javads.interview.demos.custom;

/**
 * Resizable int array (amortized O(1) append), illustrating how {@link java.util.ArrayList} grows.
 *
 * <p><b>Story:</b> when {@code size} would exceed capacity, allocate ~2× buffer and copy existing elements.
 */
public final class CustomDynamicArray {

    private int[] data = new int[4];
    private int size;

    public void add(int v) {
        ensureCapacity(size + 1);
        data[size++] = v;
    }

    public int get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("index " + i + " size " + size);
        }
        return data[i];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return data.length;
    }

    private void ensureCapacity(int needed) {
        if (needed <= data.length) {
            return;
        }
        // Geometric growth keeps amortized append cost O(1).
        int[] next = new int[data.length * 2];
        System.arraycopy(data, 0, next, 0, size);
        data = next;
    }
}
