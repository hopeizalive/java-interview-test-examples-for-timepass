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

public final class DemoExecutors {

    private DemoExecutors() {}

    public static void l04(StudyContext ctx) throws Exception {
        ctx.log("newFixedThreadPool(4): bounded workers, unbounded queue — watch memory if tasks pile up.");
        try (var ex = Executors.newFixedThreadPool(4)) {
            for (int i = 0; i < 8; i++) {
                int n = i;
                ex.submit(() -> ctx.log("  task " + n + " on " + Thread.currentThread().getName()));
            }
        }
    }

    public static void l05(StudyContext ctx) throws Exception {
        ctx.log("newCachedThreadPool: grows/shrinks; good for many short async tasks.");
        try (var ex = Executors.newCachedThreadPool()) {


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

    public static void l06(StudyContext ctx) throws Exception {
        ctx.log("newSingleThreadExecutor: FIFO order guaranteed for submitted tasks.");
        try (var ex = Executors.newSingleThreadExecutor()) {
            for (int i = 0; i < 3; i++) {
                int n = i;
                ex.submit(() -> ctx.log("  step " + n));
            }
        }
    }

    public static void l07(StudyContext ctx) throws Exception {
        ctx.log("ScheduledExecutorService.schedule: run once after delay.");
        try (var ex = Executors.newSingleThreadScheduledExecutor()) {
            var latch = new java.util.concurrent.CountDownLatch(1);
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

    public static void l08(StudyContext ctx) throws Exception {
        ctx.log("scheduleAtFixedRate: fixed cadence (can overlap if task runs longer than period).");
        try (var ex = Executors.newSingleThreadScheduledExecutor()) {
            var count = new AtomicInteger();
            var future = ex.scheduleAtFixedRate(() -> {
                int c = count.incrementAndGet();
                ctx.log("  tick " + c);
            }, 0, 100, TimeUnit.MILLISECONDS);
            Thread.sleep(350);
            future.cancel(false);
        }
    }

    public static void l09(StudyContext ctx) {
        ctx.log("ForkJoinPool.commonPool(): work-stealing for parallel streams (do not shut down the common pool).");
        Integer sum = List.of(1, 2, 3, 4).parallelStream().reduce(0, Integer::sum);
        ctx.log("  parallel stream sum: " + sum);
        ctx.log("  commonPool parallelism: " + ForkJoinPool.commonPool().getParallelism());
    }

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

    public static void l11(StudyContext ctx) throws Exception {
        ctx.log("invokeAll: submit a batch of Callables and wait for all to complete.");
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            int n = i;
            tasks.add(() -> "v" + n);
        }
        try (var ex = Executors.newFixedThreadPool(3)) {
            var futures = ex.invokeAll(tasks);
            for (var f : futures) {
                ctx.log(Thread.currentThread().getName()+"  " + f.get());
            }
        }
    }

    public static void l12(StudyContext ctx) throws Exception {
        ctx.log("invokeAny: first successful result wins; others may be cancelled.");
        try (var ex = Executors.newFixedThreadPool(4)) {
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
