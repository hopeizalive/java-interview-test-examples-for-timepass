package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

/**
 * Small helpers shared by timed comparison demos across list/map/set/array lessons.
 *
 * <p>Serves lessons that print wall-clock style timings for <em>relative</em> intuition only (not JMH-grade
 * benchmarks). Used by lessons 1, 4, 8, 12–14, 17, 18, 20.
 */
final class DemoSupport {

    private DemoSupport() {}

    /**
     * Runs {@code action} and returns elapsed nanoseconds.
     *
     * <p><b>Purpose:</b> single place for timing so demos stay readable.
     *
     * <p><b>Role:</b> used by lessons that compare two implementations on the same machine in one JVM run.
     *
     * <p><b>Demonstration:</b> shows that micro-benchmarks are noisy; we only argue directionally.
     */
    static long nanos(Runnable action) {
        long t0 = System.nanoTime();
        action.run();
        return System.nanoTime() - t0;
    }

    static void logNanos(StudyContext ctx, String label, long nanos) {
        ctx.log(String.format("  %s: %,.3f ms", label, nanos / 1_000_000.0));
    }
}
