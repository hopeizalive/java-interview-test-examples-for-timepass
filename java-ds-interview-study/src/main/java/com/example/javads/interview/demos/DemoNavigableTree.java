package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * {@link NavigableSet} / {@link TreeSet} range and neighbor queries vs scanning a sorted {@link ArrayList}.
 *
 * <p><b>Lessons:</b> 17.
 */
public final class DemoNavigableTree {

    private DemoNavigableTree() {}

    /**
     * Finds the ceiling of a target using {@link TreeSet#ceiling} vs linear scan on sorted {@link ArrayList}.
     *
     * <p><b>Purpose:</b> show O(log n) neighbor queries on a balanced tree vs O(n) scan.
     *
     * <p><b>Role:</b> same data, two lookup strategies repeated many times for timing contrast.
     *
     * <p><b>Demonstration:</b> navigable sets excel at ordered-set queries ({@code ceiling}, {@code floor}, subviews);
     * sorted array list needs manual binary/linear logic.
     */
    public static void l17(StudyContext ctx) {
        ctx.log("Interview question: TreeSet ceiling/floor vs scanning a sorted ArrayList?");
        NavigableSet<Integer> tree = new TreeSet<>();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 50_000; i += 2) {
            tree.add(i);
            list.add(i);
        }
        int target = 10_001;
        long treeTime = DemoSupport.nanos(() -> {
            int acc = 0;
            for (int k = 0; k < 10_000; k++) {
                Integer c = tree.ceiling(target + (k % 7));
                acc += (c == null ? 0 : c);
            }
            if (acc == Integer.MIN_VALUE) {
                throw new AssertionError();
            }
        });
        long scanTime = DemoSupport.nanos(() -> {
            int acc = 0;
            for (int k = 0; k < 10_000; k++) {
                int t = target + (k % 7);
                Integer found = null;
                for (Integer v : list) {
                    if (v >= t) {
                        found = v;
                        break;
                    }
                }
                acc += (found == null ? 0 : found);
            }
            if (acc == Integer.MIN_VALUE) {
                throw new AssertionError();
            }
        });
        ctx.log("ceiling-like query workload (10k lookups):");
        DemoSupport.logNanos(ctx, "TreeSet.ceiling", treeTime);
        DemoSupport.logNanos(ctx, "linear scan sorted ArrayList", scanTime);
        ctx.log("Example single ceiling(" + target + ") = " + tree.ceiling(target));
        ctx.log("headSet/tailSet views exist on NavigableSet for range scans without copying.");
    }
}
