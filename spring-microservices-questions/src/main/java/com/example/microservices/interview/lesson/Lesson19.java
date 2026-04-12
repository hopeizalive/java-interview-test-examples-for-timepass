package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l19.L19Application;
import com.example.microservices.interview.msdata.l19.L19OrderRepository;
import com.example.microservices.interview.msdata.l19.L19OrderService;
import com.example.microservices.interview.msdata.l19.L19OutboxRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;

/** Outbox pattern: same transaction persists domain row + outbound message stub. */
public final class Lesson19 extends AbstractMicroLesson {

    public Lesson19() {
        super(19, "Outbox table: order + event rows commit atomically; separate relay publishes to broker.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L19Application.class, "ms19")) {
            L19OrderService svc = c.getBean(L19OrderService.class);
            svc.placeOrder("ORD-1", "{\"type\":\"OrderPlaced\"}");
            ctx.log("Orders=" + c.getBean(L19OrderRepository.class).count()
                    + " outbox=" + c.getBean(L19OutboxRepository.class).count());
        }
        ctx.log("Talking point: outbox avoids dual-write (DB commit vs message send) inconsistency.");
    }
}
