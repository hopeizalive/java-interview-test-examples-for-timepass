package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

/** Micrometer Observation API (tracing hooks for RestClient/WebClient when registry wired). */
public final class Lesson43 extends AbstractMicroLesson {

    public Lesson43() {
        super(39, "Distributed tracing: ObservationRegistry scopes work; Brave/OTel bridges export trace IDs.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        ObservationRegistry registry = ObservationRegistry.create();
        Observation.createNotStarted("lesson43.downstream-call", registry)
                .observe(() -> ctx.log("Inside observation scope"));
        ctx.log("Observation registry type=" + registry.getClass().getSimpleName());
        ctx.log("Talking point: add management.tracing.export.zipkin.enabled or OTel exporter in real apps.");
    }
}
