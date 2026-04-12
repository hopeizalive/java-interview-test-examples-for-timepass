package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

import java.time.Duration;
import java.util.function.Supplier;

/** Circuit breaker fail-fast after error threshold. */
public final class Lesson25 extends AbstractMicroLesson {

    public Lesson25() {
        super(25, "Circuit breaker: opens after failures, rejects fast while half-open probes recover.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        CircuitBreakerConfig cfg = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(100))
                .slidingWindowSize(4)
                .minimumNumberOfCalls(2)
                .build();
        CircuitBreaker cb = CircuitBreaker.of("downstream", cfg);
        Supplier<String> boom = CircuitBreaker.decorateSupplier(cb, () -> {
            throw new RuntimeException("down");
        });
        try {
            boom.get();
        } catch (Exception ignored) {
        }
        try {
            boom.get();
        } catch (Exception ignored) {
        }
        try {
            boom.get();
            ctx.log("Unexpected success while circuit open");
        } catch (CallNotPermittedException ex) {
            ctx.log("Circuit open: " + ex.getClass().getSimpleName());
        }
        ctx.log("State=" + cb.getState());
        ctx.log("Talking point: combine timeouts + circuit breakers to shed load on unhealthy dependencies.");
    }
}
