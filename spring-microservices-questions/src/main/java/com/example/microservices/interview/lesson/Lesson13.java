package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l13.L13Application;
import com.example.microservices.interview.msdata.l13.L13Product;
import com.example.microservices.interview.msdata.l13.L13ProductRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;
import org.springframework.data.domain.PageRequest;

/**
 * Lesson 13 introduces pagination for collection-heavy endpoints.
 *
 * <p>It demonstrates `Pageable` usage to keep API responses bounded and predictable.
 */
public final class Lesson13 extends AbstractMicroLesson {

    public Lesson13() {
        super(13, "Pagination: Pageable limits rows for microservice APIs backed by Spring Data.");
    }

    /**
     * Lesson 13: Spring Data pagination for API safety.
     *
     * <p><b>Purpose:</b> Show bounded retrieval for large datasets.
     * <p><b>Role:</b> Prevents unbounded reads in service endpoints.
     * <p><b>Demonstration:</b> Inserts 25 products and reads first page of 5 with totals.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L13Application.class, "ms13")) {
            L13ProductRepository repo = c.getBean(L13ProductRepository.class);
            // Story setup: create enough rows so page metadata is meaningful.
            for (int i = 0; i < 25; i++) {
                L13Product p = new L13Product();
                p.setName("p-" + i);
                repo.save(p);
            }
            var page = repo.findAllByOrderByIdAsc(PageRequest.of(0, 5));
            ctx.log("Total elements=" + page.getTotalElements() + " first page size=" + page.getNumberOfElements());
        }
        ctx.log("Talking point: never stream unbounded collections over HTTP—page or cursor.");
    }
}
