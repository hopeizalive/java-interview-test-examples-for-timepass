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

    private static final int L61_CAPACITY = 3;
    private static final int L61_ITEMS = 8;

    /**
     * Same bounded producer–consumer scenario twice: (1) explicit {@code wait}/{@code notifyAll} on a ring buffer,
     * (2) {@link BlockingQueue} — {@link ArrayBlockingQueue#put} / {@link BlockingQueue#take} encode the same
     * full/empty conditions (blocking when full or empty).
     */
    public static void l61(StudyContext ctx) throws Exception {
        ctx.log("Producer–consumer problem — phase 1: hand-rolled buffer, wait() / notifyAll().");
        runProducerConsumerWaitNotify(ctx);

        ctx.log("Producer–consumer problem — phase 2: same bounds via BlockingQueue (ArrayBlockingQueue).");
        runProducerConsumerBlockingQueue(ctx);

        ctx.log("Done — BlockingQueue.put/take are the standard library expression of the same monitor logic.");
    }

    private static void runProducerConsumerWaitNotify(StudyContext ctx) throws InterruptedException {
        final Object lock = new Object();
        final int[] slots = new int[L61_CAPACITY];
        final int[] count = {0};
        final int[] putIdx = {0};
        final int[] takeIdx = {0};

        Runnable put = () -> {
            try {
                for (int item = 0; item < L61_ITEMS; item++) {
                    synchronized (lock) {
                        while (count[0] == L61_CAPACITY) {
                            ctx.log("  [monitor] buffer full → producer waits");
                            lock.wait();
                        }
                        slots[putIdx[0]] = item;
                        putIdx[0] = (putIdx[0] + 1) % L61_CAPACITY;
                        count[0]++;
                        ctx.log("  [monitor] put " + item + " (count=" + count[0] + ")");
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable take = () -> {
            try {
                for (int n = 0; n < L61_ITEMS; n++) {
                    synchronized (lock) {
                        while (count[0] == 0) {
                            ctx.log("  [monitor] buffer empty → consumer waits");
                            lock.wait();
                        }
                        int v = slots[takeIdx[0]];
                        takeIdx[0] = (takeIdx[0] + 1) % L61_CAPACITY;
                        count[0]--;
                        ctx.log("  [monitor] take " + v + " (count=" + count[0] + ")");
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        var tp = Thread.ofPlatform().name("pc-monitor-producer").unstarted(put);
        var tc = Thread.ofPlatform().name("pc-monitor-consumer").unstarted(take);
        tp.start();
        tc.start();
        tp.join();
        tc.join();
    }

    private static void runProducerConsumerBlockingQueue(StudyContext ctx) throws Exception {
        BlockingQueue<Integer> q = new ArrayBlockingQueue<>(L61_CAPACITY);

        Runnable producer = () -> {
            try {
                for (int item = 0; item < L61_ITEMS; item++) {
                    // put blocks while size == capacity — same "full" condition as phase 1
                    q.put(item);
                    ctx.log("  [BlockingQueue] put " + item + " (size=" + q.size() + ")");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = () -> {
            try {
                for (int n = 0; n < L61_ITEMS; n++) {
                    // take blocks while empty — same "empty" condition as phase 1
                    int v = q.take();
                    ctx.log("  [BlockingQueue] take " + v + " (size=" + q.size() + ")");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        var p = Thread.ofPlatform().name("pc-bq-producer").unstarted(producer);
        var c = Thread.ofPlatform().name("pc-bq-consumer").unstarted(consumer);
        p.start();
        c.start();
        p.join();
        c.join();
    }
}
