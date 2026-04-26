package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Executor and scheduling patterns for lessons 4-12.
 *
 * <p>Each method isolates one API decision so readers can map code behavior to interview trade-offs.
 */
public final class DemoExecutors {

    private DemoExecutors() {}

    /**
     * Lesson 4: fixed-size worker pool.
     *
     * <p><b>Purpose:</b> show bounded worker count with queue-backed execution.
     * <p><b>Role:</b> first executor sizing example.
     * <p><b>Demonstration:</b> eight tasks run on four reused worker threads.
     */
    public static void l04(StudyContext ctx) throws Exception {
        ctx.log("newFixedThreadPool(4): bounded workers, unbounded queue — watch memory if tasks pile up.");
        try (var ex = Executors.newFixedThreadPool(4)) {
            // Story: submit more tasks than workers; executor reuses 4 threads and queues the rest.
            for (int i = 0; i < 8; i++) {
                int n = i;
                ex.submit(() -> ctx.log("  task " + n + " on " + Thread.currentThread().getName()));
            }
        }
    }

    /**
     * Lesson 5: cached pool behavior.
     *
     * <p><b>Purpose:</b> demonstrate elastic pool for short-lived bursts.
     * <p><b>Role:</b> contrast against fixed pool semantics from lesson 4.
     * <p><b>Demonstration:</b> callables run immediately with dynamically provisioned/reused workers.
     */
    public static void l05(StudyContext ctx) throws Exception {
        ctx.log("newCachedThreadPool: grows/shrinks; good for many short async tasks.");
        try (var ex = Executors.newCachedThreadPool()) {
            // Story: two tiny callables run quickly; cached pool may create/reuse workers on demand.
            Callable<Object> objectCallable = () -> {
                ctx.log("  a");
                return null;
            };
            ex.invokeAll(List.of(objectCallable, () -> {
                ctx.log("  b");
                return null;
            }));
        }
    }

    /**
     * Lesson 6: single-thread executor ordering.
     *
     * <p><b>Purpose:</b> show serialized task execution off the caller thread.
     * <p><b>Role:</b> illustrates strict ordering guarantees without explicit locking.
     * <p><b>Demonstration:</b> submitted steps run one at a time on the same worker.
     */
    public static void l06(StudyContext ctx) throws Exception {
        ctx.log("newSingleThreadExecutor: FIFO order guaranteed for submitted tasks.");
        try (var ex = Executors.newSingleThreadExecutor()) {
            for (int i = 0; i < 3; i++) {
                int n = i;
                ex.submit(() -> ctx.log("  step " + n));
            }
        }
    }

    /**
     * Lesson 7: one-shot delayed scheduling.
     *
     * <p><b>Purpose:</b> introduce {@code ScheduledExecutorService.schedule}.
     * <p><b>Role:</b> foundation for recurring schedule behavior in lesson 8.
     * <p><b>Demonstration:</b> delayed task fires once after the configured delay.
     */
    public static void l07(StudyContext ctx) throws Exception {
        ctx.log("ScheduledExecutorService.schedule: run once after delay.");
        try (var ex = Executors.newSingleThreadScheduledExecutor()) {
            var latch = new java.util.concurrent.CountDownLatch(1);
            // Story: schedule one delayed task, then block caller until that task signals completion.
            ex.schedule(
                    () -> {
                        ctx.log("  delayed task");
                        latch.countDown();
                    },
                    200,
                    TimeUnit.MILLISECONDS);
            latch.await();
        }
    }

