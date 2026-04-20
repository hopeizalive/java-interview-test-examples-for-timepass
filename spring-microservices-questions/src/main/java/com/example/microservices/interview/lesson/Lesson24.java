package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Lesson 24 demonstrates retry behavior for transient failures.
 *
 * <p>It uses a flaky supplier to show eventual success after bounded retry attempts.
 */
public final class Lesson24 extends AbstractMicroLesson {

    public Lesson24() {
        super(24, "Resilience4j Retry: transient failures retried with backoff (idempotency caveat).");
    }

    /**
     * Lesson 24: Resilience4j retry semantics.
     *
     * <p><b>Purpose:</b> Show controlled retry with max attempts and backoff.
     * <p><b>Role:</b> First resilience policy before breaker/bulkhead composition.
     * <p><b>Demonstration:</b> Fails twice, succeeds on third attempt, and logs attempts.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story action: decorate unstable call and let retry policy mediate failures.
        AtomicInteger attempts = new AtomicInteger();
        Supplier<String> flaky = () -> {
            if (attempts.incrementAndGet() < 3) {
                throw new IllegalStateException("temporary");
            }
            return "success";
        };
        Retry retry = Retry.of("demo", RetryConfig.custom()
                .maxAttempts(4)
                .waitDuration(Duration.ofMillis(10))
                .build());
        Supplier<String> guarded = Retry.decorateSupplier(retry, flaky);
        ctx.log("Result after retries: " + guarded.get() + " (attempts=" + attempts.get() + ")");
        ctx.log("Talking point: never blindly retry non-idempotent POST without dedupe keys.");
    }
}
