package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

/**
 * JDK 21+ structured concurrency: {@link StructuredTaskScope} ties child tasks to a scope so
 * completion, failure, and cancellation form a single unit of work (unlike fire-and-forget threads).
 */
public final class DemoStructuredConcurrency {

    private DemoStructuredConcurrency() {}

    public static void l62(StudyContext ctx) throws Exception {
        ctx.log("StructuredTaskScope.ShutdownOnFailure: fork subtasks, join() as one deadline, throwIfFailed().");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var a = scope.fork(() -> delayedValue(40, 7));
            var b = scope.fork(() -> delayedValue(15, 5));
            scope.join();
            scope.throwIfFailed();
            ctx.log("  success path: 7 + 5 = " + (a.get() + b.get()));
        }

        ctx.log("Same scope: one fork fails → ShutdownOnFailure interrupts the other fork (no orphan work).");
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> {
                try {
                    Thread.sleep(300);
                    ctx.log("  slow fork completed (unexpected if fast fork failed first)");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ctx.log("  slow fork interrupted because peer failed (structured cancel)");
                    throw e;
                }
                return null;
            });
            scope.fork(() -> {
                Thread.sleep(20);
                throw new IllegalStateException("downstream unavailable");
            });
            scope.join();
            try {
                scope.throwIfFailed();
            } catch (ExecutionException ex) {
                ctx.log("  failure as one unit: " + ex.getCause().getMessage());
            }
        }
    }

    private static int delayedValue(int delayMs, int value) throws InterruptedException {
        Thread.sleep(delayMs);
        return value;
    }
}
