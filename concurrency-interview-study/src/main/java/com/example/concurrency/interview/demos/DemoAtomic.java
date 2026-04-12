package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public final class DemoAtomic {

    private DemoAtomic() {}

    public static void l19(StudyContext ctx) throws Exception {
        ctx.log("AtomicInteger: lock-free CAS increments; compareAndSet for conditional updates.");
        var n = new AtomicInteger();
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < 10_000; i++) {
                ex.submit(() -> n.incrementAndGet());
            }
        }
        ctx.log("  result: " + n.get());
    }

    public static void l20(StudyContext ctx) throws Exception {
        ctx.log("LongAdder: sharded accumulation — often better than AtomicLong under high contention.");
        var adder = new LongAdder();
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < 50_000; i++) {
                ex.submit(adder::increment);
            }
        }
        ctx.log("  sum: " + adder.sum());
    }

    public static void l21(StudyContext ctx) throws InterruptedException {
        ctx.log("Without volatile, publishing a flag has no happens-before guarantee (reordering / visibility).");
        ctx.log("Interview: readers may never observe the write or see stale field values — fix with volatile or sync.");
        class Holder {
            boolean ready;
            int value;
        }
        var h = new Holder();
        var t = new Thread(() -> {
            h.value = 42;
            h.ready = true;
        });
        t.start();
        t.join();
        ctx.log("  after join (happens-before via join): ready=" + h.ready + " value=" + h.value);
    }

    public static void l22(StudyContext ctx) throws Exception {
        ctx.log("volatile: visibility + sequential consistency for that field; pairs with one writer pattern.");
        class Holder {
            volatile boolean stop;
        }
        var h = new Holder();
        try (var ex = java.util.concurrent.Executors.newSingleThreadExecutor()) {
            var f = ex.submit(() -> {
                while (!h.stop) {
                    Thread.onSpinWait();
                }
                return "stopped";
            });
            Thread.sleep(50);
            h.stop = true;
            ctx.log("  worker: " + f.get());
        }
    }
}
