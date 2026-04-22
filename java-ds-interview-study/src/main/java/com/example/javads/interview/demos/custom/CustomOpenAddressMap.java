package com.example.javads.interview.demos.custom;

import java.util.Arrays;

/**
 * Open-addressing hash map with linear probing (teaching map, not {@link java.util.HashMap}).
 *
 * <p><b>Sentinels:</b> {@link #EMPTY_SLOT} means never occupied; {@link #TOMBSTONE} means deleted but slot may be in probe chain.
 * <p><b>Interview:</b> primary clustering under linear probing; double hashing or chaining reduces clustering.
 */
public final class CustomOpenAddressMap {

    /** Marks a slot that has never held a key (probe chain ends here for this hash base). */
    public static final int EMPTY_SLOT = Integer.MIN_VALUE;

    /** Marks a deleted slot so later probes for colliding keys can still find their entries. */
    public static final int TOMBSTONE = Integer.MIN_VALUE + 1;

    private final int[] keys;
    private final int[] values;
    private int size;

    public CustomOpenAddressMap(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        keys = new int[capacity];
        values = new int[capacity];
        Arrays.fill(keys, EMPTY_SLOT);
    }

    public void put(int key, int value) {
        int idx = findSlotForInsert(key);
        if (keys[idx] == key) {
            values[idx] = value;
            return;
        }
        // New key occupies an empty or tombstone slot.
        size++;
        keys[idx] = key;
        values[idx] = value;
    }

    public Integer get(int key) {
        int idx = locateKey(key);
        return idx < 0 ? null : values[idx];
    }

    public boolean containsKey(int key) {
        return locateKey(key) >= 0;
    }

    public boolean remove(int key) {
        int idx = locateKey(key);
        if (idx < 0) {
            return false;
        }
        keys[idx] = TOMBSTONE;
        size--;
        return true;
    }

    public int size() {
        return size;
    }

    /**
     * Finds index of {@code key} or -1 if absent. Stops on {@link #EMPTY_SLOT} (true miss in this chain).
     */
    private int locateKey(int key) {
        int base = Math.floorMod(key, keys.length);
        for (int i = 0; i < keys.length; i++) {
            int idx = (base + i) % keys.length;
            if (keys[idx] == EMPTY_SLOT) {
                return -1;
            }
            if (keys[idx] == key) {
                return idx;
            }
        }
        return -1;
    }

    /**
     * Finds slot for insert/update: first matching key, else first tombstone in probe chain, else first empty.
     */
    private int findSlotForInsert(int key) {
        int base = Math.floorMod(key, keys.length);
        int firstTomb = -1;
        for (int i = 0; i < keys.length; i++) {
            int idx = (base + i) % keys.length;
            if (keys[idx] == key) {
                return idx;
            }
            if (keys[idx] == TOMBSTONE && firstTomb < 0) {
                firstTomb = idx;
            }
            if (keys[idx] == EMPTY_SLOT) {
                return firstTomb >= 0 ? firstTomb : idx;
            }
        }
        throw new IllegalStateException("map full");
    }
}
