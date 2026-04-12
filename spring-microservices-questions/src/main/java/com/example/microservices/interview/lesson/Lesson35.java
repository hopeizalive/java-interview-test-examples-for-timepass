package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

/** Choreography vs orchestration for sagas. */
public final class Lesson35 extends AbstractMicroLesson {

    public Lesson35() {
        super(35, "Saga choreography vs orchestration: events vs central coordinator (Spring State Machine / custom).");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        ctx.log("Choreography: services react to each other's domain events (loose coupling, harder to trace).");
        ctx.log("Orchestration: coordinator drives steps and compensations (clear flow, single point of logic).");
        ctx.log("Spring: both patterns use @Transactional boundaries per service + messaging or synchronous calls.");
    }
}
