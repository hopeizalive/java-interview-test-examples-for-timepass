package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.CheckingAccount;
import com.example.jpa.interview.study.StudyContext;

import java.math.BigDecimal;

/**
 * Lesson 36 demonstrates mapping deeper inheritance hierarchies with JOINED strategy.
 *
 * <p>It persists a concrete subclass to show how polymorphic models remain queryable while data is
 * distributed across related tables.
 */
public final class Lesson36 extends AbstractLesson {

    public Lesson36() {
        super(36, "How do you map complex inheritance hierarchies in JPA?");
    }

    /**
     * Lesson 36: complex inheritance hierarchy mapping.
     *
     * <p><b>Purpose:</b> Show practical persistence of subclass entities in joined inheritance.
     * <p><b>Role:</b> Extends earlier inheritance basics to more normalized table layouts.
     * <p><b>Demonstration:</b> Persists `CheckingAccount` and logs generated id.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story action: persist subclass instance participating in joined hierarchy.
            CheckingAccount ca = new CheckingAccount("Owner", new BigDecimal("500.00"));
            em.persist(ca);
            // Story observation: id confirms insert path across hierarchy mapping.
            System.out.println("JOINED subclass persisted CheckingAccount id=" + ca.getId());
        });
    }
}
