package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Thread lifecycle and cancellation fundamentals.
 *
 * <p>Serves lessons 1-3 with small, observable examples that can be explained in interviews.
 */
public final class DemoThreads {

    private DemoThreads() {}

    /**
     * Lesson 1: starts a plain {@link Thread}, waits with {@link Thread#join()}, and prints state transitions.
     *
     * <p><b>Purpose:</b> show the difference between constructing, starting, and joining a thread.
     * <p><b>Role:</b> baseline thread lifecycle demo before introducing executors/futures.
     * <p><b>Demonstration:</b> worker runs on a separate thread, and {@code join()} provides completion visibility.
     */
    public static void l01(StudyContext ctx) throws Exception {
        ctx.log("Runnable on a new Thread: start → runnable → terminated after run completes.");
        var done = new boolean[] {false};
        // Execution story: build thread first, inspect NEW state, then start and join for deterministic completion.
        Thread t = new Thread(() -> {
            ctx.log("  worker: " + Thread.currentThread().getName());
            done[0] = true;
        }, "lesson01-worker");
        ctx.log("  state before start: " + t.getState());
        t.start();
        t.join();
        ctx.log("  state after join: " + t.getState());
        ctx.log("  done flag: " + done[0]);
    }

    /**
     * Lesson 2: executes {@link Callable} work with {@link Future} results.
     *
     * <p><b>Purpose:</b> show typed return values and checked-exception boundary at {@code Future#get()}.
     * <p><b>Role:</b> bridges from raw threads to task-oriented APIs.
     * <p><b>Demonstration:</b> two submitted callables return values from executor-managed worker threads.
     */
    public static void l02(StudyContext ctx) throws Exception {
        ctx.log("Callable + Future: typed result and checked exceptions from the task.");
        try (var ex = Executors.newSingleThreadExecutor()) {
            // Execution story: submit twice to same executor so both results come through Future.get().
            Callable<String> task = () -> "answer-" + Thread.currentThread().getName();
            Future<String> f = ex.submit(task);
            Future<String> f2 = ex.submit(task);
            ctx.log("  get(): " + f.get());
            ctx.log("  get2(): " + f2.get());
        }
    }

    /**
     * Lesson 3: cancels a running task with interruption.
     *
     * <p><b>Purpose:</b> explain cooperative cancellation via {@code Future.cancel(true)}.
     * <p><b>Role:</b> teaches how interruption is observed inside blocking code.
     * <p><b>Demonstration:</b> cancelled future reports {@link CancellationException}; task handles
     * {@link InterruptedException} and restores interrupt status.
     */
    public static void l03(StudyContext ctx) {
        ctx.log("Future.cancel(mayInterruptIfRunning): stop work that supports interruption.");
        try (var ex = Executors.newSingleThreadExecutor()) {
            // Execution story step 1: start blocking task.
            Future<?> f = ex.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ctx.log("  task saw interrupt and restored interrupt flag");
                }
                return null;
            });
            // Story step 2: cancel with interruption request.
            boolean cancelled = f.cancel(true);
            ctx.log("  cancel returned: " + cancelled);
            try {
                // Story step 3: cancelled future no longer yields a value; get() signals cancellation.
                f.get();
            } catch (CancellationException e) {
                ctx.log("  get() after cancel: CancellationException (expected)");
            } catch (ExecutionException | InterruptedException e) {
                ctx.log("  unexpected: " + e);
            }
        }
    }
}
