package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

/** Messaging vs REST: delivery semantics and naming. */
public final class Lesson31 extends AbstractMicroLesson {

    public Lesson31() {
        super(31, "Messaging vs REST: at-least-once delivery, command vs event names, async boundaries.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        ctx.log("REST: synchronous request/response, often exactly-once from caller perspective for reads.");
        ctx.log("Messaging: brokers typically offer at-least-once; consumers must be idempotent or dedupe.");
        ctx.log("Spring: KafkaListener / RabbitListener acknowledge modes control redelivery vs data loss trade-offs.");
    }
}
