package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

/** Micrometer counter + RED metrics framing. */
public final class Lesson42 extends AbstractMicroLesson {

    public Lesson42() {
        super(37, "Micrometer: SimpleMeterRegistry counter increment; bind to global composite for Actuator export.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        SimpleMeterRegistry reg = new SimpleMeterRegistry();
        Metrics.addRegistry(reg);
        try {
            Counter c = reg.counter("lesson42.requests", "service", "demo");
            c.increment();
            c.increment();
            ctx.log("Counter count=" + c.count());
        } finally {
            Metrics.removeRegistry(reg);
        }
        ctx.log("Talking point: RED—rate, errors, duration; tag with tenant/service for slicing.");
    }
}
