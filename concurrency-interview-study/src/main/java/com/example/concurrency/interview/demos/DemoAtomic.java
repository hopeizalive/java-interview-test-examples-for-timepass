package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * Atomic counters and visibility examples for lessons 19-22.
 */
public final class DemoAtomic {

    private DemoAtomic() {}

    /**
     * Lesson 19: atomic increment with {@link AtomicInteger}.
     *
     * <p><b>Purpose:</b> demonstrate lock-free CAS-based counting.
     * <p><b>Role:</b> baseline atomic alternative to synchronized counters.
     * <p><b>Demonstration:</b> concurrent increments converge to expected total.
     */
    public static void l19(StudyContext ctx) throws Exception {
        ctx.log("AtomicInteger: lock-free CAS increments; compareAndSet for conditional updates.");
        // Story step 1: shared atomic counter starts at zero.
        var n = new AtomicInteger();
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(8)) {
            // Story step 2: many worker submissions race to increment the same counter.
            for (int i = 0; i < 10_000_00; i++) {
                ex.submit(() -> n.incrementAndGet());
            }
        }
        // Story step 3: final value should match submitted increments because incrementAndGet is atomic.
        ctx.log("  result: " + n.get());
    }

    /**
     * Lesson 20: high-contention accumulation with {@link LongAdder}.
     *
     * <p><b>Purpose:</b> show striped counters for write-heavy contention.
     * <p><b>Role:</b> contrast with single-cell atomics.
     * <p><b>Demonstration:</b> many threads increment and final {@code sum()} is reported.
     */
    public static void l20(StudyContext ctx) throws Exception {
        ctx.log("LongAdder: sharded accumulation — often better than AtomicLong under high contention.");
        // Story step 1: LongAdder keeps striped cells internally when contention appears.
        var adder = new LongAdder();
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(8)) {
            // Story step 2: high write volume, where striping usually outperforms a single CAS hotspot.
            for (int i = 0; i < 50_000; i++) {
                ex.submit(adder::increment);
            }
        }
        // Story step 3: sum() merges cell values into one readable total.
        ctx.log("  sum: " + adder.sum());
    }

    /**
     * Lesson 21: visibility discussion around non-volatile fields.
     *
     * <p><b>Purpose:</b> explain missing happens-before without proper publication.
     * <p><b>Role:</b> conceptual setup before volatile fix in lesson 22.
     * <p><b>Demonstration:</b> {@code join()} is used to show a known happens-before boundary.
     */
    public static void l21(StudyContext ctx) throws InterruptedException {
        ctx.log("Without volatile, publishing a flag has no happens-before guarantee (reordering / visibility).");
        ctx.log("Interview: readers may never observe the write or see stale field values — fix with volatile or sync.");
        class Holder {
            boolean ready;
            int value;
        }
        var h = new Holder();
        // Story step 1: writer thread stores payload first, then readiness flag.
        var t = new Thread(() -> {
            h.value = 42;
            h.ready = true;
        });
        t.start();
        // Story step 2: join introduces happens-before, so this specific read is safe and deterministic.
        t.join();
        ctx.log("  after join (happens-before via join): ready=" + h.ready + " value=" + h.value);
    }

    /**
     * Lesson 22: volatile stop flag.
     *
     * <p><b>Purpose:</b> show visibility semantics for one-writer/one-reader stop signals.
     * <p><b>Role:</b> practical counterpart to lesson 21's memory-model warning.
     * <p><b>Demonstration:</b> worker loop exits once volatile flag is published.
     */
    public static void l22(StudyContext ctx) throws Exception {
        ctx.log("volatile: visibility + sequential consistency for that field; pairs with one writer pattern.");
        class Holder {
            volatile boolean stop;
        }
        var h = new Holder();
        try (var ex = java.util.concurrent.Executors.newSingleThreadExecutor()) {
            // Story step 1: worker spins until stop flag becomes visible from another thread.
            var f = ex.submit(() -> {
                while (!h.stop) {
                    Thread.onSpinWait();
                }
                return "stopped";
            });
            // Story step 2: publisher flips volatile flag; worker should observe it quickly and exit.
            Thread.sleep(50);
            h.stop = true;
            ctx.log("  worker: " + f.get());
        }
    }
}
