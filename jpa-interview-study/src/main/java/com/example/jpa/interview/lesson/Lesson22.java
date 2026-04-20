package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.OrderStatus;
import com.example.jpa.interview.entity.RetailOrder;
import com.example.jpa.interview.study.StudyContext;

import java.util.Date;

/**
 * Lesson 22 explains legacy date/time mapping with `@Temporal`.
 *
 * <p>It also calls out modern `java.time` preference so interview answers can compare old and new
 * date APIs clearly.
 */
public final class Lesson22 extends AbstractLesson {

    public Lesson22() {
        super(22, "What is the purpose of the @Temporal annotation in JPA?");
    }

    /**
     * Lesson 22: @Temporal and date/time persistence.
     *
     * <p><b>Purpose:</b> Show how `java.util.Date` is mapped to SQL temporal types.
     * <p><b>Role:</b> Complements basic type mapping lessons with temporal specifics.
     * <p><b>Demonstration:</b> Persists legacy date field and logs TIMESTAMP mapping plus modern guidance.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story action: set legacy Date field before persistence.
            RetailOrder o = new RetailOrder(OrderStatus.PENDING);
            o.setLegacyCreated(new Date());
            em.persist(o);
            // Story takeaway: show annotation behavior and recommended modern alternative.
            System.out.println("@Temporal(TIMESTAMP) maps java.util.Date → SQL TIMESTAMP.");
            System.out.println("Prefer java.time types (LocalDateTime) for new code — no @Temporal.");
        });
    }
}
