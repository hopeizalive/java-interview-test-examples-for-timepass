package com.example.javads.interview.demos.custom;

/**
 * Segment tree for range sum queries and point updates over a fixed int array (1-based tree indexing in recursion).
 *
 * <p><b>Interview:</b> range query O(log n), point update O(log n), memory ~4n for this array representation.
 */
public final class CustomSegmentTree {

    private final int n;
    private final int[] tree;

    public CustomSegmentTree(int[] src) {
        if (src.length == 0) {
            throw new IllegalArgumentException("source array must be non-empty");
        }
        this.n = src.length;
        this.tree = new int[n * 4];
        build(1, 0, n - 1, src);
    }

    public int rangeSum(int l, int r) {
        return query(1, 0, n - 1, l, r);
    }

    public void update(int idx, int val) {
        update(1, 0, n - 1, idx, val);
    }

    private void build(int node, int lo, int hi, int[] src) {
        if (lo == hi) {
            tree[node] = src[lo];
            return;
        }
        int mid = (lo + hi) >>> 1;
        build(node * 2, lo, mid, src);
        build(node * 2 + 1, mid + 1, hi, src);
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }

    private int query(int node, int lo, int hi, int ql, int qr) {
        if (qr < lo || hi < ql) {
            return 0;
        }
        if (ql <= lo && hi <= qr) {
            return tree[node];
        }
        int mid = (lo + hi) >>> 1;
        return query(node * 2, lo, mid, ql, qr) + query(node * 2 + 1, mid + 1, hi, ql, qr);
    }

    private void update(int node, int lo, int hi, int idx, int val) {
        if (lo == hi) {
            tree[node] = val;
            return;
        }
        int mid = (lo + hi) >>> 1;
        if (idx <= mid) {
            update(node * 2, lo, mid, idx, val);
        } else {
            update(node * 2 + 1, mid + 1, hi, idx, val);
        }
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }
}
