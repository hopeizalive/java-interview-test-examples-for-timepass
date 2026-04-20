package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Lesson 8 shows explicit transaction demarcation with `EntityTransaction`.
 *
 * <p>Unlike helper-based transaction wrappers, this lesson exposes begin/commit/rollback control
 * so users can explain failure handling in interviews.
 */
public final class Lesson08 extends AbstractLesson {

    public Lesson08() {
        super(8, "What is an EntityTransaction in JPA and how is it used?");
    }

    /**
     * Lesson 8: manual transaction control with EntityTransaction.
     *
     * <p><b>Purpose:</b> Demonstrate explicit begin/commit/rollback lifecycle.
     * <p><b>Role:</b> Teaches low-level transaction semantics behind convenience helpers.
     * <p><b>Demonstration:</b> Persists an entity on success and rolls back on runtime failure.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: open an EntityManager manually to show direct transaction API usage.
        EntityManager em = ctx.studyEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            // Story action: start transaction boundary, then execute write.
            tx.begin();
            em.persist(new Author("Transaction commit"));
            // Story boundary: commit finalizes DB changes.
            tx.commit();
            System.out.println("Committed successfully.");
        } catch (RuntimeException e) {
            // Story error path: rollback keeps DB consistent when write flow fails.
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            // Story cleanup: release provider resources in all outcomes.
            em.close();
        }
    }
}
