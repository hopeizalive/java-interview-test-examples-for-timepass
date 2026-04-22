package com.example.javads.interview.demos.custom;

/**
 * Set of ints implemented as keys in {@link CustomHashMapChaining} with dummy value (set semantics).
 */
public final class CustomHashSet {

    private static final int PRESENT = 1;
    private final CustomHashMapChaining map;

    public CustomHashSet(int capacity) {
        this.map = new CustomHashMapChaining(capacity);
    }

    public void add(int key) {
        map.put(key, PRESENT);
    }

    public boolean contains(int key) {
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }
}
