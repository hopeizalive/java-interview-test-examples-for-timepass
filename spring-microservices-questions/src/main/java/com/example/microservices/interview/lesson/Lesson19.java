package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l19.L19Application;
import com.example.microservices.interview.msdata.l19.L19OrderRepository;
import com.example.microservices.interview.msdata.l19.L19OrderService;
import com.example.microservices.interview.msdata.l19.L19OutboxRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;

/**
 * Lesson 19 demonstrates the transactional outbox pattern.
 *
 * <p>Order data and outbound event payload are stored in one commit so message publication can be
 * retried safely by a relay process.
 */
public final class Lesson19 extends AbstractMicroLesson {

    public Lesson19() {
        super(19, "Outbox table: order + event rows commit atomically; separate relay publishes to broker.");
    }

    /**
     * Lesson 19: atomic order + outbox write.
     *
     * <p><b>Purpose:</b> Prevent dual-write inconsistency between DB updates and broker publishes.
     * <p><b>Role:</b> Core reliability pattern for event-driven microservices.
     * <p><b>Demonstration:</b> Places order and verifies both order and outbox row counts.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L19Application.class, "ms19")) {
            L19OrderService svc = c.getBean(L19OrderService.class);
            // Story action: persist domain state and outbox event in same transaction.
            svc.placeOrder("ORD-1", "{\"type\":\"OrderPlaced\"}");
            ctx.log("Orders=" + c.getBean(L19OrderRepository.class).count()
                    + " outbox=" + c.getBean(L19OutboxRepository.class).count());
        }
        ctx.log("Talking point: outbox avoids dual-write (DB commit vs message send) inconsistency.");
    }
}
