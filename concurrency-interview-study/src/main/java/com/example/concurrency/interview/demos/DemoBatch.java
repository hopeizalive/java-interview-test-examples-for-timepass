package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Batch and throughput patterns (lessons 27-31).
 */
public final class DemoBatch {

    private DemoBatch() {}

    private static final int RECORDS = 1200;

    /** Lesson 27: fixed-pool processing of many independent records. */
    public static void l27(StudyContext ctx) throws Exception {
        ctx.log("Batch: process " + RECORDS + " records with a fixed pool (typical worker-queue pattern).");
        // Execution story: submit one task per record, then wait for every Future as a completion barrier.
        long t0 = System.nanoTime();
        try (var pool = Executors.newFixedThreadPool(8)) {
            var futures = new ArrayList<Future<String>>();
            for (int i = 0; i < RECORDS; i++) {
                int id = i;
                futures.add(pool.submit(() -> "ok-" + id));
            }
            int ok = 0;
            for (Future<String> f : futures) {
                ok += f.get().startsWith("ok") ? 1 : 0;
            }
            ctx.log("  processed: " + ok);
        }
        ctx.log("  elapsed ms: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0));
    }

    /** Lesson 28: chunking work to reduce scheduling overhead. */
    public static void l28(StudyContext ctx) throws Exception {
        ctx.log("Chunking: slice IDs into batches per task to reduce task submission overhead.");
        int chunk = 200;
        // Execution story: fewer coarse-grained tasks reduce scheduler pressure vs 1200 tiny tasks.
        long t0 = System.nanoTime();
        try (var pool = Executors.newFixedThreadPool(4)) {
            List<Callable<Integer>> tasks = new ArrayList<>();
            for (int start = 0; start < RECORDS; start += chunk) {
                int s = start;
                int e = Math.min(start + chunk, RECORDS);
                tasks.add(() -> {
                    int n = 0;
                    for (int i = s; i < e; i++) {
                        n++;
                    }
                    return n;
                });
            }
            int total = 0;
            for (Future<Integer> f : pool.invokeAll(tasks)) {
                total += f.get();
            }
            ctx.log("  records covered: " + total);
        }
        ctx.log("  elapsed ms: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0));
    }

    /** Lesson 29: invokeAll as an explicit batch completion barrier. */
    public static void l29(StudyContext ctx) throws Exception {
        ctx.log("invokeAll on a batch of Callables: barrier after the whole batch completes.");
        // Execution story: construct deterministic batch, invoke all at once, aggregate after full completion.
        List<Callable<Integer>> batch = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int n = i;
            batch.add(() -> n * n);
        }
        try (var pool = Executors.newFixedThreadPool(10)) {
            var results = pool.invokeAll(batch);
            int sum = 0;
            for (var f : results) {
                sum += f.get();
            }
            ctx.log("  sum of squares 0..49: " + sum);
        }
    }

    /** Lesson 30: simple throughput comparison across pool sizes. */
    public static void l30(StudyContext ctx) throws Exception {
        ctx.log("Throughput: pool size vs task count — measure before tuning in production.");
        // Execution story: run same synthetic CPU workload with different pool sizes and compare elapsed time.
        for (int threads : List.of(2, 8, 32)) {
            long t0 = System.nanoTime();
            try (var pool = Executors.newFixedThreadPool(threads)) {
                var futures = new ArrayList<Future<?>>();
                for (int i = 0; i < 500; i++) {
                    futures.add(pool.submit(() -> {
                        long x = 0;
                        for (int j = 0; j < 5000; j++) {
                            x += j % 7;
                        }
                        return x;
                    }));
                }
                for (var f : futures) {
                    f.get();
                }
            }
            long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0);
            ctx.log("  threads=" + threads + " elapsedMs=" + ms);
        }
    }

    /** Lesson 31: bridge from batched tasks to CompletableFuture.allOf composition. */
    public static void l31(StudyContext ctx) throws Exception {
        ctx.log("Batch + CompletableFuture.allOf: async stages then join (see also CF lessons).");
        // Execution story: fan-out async stages first, then single join point via allOf for fan-in.
        try (var pool = Executors.newFixedThreadPool(6)) {
            var cfs = new java.util.concurrent.CompletableFuture<?>[10];
            for (int i = 0; i < cfs.length; i++) {
                int n = i;
                cfs[i] = java.util.concurrent.CompletableFuture.supplyAsync(() -> n * 10, pool);
            }
            java.util.concurrent.CompletableFuture.allOf(cfs).join();
            ctx.log("  all " + cfs.length + " stages completed");
        }
    }
}
