package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 * {@link Set} ordering / {@code equals}-{@code hashCode} pitfalls and {@link PriorityQueue} vs {@link TreeSet}.
 *
 * <p><b>Lessons:</b> 3, 5.
 */
public final class DemoSetsPriority {

    private DemoSetsPriority() {}

    /** Example value type with correct equals/hashCode. */
    private record GoodPoint(int x, int y) {}

    /** Broken type: equals without consistent hashCode — anti-pattern for demo only. */
    @SuppressWarnings("EqualsHashCode")
    private static final class BrokenPoint {
        private final int x;
        private final int y;

        private BrokenPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof BrokenPoint p && p.x == x && p.y == y;
        }
        // hashCode intentionally omitted — violates Object.equals contract in production
    }

    /**
     * Shows {@link HashSet} / {@link LinkedHashSet} / {@link TreeSet} ordering and a broken {@code equals}/{@code
     * hashCode} contract.
     *
     * <p><b>Purpose:</b> connect {@link Set} semantics to {@code equals}/{@code hashCode}.
     *
     * <p><b>Role:</b> Path A broken contract; Path B correct {@link record}; Path C iteration orders.
     *
     * <p><b>Demonstration:</b> equal objects can coexist in a hash bucket set if {@code hashCode} is wrong; {@link
     * TreeSet} uses {@link Comparable} or {@link java.util.Comparator} ordering, not insertion order.
     */
    public static void l03(StudyContext ctx) {
        ctx.log("Interview question: HashSet vs LinkedHashSet vs TreeSet — and what breaks sets?");
        Set<BrokenPoint> broken = new HashSet<>();
        broken.add(new BrokenPoint(1, 2));
        broken.add(new BrokenPoint(1, 2));
        ctx.log("HashSet with equals-without-hashCode type — size (often 2, broken contract): " + broken.size());
        Set<GoodPoint> good = new HashSet<>();
        good.add(new GoodPoint(1, 2));
        good.add(new GoodPoint(1, 2));
        ctx.log("HashSet with record (correct equals/hashCode) — size: " + good.size());
        Set<String> linked = new LinkedHashSet<>();
        linked.add("third");
        linked.add("first");
        linked.add("second");
        ctx.log("LinkedHashSet iteration (insertion order): " + String.join(", ", linked));
        Set<String> tree = new TreeSet<>();
        tree.add("third");
        tree.add("first");
        tree.add("second");
        ctx.log("TreeSet iteration (sorted): " + String.join(", ", tree));
    }

    /**
     * Contrasts min-heap {@link PriorityQueue} behavior with {@link TreeSet} for ordered unique elements.
     *
     * <p><b>Purpose:</b> clarify duplicates, ordering source, and typical “top K” patterns.
     *
     * <p><b>Role:</b> enqueue the same integers into both structures and poll / iterate.
     *
     * <p><b>Demonstration:</b> {@link PriorityQueue} allows duplicates; {@link TreeSet} stores each value once; poll
     * order vs iterator order on priority queue matters for interviews.
     */
    public static void l05(StudyContext ctx) {
        ctx.log("Interview question: PriorityQueue vs TreeSet — duplicates and ordering?");
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(10);
        pq.add(5);
        pq.add(5);
        pq.add(1);
        ctx.log("PriorityQueue: added 10,5,5,1 — size=" + pq.size() + " (duplicates allowed)");
        StringBuilder pollOrder = new StringBuilder("poll order: ");
        while (!pq.isEmpty()) {
            pollOrder.append(pq.poll()).append(' ');
        }
        ctx.log(pollOrder.toString().trim());
        TreeSet<Integer> ts = new TreeSet<>();
        ts.add(10);
        ts.add(5);
        ts.add(5);
        ts.add(1);
        ctx.log("TreeSet: added 10,5,5,1 — size=" + ts.size() + " (duplicates collapse)");
        ctx.log("TreeSet ascending iteration: " + ts);
    }
}
