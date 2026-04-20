package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l11.L11Application;
import com.example.microservices.interview.msdata.l11.L11UserService;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;

/**
 * Lesson 11 demonstrates repository usage behind service-level transactions.
 *
 * <p>The flow emphasizes that business operations should be transactional in services while
 * repositories stay focused on persistence mechanics.
 */
public final class Lesson11 extends AbstractMicroLesson {

    public Lesson11() {
        super(11, "Spring Data JpaRepository + @Transactional service writes and read-only query.");
    }

    /**
     * Lesson 11: service transaction boundaries with Spring Data.
     *
     * <p><b>Purpose:</b> Show write operations coordinated by transactional service methods.
     * <p><b>Role:</b> Introduces reliable write consistency patterns for microservice internals.
     * <p><b>Demonstration:</b> Creates two users and verifies count in one lesson run.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: boot lesson-specific data service context.
        try (var c = MicroBoot.start(L11Application.class, "ms11")) {
            L11UserService svc = c.getBean(L11UserService.class);
            svc.create("alice");
            svc.create("bob");
            // Story observation: count reflects completed transactional writes.
            ctx.log("User count after two creates: " + svc.count());
        }
        ctx.log("Talking point: keep transactions on the service layer; repositories stay persistence-focused.");
    }
}
