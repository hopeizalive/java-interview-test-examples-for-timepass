package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 3 centers on what a JPA entity is and why annotation metadata matters.
 *
 * <p>It keeps the runtime example minimal so learners can map `@Entity`, `@Id`, and table mapping
 * concepts to a concrete persist operation.
 */
public final class Lesson03 extends AbstractLesson {

    public Lesson03() {
        super(3, "Can you explain the concept of JPA Entity and how it is declared?");
    }

    /**
     * Lesson 3: entity declaration and managed lifecycle.
     *
     * <p><b>Purpose:</b> Connect entity annotations to the persistence lifecycle.
     * <p><b>Role:</b> Reinforces model-first thinking before advanced mapping topics.
     * <p><b>Demonstration:</b> References Author mapping and persists one instance successfully.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: point readers to the annotated entity definition used by the ORM.
        System.out.println("See entity.Author: @Entity, @Table, @Id + managed lifecycle.");
        // Story action/observation: persist verifies that the declared entity is recognized and managed.
        ctx.withTransaction(em -> em.persist(new Author("Entity declaration")));
    }
}
