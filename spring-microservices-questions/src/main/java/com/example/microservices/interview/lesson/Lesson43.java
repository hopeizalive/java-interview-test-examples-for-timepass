package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

/**
 * Lesson 43 demonstrates Micrometer Observation scope for tracing hooks.
 *
 * <p>It wraps work inside an observation to show where trace/span integration can attach.
 */
public final class Lesson43 extends AbstractMicroLesson {

    public Lesson43() {
        super(38, "Distributed tracing: ObservationRegistry scopes work; Brave/OTel bridges export trace IDs.");
    }

    /**
     * Lesson 38/43: observation-based tracing hooks.
     *
     * <p><b>Purpose:</b> Show instrumentation scope around downstream operations.
     * <p><b>Role:</b> Bridge from metrics into distributed tracing concepts.
     * <p><b>Demonstration:</b> Executes observed block and logs registry context.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story action: execute demo work inside observation context.
        ObservationRegistry registry = ObservationRegistry.create();
        Observation.createNotStarted("lesson43.downstream-call", registry)
                .observe(() -> ctx.log("Inside observation scope"));
        ctx.log("Observation registry type=" + registry.getClass().getSimpleName());
        ctx.log("Talking point: add management.tracing.export.zipkin.enabled or OTel exporter in real apps.");
    }
}
