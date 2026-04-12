package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

/** Dead-letter queues and replay. */
public final class Lesson36 extends AbstractMicroLesson {

    public Lesson36() {
        super(36, "Dead-letter handling: move poison messages to DLQ; replay after fix with auditing.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        ctx.log("Kafka: seek to offset or consume DLQ topic into a repair tool.");
        ctx.log("Rabbit: x-dead-letter-exchange routes failed deliveries after max retries.");
        ctx.log("Spring: implement ErrorHandler / SeekToCurrentErrorHandler patterns for safe retries.");
    }
}
