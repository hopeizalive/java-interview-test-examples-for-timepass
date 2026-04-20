package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.OrderStatus;
import com.example.jpa.interview.entity.RetailOrder;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 21 covers enum mapping from Java domain values to database columns.
 *
 * <p>It highlights string-based enum persistence so schema values remain readable and resilient
 * to enum constant reordering.
 */
public final class Lesson21 extends AbstractLesson {

    public Lesson21() {
        super(21, "How can you map an Enum in JPA?");
    }

    /**
     * Lesson 21: enum mapping with @Enumerated.
     *
     * <p><b>Purpose:</b> Show how enum state is persisted in entity fields.
     * <p><b>Role:</b> Adds type-safe domain modeling practice.
     * <p><b>Demonstration:</b> Persists a RetailOrder with enum status and prints stored value.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story action: persist enum-backed status field.
            RetailOrder o = new RetailOrder(OrderStatus.PAID);
            em.persist(o);
            // Story observation: printed enum confirms round-trip mapping choice.
            System.out.println("@Enumerated(STRING) stored: " + o.getStatus());
        });
    }
}
