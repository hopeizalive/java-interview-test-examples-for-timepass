package com.example.javads.interview.demos.custom;

/**
 * Hash map with separate chaining: each bucket is a linked list of entries (collision handling).
 *
 * <p><b>Interview:</b> load factor triggers resize in real maps; here fixed table length for small demos.
 */
public final class CustomHashMapChaining {

    private final Entry[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public CustomHashMapChaining(int capacity) {
        this.table = new Entry[capacity];
    }

    public void put(int key, int value) {
        int idx = index(key);
        Entry cur = table[idx];
        while (cur != null) {
            if (cur.key == key) {
                cur.value = value;
                return;
            }
            cur = cur.next;
        }
        table[idx] = new Entry(key, value, table[idx]);
        size++;
    }

    public Integer get(int key) {
        Entry cur = table[index(key)];
        while (cur != null) {
            if (cur.key == key) {
                return cur.value;
            }
            cur = cur.next;
        }
        return null;
    }

    public boolean containsKey(int key) {
        return get(key) != null;
    }

    public boolean remove(int key) {
        int idx = index(key);
        Entry cur = table[idx];
        Entry prev = null;
        while (cur != null) {
            if (cur.key == key) {
                if (prev == null) {
                    table[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    /** Bucket index: non-negative remainder fits Java's negative hashCode. */
    private int index(int key) {
        return Math.floorMod(key, table.length);
    }

    private static final class Entry {
        final int key;
        int value;
        Entry next;

        Entry(int key, int value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
