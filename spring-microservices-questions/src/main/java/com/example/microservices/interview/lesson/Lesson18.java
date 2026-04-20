package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Lesson 18 illustrates eventual consistency timing.
 *
 * <p>The read model lags behind command-side writes to mimic asynchronous replication between
 * services.
 */
public final class Lesson18 extends AbstractMicroLesson {

    public Lesson18() {
        super(18, "Eventual consistency: local view updates after async propagation delay.");
    }

    /**
     * Lesson 18: eventual consistency read lag.
     *
     * <p><b>Purpose:</b> Show temporary divergence between write and read views.
     * <p><b>Role:</b> Prepares students to discuss UX and retry strategies under async propagation.
     * <p><b>Demonstration:</b> Logs read model value before and after delayed update.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws InterruptedException {
        // Story setup: read model starts with stale value.
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
        // Story observation: immediate read is stale until async projection catches up.
        ctx.log("Immediately after command: read model still " + readModel.get());
        publisher.join();
        ctx.log("After replication delay: read model " + readModel.get());
        ctx.log("Talking point: choose strong consistency inside a service; across services prefer clear UX for lag.");
    }
}
