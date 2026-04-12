package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.OrderStatus;
import com.example.jpa.interview.entity.RetailOrder;
import com.example.jpa.interview.study.StudyContext;

import java.util.Date;

/**
 * What is the purpose of the @Temporal annotation in JPA?
 */
public final class Lesson22 extends AbstractLesson {

    public Lesson22() {
        super(22, "What is the purpose of the @Temporal annotation in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            RetailOrder o = new RetailOrder(OrderStatus.PENDING);
            o.setLegacyCreated(new Date());
            em.persist(o);
            System.out.println("@Temporal(TIMESTAMP) maps java.util.Date → SQL TIMESTAMP.");
            System.out.println("Prefer java.time types (LocalDateTime) for new code — no @Temporal.");
        });
    }
}
