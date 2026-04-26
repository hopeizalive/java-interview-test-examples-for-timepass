package com.example.javadp.interview.lesson.patterns.modern;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * Circuit breaker (Release It!, Microservices Patterns): fail fast while dependencies are unhealthy; probe recovery with
 * limited traffic.
 */
public final class CircuitBreakerPatternLesson {

    private CircuitBreakerPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Netflix Hystrix / Resilience4j",
                "Java libraries wrap remote calls with metrics-driven OPEN/HALF_OPEN/CLOSED states and bulkheads.",
                "Industry default story for JVM microservices.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Istio / Envoy outlier detection",
                "Sidecars eject unhealthy upstream hosts after consecutive failures - distributed circuit breaking.",
                "Same pattern at the mesh layer instead of in-process.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "AWS SDK retries with circuit-aware backoff",
                "Cloud clients combine circuit breaking with exponential backoff to protect downstream SaaS APIs.",
                "Operational resilience, not just application code.");

        ctx.log("--- Java core tie-in ---");
        java.util.concurrent.Semaphore permits = new java.util.concurrent.Semaphore(0);
        boolean acquired = permits.tryAcquire();
        ctx.log("java.util.concurrent.Semaphore.tryAcquire fail-fast when no permits (guarded resource): " + acquired);

        PatternLessonHeader.print(
                ctx,
                "Circuit Breaker",
                "Modern / resilience (Release It!, Microservices Patterns)",
                "Track failures; open the circuit to skip expensive calls; later probe with limited traffic.",
                "After two failures the breaker opens; the next call probes half-open, then the dependency succeeds once healthy.");
        DemoCircuitBreaker cb = new DemoCircuitBreaker(2);
        RemoteClient flaky = new RemoteClient();
        for (int i = 0; i < 6; i++) {
            ctx.log("call " + i + " => " + cb.invoke(flaky::call));
        }
    }

    private static final class RemoteClient {
        private int attempts;

        String call() {
            attempts++;
            if (attempts < 4) {
                throw new IllegalStateException("dependency down");
            }
            return "ok";
        }
    }

    private enum CbState {
        CLOSED,
        OPEN,
        HALF_OPEN
    }

    private static final class DemoCircuitBreaker {
        private final int failureThreshold;
        private CbState state = CbState.CLOSED;
        private int consecutiveFailures;

        DemoCircuitBreaker(int failureThreshold) {
            this.failureThreshold = failureThreshold;
        }

        String invoke(ThrowingSupplier supplier) {
            if (state == CbState.OPEN) {
                state = CbState.HALF_OPEN;
                return trial(supplier);
            }
            return trial(supplier);
        }

        private String trial(ThrowingSupplier supplier) {
            try {
                String ok = supplier.get();
                state = CbState.CLOSED;
                consecutiveFailures = 0;
                return ok;
            } catch (RuntimeException ex) {
                if (state == CbState.HALF_OPEN) {
                    state = CbState.OPEN;
                }
                consecutiveFailures++;
                if (consecutiveFailures >= failureThreshold) {
                    state = CbState.OPEN;
                }
                return "error:" + ex.getMessage();
            }
        }
    }

    @FunctionalInterface
    private interface ThrowingSupplier {
        String get();
    }
}
