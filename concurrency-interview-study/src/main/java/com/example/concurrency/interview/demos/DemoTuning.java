package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Thread-pool tuning interview notes and tiny runtime probes (lessons 51-54).
 */
public final class DemoTuning {

    private DemoTuning() {}

    /** Lesson 51: core vs max pool size relationship. */
    public static void l51(StudyContext ctx) {
        ctx.log("corePoolSize vs maximumPoolSize: threads grow above core only when queue is full (for typical pools).");
        // Execution story: build pool with explicit bounds and print configured sizing knobs.
        var pool = new ThreadPoolExecutor(2, 4, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        ctx.log("  core=" + pool.getCorePoolSize() + " max=" + pool.getMaximumPoolSize());
        pool.shutdown();
    }

    /** Lesson 52: unbounded LinkedBlockingQueue overload risk. */
    public static void l52(StudyContext ctx) {
        ctx.log("LinkedBlockingQueue default unbounded capacity: can grow without bound — OOM risk under spikes.");
        // Execution story: instantiate default LinkedBlockingQueue-backed pool to highlight implicit unbounded queue.
        var pool = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        ctx.log("  queue class: " + pool.getQueue().getClass().getSimpleName());
        pool.shutdown();
    }

    /** Lesson 53: rejection policy trade-offs under saturation. */
    public static void l53(StudyContext ctx) {
        ctx.log("DiscardOldestPolicy vs DiscardPolicy: trade fairness for survival under overload.");
        // Execution story: conceptual lesson only — logs summarize selection criteria rather than running synthetic load.
        ctx.log("  interview: prefer explicit CallerRuns, bounded queue, or rate limiting in APIs.");
    }

    /** Lesson 54: practical tuning recap checklist. */
    public static void l54(StudyContext ctx) {
        ctx.log("Tuning recap: measure; bound queues; match pool size to CPU/IO; use semaphores for downstream limits.");
        // Execution story: closes tuning sequence with actionable checklist for production review.
        ctx.log("  CPU-bound ~ cores; IO-bound higher; always handle RejectedExecutionException or use backpressure policies.");
    }
}
