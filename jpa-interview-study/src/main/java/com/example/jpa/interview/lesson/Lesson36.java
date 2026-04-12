package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.CheckingAccount;
import com.example.jpa.interview.study.StudyContext;

import java.math.BigDecimal;

/**
 * How do you map complex inheritance hierarchies in JPA?
 */
public final class Lesson36 extends AbstractLesson {

    public Lesson36() {
        super(36, "How do you map complex inheritance hierarchies in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            CheckingAccount ca = new CheckingAccount("Owner", new BigDecimal("500.00"));
            em.persist(ca);
            System.out.println("JOINED subclass persisted CheckingAccount id=" + ca.getId());
        });
    }
}
