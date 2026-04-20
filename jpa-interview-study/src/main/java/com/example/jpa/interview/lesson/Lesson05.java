package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 5 explains how JPA primary keys are modeled and generated.
 *
 * <p>It uses a simple persist flow to make `@Id` and generation strategy behavior visible without
 * introducing relationship complexity.
 */
public final class Lesson05 extends AbstractLesson {

    public Lesson05() {
        super(5, "How would you define and use a primary key in JPA?");
    }

    /**
     * Lesson 5: primary key definition and generated identifiers.
     *
     * <p><b>Purpose:</b> Show how JPA assigns entity identity using mapping annotations.
     * <p><b>Role:</b> Prepares learners for association mapping that depends on stable ids.
     * <p><b>Demonstration:</b> Persists Author and prints the generated primary key value.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story action: create an entity without id; strategy fills it during persist/flush.
            Author a = new Author("Primary key");
            em.persist(a);
            // Story observation: non-null id confirms @Id + generation mapping is active.
            System.out.println("@Id + @GeneratedValue(IDENTITY) assigned: " + a.getId());
        });
    }
}
