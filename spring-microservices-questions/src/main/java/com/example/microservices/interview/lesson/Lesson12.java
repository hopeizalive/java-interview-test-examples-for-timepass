package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l12.L12Application;
import com.example.microservices.interview.msdata.l12.L12Line;
import com.example.microservices.interview.msdata.l12.L12Order;
import com.example.microservices.interview.msdata.l12.L12OrderRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;
import org.springframework.transaction.support.TransactionTemplate;

/** Lazy collections vs @EntityGraph to avoid N+1 when exposing aggregates. */
public final class Lesson12 extends AbstractMicroLesson {

    public Lesson12() {
        super(12, "Lazy @OneToMany: load without graph then with @EntityGraph (fewer round-trips).");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L12Application.class, "ms12")) {
            L12OrderRepository repo = c.getBean(L12OrderRepository.class);
            TransactionTemplate tx = new TransactionTemplate(c.getBean(org.springframework.transaction.PlatformTransactionManager.class));
            Long id = tx.execute(status -> {
                L12Order o = new L12Order();
                L12Line l1 = new L12Line();
                l1.setSku("A");
                L12Line l2 = new L12Line();
                l2.setSku("B");
                o.addLine(l1);
                o.addLine(l2);
                return repo.save(o).getId();
            });
            tx.executeWithoutResult(s -> {
                L12Order plain = repo.loadOrderWithoutLines(id).orElseThrow();
                int n = plain.getLines().size();
                ctx.log("Lines visible after lazy init (extra SELECTs likely): " + n);
            });
            tx.executeWithoutResult(s -> {
                L12Order graph = repo.findById(id).orElseThrow();
                ctx.log("Lines size with entity graph (eager graph fetch): " + graph.getLines().size());
            });
        }
        ctx.log("Talking point: use fetch join / EntityGraph for API reads; keep lazy inside pure domain code.");
    }
}
