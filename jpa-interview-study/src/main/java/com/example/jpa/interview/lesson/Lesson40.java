package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Ticket;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 40 introduces `@Version` for optimistic concurrency control.
 *
 * <p>The demo focuses on initial version assignment so learners can connect mapping annotation to
 * runtime conflict-detection mechanism.
 */
public final class Lesson40 extends AbstractLesson {

    public Lesson40() {
        super(40, "What is the role of the @Version annotation in JPA, and how does it work?");
    }

    /**
     * Lesson 40: @Version semantics.
     *
     * <p><b>Purpose:</b> Show how version fields support optimistic locking.
     * <p><b>Role:</b> Complements earlier locking lesson with direct version annotation focus.
     * <p><b>Demonstration:</b> Persists Ticket and prints initial version value.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story action: persist versioned entity so provider initializes version column.
            Ticket t = new Ticket("Support");
            em.persist(t);
            // Story observation: initial version value confirms optimistic lock tracking.
            System.out.println("Initial @Version=" + t.getVersion());
        });
    }
}
