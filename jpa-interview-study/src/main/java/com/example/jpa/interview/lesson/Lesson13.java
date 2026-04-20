package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.CardPayment;
import com.example.jpa.interview.study.StudyContext;

import java.math.BigDecimal;

/**
 * Lesson 13 introduces JPA inheritance mapping through a concrete subtype entity.
 *
 * <p>It keeps the demo small so learners can focus on strategy semantics (for example
 * single-table) and polymorphic persistence behavior.
 */
public final class Lesson13 extends AbstractLesson {

    public Lesson13() {
        super(13, "Can you explain how inheritance is mapped in JPA?");
    }

    /**
     * Lesson 13: inheritance mapping strategies.
     *
     * <p><b>Purpose:</b> Show that subclass entities can be persisted through mapped inheritance hierarchies.
     * <p><b>Role:</b> Bridges entity basics to advanced modeling techniques.
     * <p><b>Demonstration:</b> Persists a CardPayment and prints generated id under configured strategy.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story action: persist subtype instance to exercise inheritance metadata.
            CardPayment card = new CardPayment(new BigDecimal("42.00"), "4242");
            em.persist(card);
            // Story observation: successful id generation confirms hierarchy persistence path.
            System.out.println("SINGLE_TABLE inheritance persisted CardPayment id=" + card.getId());
        });
    }
}
