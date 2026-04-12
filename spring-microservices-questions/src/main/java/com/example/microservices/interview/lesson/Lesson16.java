package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdata.l16.L16Application;
import com.example.microservices.interview.msdata.l16.L16Stock;
import com.example.microservices.interview.msdata.l16.L16StockRepository;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;

/** @Version optimistic locking for concurrent microservice instances. */
public final class Lesson16 extends AbstractMicroLesson {

    public Lesson16() {
        super(16, "Optimistic locking: @Version increments on successful updates to detect stale writes.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L16Application.class, "ms16")) {
            L16StockRepository repo = c.getBean(L16StockRepository.class);
            L16Stock s = new L16Stock();
            s.setSku("SKU-1");
            s.setQty(1);
            s = repo.save(s);
            long v0 = s.getVersion();
            s.setQty(5);
            s = repo.save(s);
            long v1 = s.getVersion();
            ctx.log("Version moved " + v0 + " -> " + v1 + " after qty update.");
        }
        ctx.log("Talking point: under concurrent PUTs, second commit can fail with OptimisticLockException—surface as 409.");
    }
}
