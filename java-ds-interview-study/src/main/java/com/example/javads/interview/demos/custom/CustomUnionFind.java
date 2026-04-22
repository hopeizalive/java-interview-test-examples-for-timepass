package com.example.javads.interview.demos.custom;

/**
 * Disjoint-set (Union-Find) with path compression and union-by-rank (near-inverse-Ackermann amortized).
 */
public final class CustomUnionFind {

    private final int[] parent;
    private final int[] rank;

    public CustomUnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    /** Find representative with path compression: point every node on path directly to root. */
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return;
        }
        if (rank[ra] < rank[rb]) {
            parent[ra] = rb;
        } else if (rank[ra] > rank[rb]) {
            parent[rb] = ra;
        } else {
            parent[rb] = ra;
            rank[ra]++;
        }
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }
}
