package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 7 demonstrates JPA entity lifecycle states in a single transaction.
 *
 * <p>The flow makes transient, managed, and detached states visible through `contains(...)`
 * checks so interview explanations can stay concrete.
 */
public final class Lesson07 extends AbstractLesson {

    public Lesson07() {
        super(7, "Can you differentiate between transient, persistent, and detached objects in JPA?");
    }

    /**
     * Lesson 7: transient vs managed vs detached.
     *
     * <p><b>Purpose:</b> Clarify lifecycle transitions controlled by persist and detach.
     * <p><b>Role:</b> Builds directly on persistence-context concepts from Lesson 6.
     * <p><b>Demonstration:</b> Prints contains-state before persist, after persist, and after detach.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story setup: newly constructed entity starts in transient state.
            Author t = new Author("lifecycle");
            System.out.println("Before persist, contains? " + em.contains(t));
            // Story action: persist transitions transient -> managed.
            em.persist(t);
            System.out.println("After persist (managed): " + em.contains(t));
            // Story action: detach transitions managed -> detached.
            em.detach(t);
            // Story observation: detached entities are no longer tracked by the context.
            System.out.println("After detach: " + em.contains(t));
        });
    }
}
