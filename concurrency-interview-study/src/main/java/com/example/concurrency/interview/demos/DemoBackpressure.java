package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Saturation and backpressure patterns (lessons 47-50).
 */
public final class DemoBackpressure {

    private DemoBackpressure() {}

    /**
     * Lesson 47: rejection with {@link ThreadPoolExecutor.AbortPolicy}.
     *
     * <p><b>Purpose:</b> show fail-fast behavior when bounded queue is full.
     * <p><b>Role:</b> introduces explicit overload handling path.
     * <p><b>Demonstration:</b> saturated pool throws {@link RejectedExecutionException}.
     */
    public static void l47(StudyContext ctx) {
        ctx.log("AbortPolicy: reject when queue is full — caller must handle RejectedExecutionException.");
        // Execution story: one worker + queue size 2 means the 4th submission should be rejected.
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

    /**
     * Lesson 48: {@link ThreadPoolExecutor.CallerRunsPolicy} for producer throttling.
     *
     * <p><b>Purpose:</b> demonstrate built-in backpressure by executing overflow work on submitter thread.
     * <p><b>Role:</b> contrast with hard rejection from lesson 47.
     * <p><b>Demonstration:</b> some tasks run on caller thread when worker+queue are saturated.
     */
    public static void l48(StudyContext ctx) throws Exception {
        ctx.log("CallerRunsPolicy: submitter thread runs the task — natural backpressure on producers.");
        // Execution story: saturated pool pushes overflow work back onto the submitting thread.
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

    /**
     * Lesson 49: concurrency cap with {@link Semaphore}.
     *
     * <p><b>Purpose:</b> bound in-flight work independently of thread-pool size.
     * <p><b>Role:</b> protects downstream resources (DB/API) from overload.
     * <p><b>Demonstration:</b> only two tasks enter critical section simultaneously.
     */
    public static void l49(StudyContext ctx) throws Exception {
        ctx.log("Semaphore: cap concurrent in-flight work (limit overload on downstream).");
        // Execution story: six tasks start, but only two hold permits and run at any moment.
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

    /**
     * Lesson 50: direct handoff with {@link SynchronousQueue}.
     *
     * <p><b>Purpose:</b> show zero-capacity queue semantics.
     * <p><b>Role:</b> explains why cached-pool style handoff can reject if no idle worker accepts transfer.
     * <p><b>Demonstration:</b> submissions may be rejected because queue cannot buffer.
     */
    public static void l50(StudyContext ctx) throws Exception {
        ctx.log("SynchronousQueue: handoff — no capacity; pairs producer thread with consumer thread.");
        // Execution story: submissions only succeed when an idle worker can accept direct handoff immediately.
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
