package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lesson 34 demonstrates idempotent message handling.
 *
 * <p>A dedupe set keyed by message id ensures retried deliveries do not reapply effects.
 */
public final class Lesson34 extends AbstractMicroLesson {

    public Lesson34() {
        super(33, "Idempotent consumer: ConcurrentHashMap.newKeySet() skips duplicate message IDs.");
    }

    /**
     * Lesson 33/34: idempotent consumer pattern.
     *
     * <p><b>Purpose:</b> Show safe duplicate handling under at-least-once delivery.
     * <p><b>Role:</b> Reliability pattern for retry-driven messaging systems.
     * <p><b>Demonstration:</b> Processes duplicate/new ids and logs applied vs skipped states.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: maintain seen-id set to gate duplicate message processing.
        Set<String> seen = ConcurrentHashMap.newKeySet();
        ctx.log("First process id-1: " + process(seen, "id-1"));
        ctx.log("Retry id-1: " + process(seen, "id-1"));
        ctx.log("New id-2: " + process(seen, "id-2"));
        ctx.log("Talking point: prefer natural keys (orderId) or idempotency tokens over blind retries.");
    }

    private static String process(Set<String> seen, String id) {
        if (!seen.add(id)) {
            return "skipped-duplicate";
        }
        return "applied";
    }
}
