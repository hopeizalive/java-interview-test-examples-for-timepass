package com.example.javads.interview.demos.custom;

/**
 * Fenwick tree (Binary Indexed Tree) for prefix sums and range sums in O(log n), 1-based internal indexing.
 *
 * <p><b>Interview:</b> {@code i & -i} isolates lowest set bit to jump to responsible index range.
 */
public final class CustomFenwickTree {

    private final int[] bit;

    /** {@code n} is number of elements; internal array length {@code n+1}. */
    public CustomFenwickTree(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        this.bit = new int[n + 1];
    }

    /** Adds {@code delta} at position {@code idx1Based} (1..n). */
    public void add(int idx1Based, int delta) {
        for (int i = idx1Based; i < bit.length; i += i & -i) {
            bit[i] += delta;
        }
    }

    /** Sum of elements from 1 through {@code idx1Based} inclusive. */
    public int prefixSum(int idx1Based) {
        int sum = 0;
        for (int i = idx1Based; i > 0; i -= i & -i) {
            sum += bit[i];
        }
        return sum;
    }

    /** Inclusive range sum on 1-based coordinates. */
    public int rangeSum(int l1Based, int r1Based) {
        return prefixSum(r1Based) - prefixSum(l1Based - 1);
    }
}
