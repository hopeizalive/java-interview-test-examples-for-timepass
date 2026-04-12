package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Idempotent consumer: dedupe by business key. */
public final class Lesson34 extends AbstractMicroLesson {

    public Lesson34() {
        super(33, "Idempotent consumer: ConcurrentHashMap.newKeySet() skips duplicate message IDs.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
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
