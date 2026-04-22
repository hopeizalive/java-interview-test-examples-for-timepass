package com.example.javads.interview.demos.custom;

/**
 * Tiny Bloom filter demo: three bit positions per string; false positives possible, false negatives impossible.
 */
public final class CustomBloomFilter {

    private final boolean[] bits;

    public CustomBloomFilter(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive");
        }
        this.bits = new boolean[size];
    }

    public void add(String value) {
        bits[h1(value)] = true;
        bits[h2(value)] = true;
        bits[h3(value)] = true;
    }

    /** If false, value was definitely not added; if true, value might or might not be present. */
    public boolean maybeContains(String value) {
        return bits[h1(value)] && bits[h2(value)] && bits[h3(value)];
    }

    private int h1(String s) {
        return Math.floorMod(s.hashCode(), bits.length);
    }

    private int h2(String s) {
        return Math.floorMod((s.hashCode() * 31) ^ 0x9E3779B9, bits.length);
    }

    private int h3(String s) {
        return Math.floorMod((s.hashCode() * 131) + 17, bits.length);
    }
}
