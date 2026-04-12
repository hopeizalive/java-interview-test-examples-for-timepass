package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.OrderStatus;
import com.example.jpa.interview.entity.RetailOrder;
import com.example.jpa.interview.study.StudyContext;

/**
 * How can you map an Enum in JPA?
 */
public final class Lesson21 extends AbstractLesson {

    public Lesson21() {
        super(21, "How can you map an Enum in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            RetailOrder o = new RetailOrder(OrderStatus.PAID);
            em.persist(o);
            System.out.println("@Enumerated(STRING) stored: " + o.getStatus());
        });
    }
}
