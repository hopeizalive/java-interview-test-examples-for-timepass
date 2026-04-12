package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class DemoBackpressure {

    private DemoBackpressure() {}

    public static void l47(StudyContext ctx) {
        ctx.log("AbortPolicy: reject when queue is full — caller must handle RejectedExecutionException.");
        var pool = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(2),
                new ThreadPoolExecutor.AbortPolicy());
        try {
            pool.submit(() -> sleep(500));
            pool.submit(() -> sleep(500));
            pool.submit(() -> sleep(500));
            pool.submit(() -> sleep(500));
            ctx.log("  unexpected: no reject");
        } catch (RejectedExecutionException e) {
            ctx.log("  rejected as expected when queue saturated");
        } finally {
            pool.shutdownNow();
        }
    }

    public static void l48(StudyContext ctx) throws Exception {
        ctx.log("CallerRunsPolicy: submitter thread runs the task — natural backpressure on producers.");
        var pool = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(2),
                new ThreadPoolExecutor.CallerRunsPolicy());
        try (var self = Executors.newSingleThreadExecutor()) {
            self.submit(() -> {
                for (int i = 0; i < 6; i++) {
                    int n = i;
                    pool.submit(() -> {
                        ctx.log("  task " + n + " on " + Thread.currentThread().getName());
                        sleep(50);
                    });
                }
            }).get();
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void l49(StudyContext ctx) throws Exception {
        ctx.log("Semaphore: cap concurrent in-flight work (limit overload on downstream).");
        var sem = new Semaphore(2);
        try (var ex = Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < 6; i++) {
                int n = i;
                ex.submit(() -> {
                    sem.acquireUninterruptibly();
                    try {
                        ctx.log("  running " + n);
                        sleep(30);
                    } finally {
                        sem.release();
                    }
                });
            }
        }
    }

    public static void l50(StudyContext ctx) throws Exception {
        ctx.log("SynchronousQueue: handoff — no capacity; pairs producer thread with consumer thread.");
        var pool = new ThreadPoolExecutor(
                2,
                2,
                0L,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.AbortPolicy());
        try {
            for (int i = 0; i < 4; i++) {
                int n = i;
                try {
                    pool.submit(() -> ctx.log("  work " + n));
                } catch (RejectedExecutionException e) {
                    ctx.log("  submit " + n + " rejected — SynchronousQueue has no buffer; only idle threads accept handoff.");
                }
            }
        } finally {
            pool.shutdown();
            pool.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    private static void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
