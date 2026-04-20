package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link Collections#binarySearch} on lists vs linear search; random-access vs {@link LinkedList} pitfall.
 *
 * <p><b>Lessons:</b> 14.
 */
public final class DemoSearchLists {

    private DemoSearchLists() {}

    /**
     * Compares {@link Collections#binarySearch} on a sorted {@link ArrayList} vs {@link LinkedList}, and linear {@link
     * List#indexOf}.
     *
     * <p><b>Purpose:</b> show that binary search on {@link LinkedList} is not O(log n) because {@code get(mid)} is
     * linear.
     *
     * <p><b>Role:</b> three timed searches for the same key in structures with the same sorted content.
     *
     * <p><b>Demonstration:</b> {@link Collections#binarySearch} documents random-access lists are required for
     * logarithmic behavior; {@link LinkedList} degrades due to node walking.
     */
    public static void l14(StudyContext ctx) {
        ctx.log("Interview question: Collections.binarySearch on ArrayList vs LinkedList — same complexity?");
        final int n = 20_000;
        List<Integer> arrayList = new ArrayList<>(n);
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            arrayList.add(i * 2);
            linkedList.add(i * 2);
        }
        /* Even values only: 0, 2, …, 2(n−1). Pick a key guaranteed present near the middle. */
        int searchKey = (n / 2) * 2;
        long binArray = DemoSupport.nanos(() -> {
            int idx = Collections.binarySearch(arrayList, searchKey);
            if (idx < 0) {
                throw new AssertionError();
            }
        });
        long binLinked = DemoSupport.nanos(() -> {
            int idx = Collections.binarySearch(linkedList, searchKey);
            if (idx < 0) {
                throw new AssertionError();
            }
        });
        long linearLinked = DemoSupport.nanos(() -> {
            int idx = linkedList.indexOf(searchKey);
            if (idx < 0) {
                throw new AssertionError();
            }
        });
        ctx.log("Single search in sorted lists of size " + n + " for key " + searchKey + ":");
        DemoSupport.logNanos(ctx, "Collections.binarySearch(ArrayList)", binArray);
        DemoSupport.logNanos(ctx, "Collections.binarySearch(LinkedList)", binLinked);
        DemoSupport.logNanos(ctx, "linkedList.indexOf (linear)", linearLinked);
        ctx.log("Takeaway: binarySearch on LinkedList still pays O(n) per probe → overall poor vs ArrayList.");
    }
}
