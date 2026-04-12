package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l11.L11Application;
import com.example.microservices.interview.msdata.l11.L11UserRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;

/** @DataJpaTest analog: only JPA + H2 via isolated Boot app package scan. */
public final class Lesson46 extends AbstractMicroLesson {

    public Lesson46() {
        super(46, "@DataJpaTest analog: MicroBoot + msdata.l11 slice (repositories only, no web).");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L11Application.class, "ms46")) {
            ctx.log("Repository bean present: " + c.getBean(L11UserRepository.class).getClass().getSimpleName());
        }
        ctx.log("Talking point: @DataJpaTest auto-configures TestEntityManager and @Transactional rollback.");
    }
}
