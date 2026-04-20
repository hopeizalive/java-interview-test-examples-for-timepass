package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * List and {@link Deque} comparison demos backed by {@link ArrayList}, {@link LinkedList}, and {@link ArrayDeque}.
 *
 * <p><b>Lessons:</b> 1 (list flavors), 4 (deque backing stores).
 */
public final class DemoListsAndDeques {

    private DemoListsAndDeques() {}

    /**
     * Compares {@link ArrayList} vs {@link LinkedList} for tail growth, middle insertion, and indexed {@code get}.
     *
     * <p><b>Purpose:</b> exercise the classic interview trade-off: contiguous array + amortized grow vs linked nodes.
     *
     * <p><b>Role:</b> Path A/B timing for comparable workloads in one JVM run (noisy but directional).
     *
     * <p><b>Demonstration:</b> {@link ArrayList} wins random access; {@link LinkedList} is poor at {@code get(i)}; prepend
     * is cheap on doubly-linked nodes and expensive on array (shift).
     */
    public static void l01(StudyContext ctx) {
        ctx.log("Interview question: When do you pick ArrayList vs LinkedList in Java?");
        final int n = 8_000;
        /*
         * Phase 1 — tail append only.
         * Both structures are fine here; ArrayList often wins on locality and fewer allocations per element.
         */
        long arrayAppend = DemoSupport.nanos(() -> {
            List<Integer> list = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                list.add(i);
            }
        });
        long linkedAppend = DemoSupport.nanos(() -> {
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < n; i++) {
                list.add(i); // tail append uses last node
            }
        });
        ctx.log("Tail append (" + n + " integers):");
        DemoSupport.logNanos(ctx, "ArrayList", arrayAppend);
        DemoSupport.logNanos(ctx, "LinkedList", linkedAppend);

        /*
         * Phase 2 — prepend (index 0) repeatedly.
         * ArrayList must shift all elements → O(n) per insert at front → catastrophic for this pattern.
         */
        final int prependOps = 2_000;
        long arrayPrepend = DemoSupport.nanos(() -> {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < prependOps; i++) {
                list.add(0, i);
            }
        });
        long linkedPrepend = DemoSupport.nanos(() -> {
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < prependOps; i++) {
                list.add(0, i);
            }
        });
        ctx.log("Prepend at index 0 (" + prependOps + " ops):");
        DemoSupport.logNanos(ctx, "ArrayList", arrayPrepend);
        DemoSupport.logNanos(ctx, "LinkedList", linkedPrepend);

        /*
         * Phase 3 — random indexed reads.
         * ArrayList.get(i) is O(1); LinkedList must walk from nearest end → O(n) worst case.
         */
        List<Integer> arrayBacked = new ArrayList<>();
        List<Integer> linkedBacked = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            arrayBacked.add(i);
            linkedBacked.add(i);
        }
        long arrayRandom = DemoSupport.nanos(() -> {
            long acc = 0;
            for (int k = 0; k < 5_000; k++) {
                acc += arrayBacked.get(k * 13 % n);
            }
            if (acc == Long.MIN_VALUE) {
                throw new AssertionError("unreachable");
            }
        });
        long linkedRandom = DemoSupport.nanos(() -> {
            long acc = 0;
            for (int k = 0; k < 5_000; k++) {
                acc += linkedBacked.get(k * 13 % n);
            }
            if (acc == Long.MIN_VALUE) {
                throw new AssertionError("unreachable");
            }
        });
        ctx.log("Random get() workload (5k reads on size " + n + "):");
        DemoSupport.logNanos(ctx, "ArrayList", arrayRandom);
        DemoSupport.logNanos(ctx, "LinkedList", linkedRandom);
        ctx.log("Takeaway: default list choice is ArrayList unless you have a proven deque/front-heavy pattern.");
    }

    /**
     * Contrasts {@link ArrayDeque} vs {@link LinkedList} when both are used only through the {@link Deque} API.
     *
     * <p><b>Purpose:</b> show the JDK’s preferred resizable-array deque versus a linked-node deque.
     *
     * <p><b>Role:</b> complements lesson 1 by focusing on stack/queue operations, not {@link List#get}.
     *
     * <p><b>Demonstration:</b> {@link ArrayDeque} is usually faster and more memory-efficient; {@link LinkedList} also
     * implements {@link List} but pays node overhead when you only need a deque.
     */
    public static void l04(StudyContext ctx) {
        ctx.log("Interview question: ArrayDeque vs LinkedList as a Deque — what do you use?");
        final int ops = 200_000;
        Deque<Integer> ring = new ArrayDeque<>();
        Deque<Integer> finalRing = ring;
        long ad = DemoSupport.nanos(() -> {
            for (int i = 0; i < ops; i++) {
                finalRing.addLast(i);
                if ((i & 1) == 0) {
                    finalRing.pollFirst();
                }
            }
        });
        ring = new LinkedList<>();
        Deque<Integer> finalRing1 = ring;
        long ll = DemoSupport.nanos(() -> {
            for (int i = 0; i < ops; i++) {
                finalRing1.addLast(i);
                if ((i & 1) == 0) {
                    finalRing1.pollFirst();
                }
            }
        });
        ctx.log("Mixed addLast/pollFirst (" + ops + " iterations):");
        DemoSupport.logNanos(ctx, "ArrayDeque", ad);
        DemoSupport.logNanos(ctx, "LinkedList", ll);
        ctx.log("Takeaway: prefer ArrayDeque for stack/queue unless you need null elements (neither allows null).");
    }
}
