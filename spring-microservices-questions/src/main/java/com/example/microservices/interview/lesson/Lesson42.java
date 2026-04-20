package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

/**
 * Lesson 42 demonstrates Micrometer counters for service observability.
 *
 * <p>It increments a tagged counter and frames RED metrics interpretation.
 */
public final class Lesson42 extends AbstractMicroLesson {

    public Lesson42() {
        super(37, "Micrometer: SimpleMeterRegistry counter increment; bind to global composite for Actuator export.");
    }

    /**
     * Lesson 37/42: basic metrics instrumentation.
     *
     * <p><b>Purpose:</b> Show collecting request-rate style telemetry.
     * <p><b>Role:</b> Observability foundation before tracing and alerting.
     * <p><b>Demonstration:</b> Registers counter, increments twice, logs final count.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: bind local meter registry for isolated lesson metrics.
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
