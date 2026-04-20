package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * {@link CopyOnWriteArrayList} vs synchronized {@link ArrayList} under read-heavy concurrency.
 *
 * <p><b>Lessons:</b> 18.
 */
public final class DemoCopyOnWriteCompare {

    private DemoCopyOnWriteCompare() {}

    /**
     * Runs concurrent readers only on {@link CopyOnWriteArrayList} vs {@link Collections#synchronizedList}.
     *
     * <p><b>Purpose:</b> illustrate copy-on-write snapshot reads vs reader/writer lock contention.
     *
     * <p><b>Role:</b> same thread pattern for both list implementations.
     *
     * <p><b>Demonstration:</b> under pure concurrent {@code get()}, COWAL avoids the synchronized list’s per-call
     * monitor; frequent writes elsewhere would flip the trade-off (each COW mutation copies the backing array).
     */
    public static void l18(StudyContext ctx) throws InterruptedException {
        ctx.log("Interview question: CopyOnWriteArrayList vs synchronized ArrayList for many readers?");
        final int readers = 10;
        final int readsEach = 50_000;
        final int size = 100;
        List<Integer> cow = new CopyOnWriteArrayList<>();
        List<Integer> sync = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < size; i++) {
            cow.add(i);
            sync.add(i);
        }
        /*
         * Pure concurrent reads: synchronizedList serializes each get on one monitor; CopyOnWriteArrayList reads the
         * current backing array reference without locking.
         */
        long tCow = runReadersOnly(cow, readers, readsEach);
        long tSync = runReadersOnly(sync, readers, readsEach);
        ctx.log("Concurrent get() only (" + readers + " threads × " + readsEach + " reads):");
        DemoSupport.logNanos(ctx, "CopyOnWriteArrayList", tCow);
        DemoSupport.logNanos(ctx, "synchronized ArrayList", tSync);
        ctx.log("COW iterator uses snapshot; each add/set copies the whole backing array — avoid write-heavy use.");
    }

    /**
     * Spawns reader threads hammering {@code get} with no structural changes during the run.
     *
     * <p><b>Purpose:</b> isolate read scalability for two list implementations.
     *
     * <p><b>Role:</b> used twice in {@link #l18}.
     *
     * <p><b>Demonstration:</b> lock-free reads vs per-get monitor acquisition.
     */
    private static long runReadersOnly(List<Integer> list, int readers, int readsEach) throws InterruptedException {
        AtomicInteger sink = new AtomicInteger();
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(readers);
        ExecutorService pool = Executors.newFixedThreadPool(readers);
        long t0 = System.nanoTime();
        for (int r = 0; r < readers; r++) {
            pool.submit(() -> {
                try {
                    start.await();
                    int acc = 0;
                    for (int i = 0; i < readsEach; i++) {
                        acc += list.get(i % list.size());
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
            throw new AssertionError();
        }
        return elapsed;
    }
}
