package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;

/** Transactional messaging: outbox vs dual-write. */
public final class Lesson38 extends AbstractMicroLesson {

    public Lesson38() {
        super(38, "Transactional messaging: outbox avoids dual-write; never publish-then-commit or commit-then-crash-before-send.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        ctx.log("Dual-write problem: DB commit and broker publish are two systems—one can succeed alone.");
        ctx.log("Outbox: same DB transaction writes business row + outbox row; relay publishes asynchronously.");
        ctx.log("See lesson 19 for a minimal Spring Data outbox sketch.");
    }
}
