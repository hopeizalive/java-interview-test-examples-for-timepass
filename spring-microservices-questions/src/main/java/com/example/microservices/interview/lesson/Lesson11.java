package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l11.L11Application;
import com.example.microservices.interview.msdata.l11.L11UserService;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;

/** JpaRepository + @Transactional service boundaries. */
public final class Lesson11 extends AbstractMicroLesson {

    public Lesson11() {
        super(11, "Spring Data JpaRepository + @Transactional service writes and read-only query.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L11Application.class, "ms11")) {
            L11UserService svc = c.getBean(L11UserService.class);
            svc.create("alice");
            svc.create("bob");
            ctx.log("User count after two creates: " + svc.count());
        }
        ctx.log("Talking point: keep transactions on the service layer; repositories stay persistence-focused.");
    }
}
