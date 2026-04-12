package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l15.L15Application;
import com.example.microservices.interview.msdata.l15.L15Todo;
import com.example.microservices.interview.msdata.l15.L15TodoRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;

/** Derived finder vs explicit @Query. */
public final class Lesson15 extends AbstractMicroLesson {

    public Lesson15() {
        super(15, "Derived query methods vs @Query JPQL for flexible read patterns.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L15Application.class, "ms15")) {
            L15TodoRepository repo = c.getBean(L15TodoRepository.class);
            L15Todo a = new L15Todo();
            a.setTitle("Buy milk");
            a.setDone(false);
            repo.save(a);
            L15Todo b = new L15Todo();
            b.setTitle("Ship Order");
            b.setDone(true);
            repo.save(b);
            ctx.log("Open todos (derived): " + repo.findByDoneFalse().size());
            ctx.log("Search 'order' (@Query): " + repo.searchTitle("order").size());
        }
        ctx.log("Talking point: start with derived queries; move to @Query when logic exceeds naming conventions.");
    }
}