    /**
     * Lesson 8: fixed-rate recurring scheduling.
     *
     * <p><b>Purpose:</b> show cadence-driven execution.
     * <p><b>Role:</b> explains periodic task lifecycle and cancellation.
     * <p><b>Demonstration:</b> repeated ticks continue until the returned future is cancelled.
     */
    public static void l08(StudyContext ctx) throws Exception {
        ctx.log("scheduleAtFixedRate: fixed cadence (can overlap if task runs longer than period).");
        try (var ex = Executors.newSingleThreadScheduledExecutor()) {
            var count = new AtomicInteger();
            // Story step 1: recurring task increments tick counter every 100ms.
            var future = ex.scheduleAtFixedRate(() -> {
                int c = count.incrementAndGet();
                ctx.log("  tick " + c);
            }, 0, 100, TimeUnit.MILLISECONDS);
            // Story step 2: after a short observation window, caller cancels periodic work.
            Thread.sleep(350);
            future.cancel(false);
        }
    }

    /**
     * Lesson 9: common pool and parallel streams.
     *
     * <p><b>Purpose:</b> connect {@code parallelStream()} to {@code ForkJoinPool.commonPool()}.
     * <p><b>Role:</b> warns about shared-pool contention in real systems.
     * <p><b>Demonstration:</b> prints sum and current common-pool parallelism.
     */
    public static void l09(StudyContext ctx) {
        ctx.log("ForkJoinPool.commonPool(): work-stealing for parallel streams (do not shut down the common pool).");
        Integer sum = List.of(1, 2, 3, 4).parallelStream().reduce(0, Integer::sum);
        ctx.log("  parallel stream sum: " + sum);
        ctx.log("  commonPool parallelism: " + ForkJoinPool.commonPool().getParallelism());
    }

    /**
     * Lesson 10: custom {@link ThreadFactory}.
     *
     * <p><b>Purpose:</b> show explicit thread naming for observability.
     * <p><b>Role:</b> demonstrates customization point when building executors.
     * <p><b>Demonstration:</b> task logs run on named worker threads.
     */
    public static void l10(StudyContext ctx) throws Exception {
        ctx.log("Custom ThreadFactory: name threads for logs and profilers.");
        ThreadFactory tf = r -> {
            Thread t = new Thread(r);
            t.setName("study-worker-" + System.nanoTime());
            return t;
        };
        try (var ex = Executors.newFixedThreadPool(2, tf)) {
            ex.submit(() -> ctx.log("  hello from " + Thread.currentThread().getName())).get();
        }
    }

    /**
     * Lesson 11: {@code invokeAll} batch barrier.
     *
     * <p><b>Purpose:</b> run many callables and wait for every result.
     * <p><b>Role:</b> structured batch completion pattern.
     * <p><b>Demonstration:</b> all futures complete before result aggregation.
     */
    public static void l11(StudyContext ctx) throws Exception {
        ctx.log("invokeAll: submit a batch of Callables and wait for all to complete.");
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            int n = i;
            tasks.add(() -> {
                System.out.println(n+" this is value of loop with thread name --> "+Thread.currentThread().getName());
                return "v" + n;
            });
        }
        try (var ex = Executors.newFixedThreadPool(3)) {
            // Story: invokeAll blocks until every callable in the batch reaches terminal state.
            var futures = ex.invokeAll(tasks);
            System.out.println("now we can see there results");
            for (var f : futures) {
                ctx.log(Thread.currentThread().getName()+"  " + f.get());
            }
        }
    }

    /**
     * Lesson 12: {@code invokeAny} fastest-success semantics.
     *
     * <p><b>Purpose:</b> demonstrate race-to-first-success use case.
     * <p><b>Role:</b> contrasts with lesson 11's all-results barrier.
     * <p><b>Demonstration:</b> quicker callable wins and becomes the returned value.
     */
    public static void l12(StudyContext ctx) throws Exception {
        ctx.log("invokeAny: first successful result wins; others may be cancelled.");
        try (var ex = Executors.newFixedThreadPool(4)) {
            // Story: "fast" should win race against delayed "slow" callable.
            String winner = ex.invokeAny(List.of(
                    () -> {
                        Thread.sleep(200);
                        return "slow";
                    },
                    () -> "fast"));
            ctx.log("  winner: " + winner);
        } catch (ExecutionException e) {
            ctx.log("  error: " + e.getCause());
        }
    }
}
