package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l13.L13Application;
import com.example.microservices.interview.msdata.l13.L13Product;
import com.example.microservices.interview.msdata.l13.L13ProductRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;
import org.springframework.data.domain.PageRequest;

/** Pageable API for large collections. */
public final class Lesson13 extends AbstractMicroLesson {

    public Lesson13() {
        super(13, "Pagination: Pageable limits rows for microservice APIs backed by Spring Data.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L13Application.class, "ms13")) {
            L13ProductRepository repo = c.getBean(L13ProductRepository.class);
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
