package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link ConcurrentHashMap} vs {@link Collections#synchronizedMap} for concurrent read traffic.
 *
 * <p><b>Lessons:</b> 6.
 */
public final class DemoConcurrencyMaps {

    private DemoConcurrencyMaps() {}

    /**
     * Runs many concurrent {@code get} operations against {@link ConcurrentHashMap} vs synchronized {@link HashMap}.
     *
     * <p><b>Purpose:</b> illustrate striped vs single-lock read scalability at a high level.
     *
     * <p><b>Role:</b> same thread count and per-thread read count for both maps (pre-populated identical keys).
     *
     * <p><b>Demonstration:</b> {@link ConcurrentHashMap} allows lock-free reads in common cases; synchronized map
     * serializes all operations on one monitor—interview: “read-mostly caches prefer CHM”.
     */
    public static void l06(StudyContext ctx) throws InterruptedException {
        ctx.log("Interview question: ConcurrentHashMap vs Collections.synchronizedMap for read-heavy workloads?");
        final int threads = 8;
        final int readsPerThread = 200_000;
        final int keySpace = 10_000;
        Map<Integer, Integer> chm = new ConcurrentHashMap<>();
        Map<Integer, Integer> sync = Collections.synchronizedMap(new HashMap<>());
        for (int i = 0; i < keySpace; i++) {
            chm.put(i, i);
            sync.put(i, i);
        }
        long tChm = runConcurrentGets(chm, threads, readsPerThread, keySpace);
        long tSync = runConcurrentGets(sync, threads, readsPerThread, keySpace);
        ctx.log("Total wall time (" + threads + " threads × " + readsPerThread + " gets each):");
        DemoSupport.logNanos(ctx, "CHM", tChm);
        DemoSupport.logNanos(ctx, "sync map", tSync);
        ctx.log("Iterator note: CHM’s iterators are weakly consistent; synchronizedMap’s iterator still requires");
        ctx.log("external synchronization if the map may change structurally during iteration (per Javadoc).");
    }

    /**
     * Spawns {@code threadCount} workers that read random keys from {@code map}.
     *
     * <p><b>Purpose:</b> isolate the concurrent read pattern for timing.
     *
     * <p><b>Role:</b> shared harness for two different map implementations in {@link #l06}.
     *
     * <p><b>Demonstration:</b> shows throughput difference under contention (directional; not a formal benchmark).
     */
    private static long runConcurrentGets(Map<Integer, Integer> map, int threadCount, int readsPerThread, int keySpace)
            throws InterruptedException {
        AtomicInteger sink = new AtomicInteger();
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        long t0 = System.nanoTime();
        for (int t = 0; t < threadCount; t++) {
            final int seed = t;
            pool.submit(() -> {
                try {
                    start.await();
                    int acc = 0;
                    for (int i = 0; i < readsPerThread; i++) {
                        int key = (seed * 31 + i) % keySpace;
                        acc += map.get(key);
                    }
                    sink.addAndGet(acc);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        start.countDown();
        done.await();
        long elapsed = System.nanoTime() - t0;
        pool.shutdown();
        if (!pool.awaitTermination(2, TimeUnit.MINUTES)) {
            pool.shutdownNow();
        }
        if (sink.get() == Integer.MIN_VALUE) {
            throw new AssertionError("unreachable");
        }
        return elapsed;
    }
}
