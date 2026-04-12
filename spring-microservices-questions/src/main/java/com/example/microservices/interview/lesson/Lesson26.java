package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/** Bulkhead: limit concurrent calls to a dependency. */
public final class Lesson26 extends AbstractMicroLesson {

    public Lesson26() {
        super(26, "Bulkhead: Semaphore(1) isolates slow dependency from saturating all worker threads.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        Semaphore bulkhead = new Semaphore(1);
        AtomicInteger rejected = new AtomicInteger();
        ExecutorService pool = Executors.newFixedThreadPool(4);
        Runnable task = () -> {
            if (!bulkhead.tryAcquire()) {
                rejected.incrementAndGet();
                return;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                bulkhead.release();
            }
        };
        for (int i = 0; i < 4; i++) {
            pool.submit(task);
        }
        pool.shutdown();
        pool.awaitTermination(2, TimeUnit.SECONDS);
        ctx.log("Tasks rejected when bulkhead full: " + rejected.get());
        ctx.log("Talking point: Resilience4j bulkhead enforces similar limits for Feign/WebClient.");
    }
}
