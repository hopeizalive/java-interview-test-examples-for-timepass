package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Lesson 31 presents transaction management best practices for data integrity.
 *
 * <p>It keeps the pattern explicit with manual begin/commit/rollback so interview explanations
 * can clearly describe happy-path and failure-path behavior.
 */
public final class Lesson31 extends AbstractLesson {

    public Lesson31() {
        super(31, "What are the best practices for managing transactions in JPA for consistency and integrity?");
    }

    /**
     * Lesson 31: transaction consistency and integrity patterns.
     *
     * <p><b>Purpose:</b> Demonstrate safe transactional boundaries and rollback discipline.
     * <p><b>Role:</b> Consolidates transaction concepts into a reusable execution template.
     * <p><b>Demonstration:</b> Runs manual transaction lifecycle and logs the standard control flow.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: note high-level best practices before running concrete flow.
        System.out.println("Keep transactions short; rollback on failure; choose isolation/propagation in Spring.");
        EntityManager em = ctx.studyEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            // Story boundary: begin -> execute work -> commit.
            tx.begin();
            em.persist(new Author("Tx integrity"));
            tx.commit();
        } catch (RuntimeException e) {
            // Story error path: rollback preserves consistency on failure.
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            // Story cleanup: always close EntityManager.
            em.close();
        }
        System.out.println("Pattern: begin → work → commit; catch → rollback.");
    }
}
