package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l12.L12Application;
import com.example.microservices.interview.msdata.l12.L12Line;
import com.example.microservices.interview.msdata.l12.L12Order;
import com.example.microservices.interview.msdata.l12.L12OrderRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Lesson 12 demonstrates lazy-loading behavior and entity-graph based fetch tuning.
 *
 * <p>It compares plain loading against graph-enabled loading to explain N+1 risk in API-facing
 * aggregate reads.
 */
public final class Lesson12 extends AbstractMicroLesson {

    public Lesson12() {
        super(12, "Lazy @OneToMany: load without graph then with @EntityGraph (fewer round-trips).");
    }

    /**
     * Lesson 12: lazy associations versus @EntityGraph.
     *
     * <p><b>Purpose:</b> Show how fetch strategy changes query behavior for child collections.
     * <p><b>Role:</b> Builds read-performance awareness for service endpoints.
     * <p><b>Demonstration:</b> Loads same order without graph then with graph and logs line counts.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L12Application.class, "ms12")) {
            L12OrderRepository repo = c.getBean(L12OrderRepository.class);
            TransactionTemplate tx = new TransactionTemplate(c.getBean(org.springframework.transaction.PlatformTransactionManager.class));
            // Story setup: create one order with two lines for fetch-strategy comparison.
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
            // Story phase A: plain load may lazy-init children with additional selects.
            tx.executeWithoutResult(s -> {
                L12Order plain = repo.loadOrderWithoutLines(id).orElseThrow();
                int n = plain.getLines().size();
                ctx.log("Lines visible after lazy init (extra SELECTs likely): " + n);
            });
            // Story phase B: graph-based load fetches requested associations eagerly.
            tx.executeWithoutResult(s -> {
                L12Order graph = repo.findById(id).orElseThrow();
                ctx.log("Lines size with entity graph (eager graph fetch): " + graph.getLines().size());
            });
        }
        ctx.log("Talking point: use fetch join / EntityGraph for API reads; keep lazy inside pure domain code.");
    }
}
