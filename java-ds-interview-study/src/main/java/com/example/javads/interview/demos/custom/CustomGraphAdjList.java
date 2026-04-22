package com.example.javads.interview.demos.custom;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Undirected graph as adjacency lists; BFS for shortest hop count in unweighted graphs.
 */
public final class CustomGraphAdjList {

    private final List<List<Integer>> adj;

    public CustomGraphAdjList(int n) {
        adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addUndirectedEdge(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);
    }

    /**
     * Returns edge count on shortest path from {@code src} to {@code dst}, or -1 if unreachable.
     */
    public int shortestPathLength(int src, int dst) {
        int n = adj.size();
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        ArrayDeque<Integer> q = new ArrayDeque<>();
        dist[src] = 0;
        q.add(src);
        while (!q.isEmpty()) {
            int u = q.removeFirst();
            if (u == dst) {
                return dist[u];
            }
            for (int v : adj.get(u)) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    q.addLast(v);
                }
            }
        }
        return -1;
    }
}
