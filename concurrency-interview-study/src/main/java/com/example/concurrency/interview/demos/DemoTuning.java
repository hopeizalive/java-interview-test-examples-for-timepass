package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DemoTuning {

    private DemoTuning() {}

    public static void l51(StudyContext ctx) {
        ctx.log("corePoolSize vs maximumPoolSize: threads grow above core only when queue is full (for typical pools).");
        var pool = new ThreadPoolExecutor(2, 4, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        ctx.log("  core=" + pool.getCorePoolSize() + " max=" + pool.getMaximumPoolSize());
        pool.shutdown();
    }

    public static void l52(StudyContext ctx) {
        ctx.log("LinkedBlockingQueue default unbounded capacity: can grow without bound — OOM risk under spikes.");
        var pool = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        ctx.log("  queue class: " + pool.getQueue().getClass().getSimpleName());
        pool.shutdown();
    }

    public static void l53(StudyContext ctx) {
        ctx.log("DiscardOldestPolicy vs DiscardPolicy: trade fairness for survival under overload.");
        ctx.log("  interview: prefer explicit CallerRuns, bounded queue, or rate limiting in APIs.");
    }

    public static void l54(StudyContext ctx) {
        ctx.log("Tuning recap: measure; bound queues; match pool size to CPU/IO; use semaphores for downstream limits.");
        ctx.log("  CPU-bound ~ cores; IO-bound higher; always handle RejectedExecutionException or use backpressure policies.");
    }
}
