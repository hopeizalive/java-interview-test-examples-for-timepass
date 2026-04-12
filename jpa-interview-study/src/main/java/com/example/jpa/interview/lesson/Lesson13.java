package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.CardPayment;
import com.example.jpa.interview.study.StudyContext;

import java.math.BigDecimal;

/**
 * Can you explain how inheritance is mapped in JPA?
 */
public final class Lesson13 extends AbstractLesson {

    public Lesson13() {
        super(13, "Can you explain how inheritance is mapped in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            CardPayment card = new CardPayment(new BigDecimal("42.00"), "4242");
            em.persist(card);
            System.out.println("SINGLE_TABLE inheritance persisted CardPayment id=" + card.getId());
        });
    }
}
