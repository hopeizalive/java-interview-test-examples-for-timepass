package com.example.javads.interview.demos.custom;

import java.util.ArrayList;
import java.util.List;

/**
 * Fixed-capacity LIFO stack backed by an int array (study stack, not {@link java.util.Stack}).
 *
 * <p><b>Interview:</b> prefer {@link java.util.ArrayDeque} in production; this shows array + top index.
 */
public final class CustomStackArray {

    private final int[] data;
    private int top = -1;

    public CustomStackArray(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.data = new int[capacity];
    }

    public void push(int v) {
        if (top + 1 == data.length) {
            throw new IllegalStateException("stack full");
        }
        data[++top] = v;
    }

    public int pop() {
        if (top < 0) {
            throw new IllegalStateException("stack empty");
        }
        return data[top--];
    }

    public int peek() {
        if (top < 0) {
            throw new IllegalStateException("stack empty");
        }
        return data[top];
    }

    /** Bottom-to-top snapshot for demos (index 0 is stack bottom). */
    public List<Integer> snapshot() {
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i <= top; i++) {
            out.add(data[i]);
        }
        return out;
    }
}
