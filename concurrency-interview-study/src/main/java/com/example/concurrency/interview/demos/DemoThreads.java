package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class DemoThreads {

    private DemoThreads() {}

    public static void l01(StudyContext ctx) throws Exception {
        ctx.log("Runnable on a new Thread: start → runnable → terminated after run completes.");
        var done = new boolean[] {false};
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

    public static void l02(StudyContext ctx) throws Exception {
        ctx.log("Callable + Future: typed result and checked exceptions from the task.");
        try (var ex = Executors.newSingleThreadExecutor()) {
            Callable<String> task = () -> "answer-" + Thread.currentThread().getName();
            Future<String> f = ex.submit(task);
            ctx.log("  get(): " + f.get());
        }
    }

    public static void l03(StudyContext ctx) {
        ctx.log("Future.cancel(mayInterruptIfRunning): stop work that supports interruption.");
        try (var ex = Executors.newSingleThreadExecutor()) {
            Future<?> f = ex.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ctx.log("  task saw interrupt and restored interrupt flag");
                }
                return null;
            });
            boolean cancelled = f.cancel(true);
            ctx.log("  cancel returned: " + cancelled);
            try {
                f.get();
            } catch (CancellationException e) {
                ctx.log("  get() after cancel: CancellationException (expected)");
            } catch (ExecutionException | InterruptedException e) {
                ctx.log("  unexpected: " + e);
            }
        }
    }
}
