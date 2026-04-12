package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

import java.util.concurrent.atomic.AtomicReference;

/** Read-your-writes vs eventual consistency across services. */
public final class Lesson18 extends AbstractMicroLesson {

    public Lesson18() {
        super(18, "Eventual consistency: local view updates after async propagation delay.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws InterruptedException {
        AtomicReference<String> readModel = new AtomicReference<>("OLD");
        Thread publisher = new Thread(() -> {
            try {
                Thread.sleep(80);
                readModel.set("NEW");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        publisher.start();
        ctx.log("Immediately after command: read model still " + readModel.get());
        publisher.join();
        ctx.log("After replication delay: read model " + readModel.get());
        ctx.log("Talking point: choose strong consistency inside a service; across services prefer clear UX for lag.");
    }
}
