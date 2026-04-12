package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public final class DemoSync {

    private DemoSync() {}

    private static int shared;

    public static synchronized void bumpSync() {
        shared++;
    }

    public static void l13(StudyContext ctx) throws Exception {
        ctx.log("synchronized method: mutual exclusion on the monitor of DemoSync.class for static sync.");
        shared = 0;
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < 1000; i++) {
                ex.submit(DemoSync::bumpSync);
            }
        }
        ctx.log("  shared counter: " + shared);
    }

    public static void l14(StudyContext ctx) throws Exception {
        ctx.log("synchronized block on a private lock object: finer scope than whole method.");
        var lock = new Object();
        var c = new int[] {0};
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < 1000; i++) {
                ex.submit(() -> {
                    synchronized (lock) {
                        c[0]++;
                    }
                });
            }
        }
        ctx.log("  count: " + c[0]);
    }

    public static void l15(StudyContext ctx) {
        ctx.log("ReentrantLock: explicit lock/unlock; try/finally avoids deadlocks from exceptions.");
        var lock = new ReentrantLock();
        var c = new int[] {0};
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(4)) {
            for (int i = 0; i < 500; i++) {
                ex.submit(() -> {
                    lock.lock();
                    try {
                        c[0]++;
                    } finally {
                        lock.unlock();
                    }
                });
            }
        }
        ctx.log("  count: " + c[0]);
    }

    public static void l16(StudyContext ctx) throws Exception {
        ctx.log("tryLock(timeout): timed wait instead of blocking forever.");
        var lock = new ReentrantLock();
        lock.lock();
        try (var ex = java.util.concurrent.Executors.newSingleThreadExecutor()) {
            var f = ex.submit(() -> {
                try {
                    boolean got = lock.tryLock(50, java.util.concurrent.TimeUnit.MILLISECONDS);
                    ctx.log("  background tryLock while main holds lock: " + got);
                    if (got) {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            Thread.sleep(20);
            lock.unlock();
            f.get();
        }
    }

    public static void l17(StudyContext ctx) throws Exception {
        ctx.log("ReadWriteLock: many readers OR one writer — good for read-heavy maps.");
        ReadWriteLock rw = new ReentrantReadWriteLock();
        var data = new int[] {0};
        try (var ex = java.util.concurrent.Executors.newFixedThreadPool(4)) {
            for (int i = 0; i < 20; i++) {
                ex.submit(() -> {
                    rw.readLock().lock();
                    try {
                        int v = data[0];
                        ctx.log("  read: " + v);
                    } finally {
                        rw.readLock().unlock();
                    }
                });
            }
            ex.submit(() -> {
                rw.writeLock().lock();
                try {
                    data[0] = 42;
                    ctx.log("  write 42");
                } finally {
                    rw.writeLock().unlock();
                }
            }).get();
        }
    }

    public static void l18(StudyContext ctx) {
        ctx.log("StampedLock optimistic read: validate stamp after reading fields.");
        var sl = new StampedLock();
        long stamp = sl.tryOptimisticRead();
        int x = 1;
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            try {
                x = 2;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        ctx.log("  x=" + x + " (demo path exercised validate/upgrade idea)");
    }
}
