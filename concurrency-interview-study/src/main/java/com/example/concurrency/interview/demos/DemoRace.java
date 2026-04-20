package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Data-race and race-condition demonstrations (lessons 23-26).
 */
public final class DemoRace {

    private DemoRace() {}

    /** Lesson 23: lost updates with unsynchronized read-modify-write. */
    public static void l23(StudyContext ctx) throws Exception {
        ctx.log("Race: non-atomic read-modify-write loses updates under parallel increments.");
        // Execution story: 10k increments are issued concurrently, but ++ is not atomic across threads.
        var c = new int[] {0};
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < 10_000; i++) {
                ex.submit(() -> c[0]++);
            }
        }
        ctx.log("  expected 10000, often lower: " + c[0]);
    }

    /** Lesson 24: synchronized critical section fix for lesson 23. */
    public static void l24(StudyContext ctx) throws Exception {
        ctx.log("Fix with synchronized: mutual exclusion around the critical section.");
        // Execution story: same workload as l23, but one lock serializes each increment safely.
        var lock = new Object();
        var c = new int[] {0};
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < 10_000; i++) {
                ex.submit(() -> {
                    synchronized (lock) {
                        c[0]++;
                    }
                });
            }
        }
        ctx.log("  count: " + c[0]);
    }

    /** Lesson 25: atomic CAS increment alternative to explicit locks. */
    public static void l25(StudyContext ctx) throws Exception {
        ctx.log("Fix with AtomicInteger: CAS-based increment.");
        // Execution story: compare lock-free atomic path against synchronized path from l24.
        var c = new AtomicInteger();
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < 10_000; i++) {
                ex.submit(c::incrementAndGet);
            }
        }
        ctx.log("  count: " + c.get());
    }

    /**
     * Lesson 26: check-then-act race pattern.
     *
     * <p>Purposefully splits {@code isEmpty()} and {@code push()} so two threads can both pass the check.
     */
    public static void l26(StudyContext ctx) throws Exception {
        ctx.log("Check-then-act race: two threads both pass 'if empty' then both insert.");
        // Execution story: check and action are split, so interleaving can violate the intended single-insert rule.
        class Stack {
            private Integer top;

            synchronized boolean isEmpty() {
                return top == null;
            }

            synchronized void push(int v) {
                top = v;
            }
        }
        var s = new Stack();
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(2)) {
            var a = ex.submit(() -> {
                if (s.isEmpty()) {
                    s.push(1);
                }
            });
            var b = ex.submit(() -> {
                if (s.isEmpty()) {
                    s.push(2);
                }
            });
            a.get();
            b.get();
        }
        ctx.log("  interview fix: combine check+act in one synchronized method or use concurrent deque.");
    }
}
