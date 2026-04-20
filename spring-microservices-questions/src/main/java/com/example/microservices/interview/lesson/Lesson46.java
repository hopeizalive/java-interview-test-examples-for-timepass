package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l11.L11Application;
import com.example.microservices.interview.msdata.l11.L11UserRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;

/**
 * Lesson 46 demonstrates repository-focused JPA test slice behavior.
 *
 * <p>The run verifies repository bean availability in a data-only bootstrapped context.
 */
public final class Lesson46 extends AbstractMicroLesson {

    public Lesson46() {
        super(41, "@DataJpaTest analog: MicroBoot + msdata.l11 slice (repositories only, no web).");
    }

    /**
     * Lesson 41/46: data-layer test slice.
     *
     * <p><b>Purpose:</b> Show persistence-layer testing without web stack overhead.
     * <p><b>Role:</b> Complements controller slice and full integration tests.
     * <p><b>Demonstration:</b> Boots JPA slice app and logs repository bean presence.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: start data-only app context and inspect repository wiring.
        try (var c = MicroBoot.start(L11Application.class, "ms46")) {
            ctx.log("Repository bean present: " + c.getBean(L11UserRepository.class).getClass().getSimpleName());
        }
        ctx.log("Talking point: @DataJpaTest auto-configures TestEntityManager and @Transactional rollback.");
    }
}
