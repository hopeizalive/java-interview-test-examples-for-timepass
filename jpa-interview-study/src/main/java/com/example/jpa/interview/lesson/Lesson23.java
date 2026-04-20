package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.SeqAuthor;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 23 demonstrates sequence-based primary key generation.
 *
 * <p>It helps explain database sequence allocation strategy versus identity columns in interview
 * discussions about portability and batching behavior.
 */
public final class Lesson23 extends AbstractLesson {

    public Lesson23() {
        super(23, "How do you configure a JPA entity to use a sequence generator for primary keys?");
    }

    /**
     * Lesson 23: sequence generator configuration.
     *
     * <p><b>Purpose:</b> Show how entities can derive ids from database sequences.
     * <p><b>Role:</b> Extends primary-key strategy coverage.
     * <p><b>Demonstration:</b> Persists SeqAuthor and prints generated sequence id.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story action: persist entity configured with sequence generation strategy.
            SeqAuthor a = new SeqAuthor("Sequence PK");
            em.persist(a);
            // Story observation: emitted id demonstrates sequence-backed allocation.
            System.out.println("GenerationType.SEQUENCE id=" + a.getId());
        });
    }
}
