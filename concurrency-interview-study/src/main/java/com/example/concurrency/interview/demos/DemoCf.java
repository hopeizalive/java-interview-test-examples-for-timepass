package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture composition and error-handling patterns (lessons 36-42).
 */
public final class DemoCf {

    private DemoCf() {}

    /** Lesson 36: supplyAsync default executor behavior (common pool). */
    public static void l36(StudyContext ctx) throws Exception {
        ctx.log("CompletableFuture.supplyAsync: default ForkJoinPool.commonPool() as executor.");
        // Execution story: create one async supplier, then block once to show completion value.
        CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> "async-" + Thread.currentThread().getName());
        ctx.log("  " + f.get());
    }

    /** Lesson 37: using a dedicated executor for async stages. */
    public static void l37(StudyContext ctx) throws Exception {
        ctx.log("Custom executor: isolate CPU work from common pool / set thread names.");
        // Execution story: both stages are pinned to the same dedicated pool for isolation.
        try (var ex = Executors.newFixedThreadPool(2, r -> new Thread(r, "cf-pool"))) {
            CompletableFuture<Integer> f = CompletableFuture.supplyAsync(() -> 21, ex).thenApplyAsync(x -> x * 2, ex);
            ctx.log("  " + f.get());
        }
    }

    /** Lesson 38: thenCompose for dependent async chaining. */
    public static void l38(StudyContext ctx) throws Exception {
        ctx.log("thenCompose: async flatMap — avoid nested CompletableFuture.");
        // Execution story: second async call depends on first result and stays in one flattened pipeline.
        CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> "user-")
                .thenCompose(prefix -> CompletableFuture.supplyAsync(() -> prefix + "42"));
        ctx.log("  " + f.get());
    }

    /** Lesson 39: thenCombine for independent async branches. */
    public static void l39(StudyContext ctx) throws Exception {
        ctx.log("thenCombine: when both async values are ready, merge them.");
        // Execution story: run branches independently, then combine once both are complete.
        CompletableFuture<Integer> a = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> b = CompletableFuture.supplyAsync(() -> 32);
        CompletableFuture<String> c = a.thenCombine(b, (x, y) -> "sum=" + (x + y));
        ctx.log("  " + c.get());
    }

    /** Lesson 40: coordination with anyOf vs allOf. */
    public static void l40(StudyContext ctx) throws Exception {
        ctx.log("allOf: wait for every future; anyOf: first completion wins.");
        // Execution story: read early result with anyOf, then explicitly wait for full completion with allOf.
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "slow";
        });
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "fast");
        CompletableFuture<Object> any = CompletableFuture.anyOf(f1, f2);
        ctx.log("  anyOf: " + any.get());
        CompletableFuture.allOf(f1, f2).join();
        ctx.log("  allOf done");
    }

    /** Lesson 41: exception recovery with exceptionally and handle. */
    public static void l41(StudyContext ctx) {
        ctx.log("exceptionally / handle: recover or map CompletionException.");
        // Execution story: first pipeline recovers with exceptionally, second maps both success/failure via handle.
        CompletableFuture<Integer> bad = CompletableFuture.supplyAsync(() -> {
                    if (true) {
                        throw new IllegalStateException("boom");
                    }
                    return 1;
                })
                .exceptionally(ex -> {
                    ctx.log("  recovered from: " + ex.getClass().getSimpleName());
                    return -1;
                });
        ctx.log("  value: " + bad.join());

        CompletableFuture<String> h = CompletableFuture.<String>supplyAsync(() -> {
                    throw new RuntimeException("x");
                })
                .handle((v, ex) -> ex != null ? "fallback" : v);
        ctx.log("  handle: " + h.join());
    }

    /** Lesson 42: exception shape differences between get() and join(). */
    public static void l42(StudyContext ctx) throws Exception {
        ctx.log("get() vs join(): get throws checked ExecutionException; join wraps in unchecked.");
        // Execution story: show success via join first, then force exceptional path and inspect checked wrapper.
        CompletableFuture<String> f = CompletableFuture.completedFuture("ok");
        ctx.log("  join: " + f.join());
        try {
            CompletableFuture<String> g = new CompletableFuture<>();
            g.completeExceptionally(new IllegalArgumentException("no"));
            ctx.log("  get: " + g.get());
        } catch (ExecutionException e) {
            ctx.log("  get saw: " + e.getCause().getClass().getSimpleName());
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
