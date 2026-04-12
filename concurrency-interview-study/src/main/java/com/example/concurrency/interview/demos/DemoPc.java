package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public final class DemoPc {

    private DemoPc() {}

    public static void l32(StudyContext ctx) throws Exception {
        ctx.log("Producer–consumer with ArrayBlockingQueue: bounded buffer = natural backpressure.");
        BlockingQueue<Integer> q = new ArrayBlockingQueue<>(5);
        var producer = Thread.ofPlatform().start(() -> {
            try {
                for (int i = 0; i < 12; i++) {
                    q.put(i);
                    ctx.log("  produced " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        for (int i = 0; i < 12; i++) {
            ctx.log("  consumed " + q.take());
        }
        producer.join();
    }

    public static void l33(StudyContext ctx) throws Exception {
        ctx.log("LinkedBlockingQueue: optionally bounded; higher throughput for some workloads.");
        BlockingQueue<String> q = new LinkedBlockingQueue<>(100);
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(2)) {
            ex.submit(() -> {
                for (int i = 0; i < 8; i++) {
                    q.offer("m" + i, 1, TimeUnit.SECONDS);
                }
                return null;
            });
            ex.submit(() -> {
                List<String> got = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    got.add(q.poll(2, TimeUnit.SECONDS));
                }
                ctx.log("  batch: " + got);
                return null;
            }).get();
        }
    }

    public static void l34(StudyContext ctx) throws Exception {
        ctx.log("Poison pill: sentinel value tells consumers to exit (graceful shutdown pattern).");
        BlockingQueue<String> q = new LinkedBlockingQueue<>();
        final String POISON = "__POISON__";
        var consumer = Thread.ofPlatform().start(() -> {
            try {
                while (true) {
                    String m = q.take();
                    if (POISON.equals(m)) {
                        ctx.log("  consumer stops");
                        break;
                    }
                    ctx.log("  got " + m);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        q.put("a");
        q.put("b");
        q.put(POISON);
        consumer.join();
    }

    public static void l35(StudyContext ctx) throws Exception {
        ctx.log("Multiple consumers compete on one queue — increase parallelism safely.");
        BlockingQueue<Integer> q = new LinkedBlockingQueue<>();
        int consumers = 3;
        var pool = java.util.concurrent.Executors.newFixedThreadPool(consumers);
        try {
            for (int c = 0; c < consumers; c++) {
                int id = c;
                pool.submit(() -> {
                    try {
                        for (int k = 0; k < 4; k++) {
                            Integer v = q.take();
                            ctx.log("  C" + id + " took " + v);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            for (int i = 0; i < 12; i++) {
                q.put(i);
            }
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
