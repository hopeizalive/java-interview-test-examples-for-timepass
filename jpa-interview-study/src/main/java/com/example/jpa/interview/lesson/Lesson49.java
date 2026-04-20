package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Lesson 49 demonstrates JPA write patterns used in batch jobs.
 *
 * <p>It uses periodic flush/clear checkpoints to keep persistence-context memory bounded while
 * processing many rows.
 */
public final class Lesson49 extends AbstractLesson {

    public Lesson49() {
        super(49, "Can you discuss the integration of JPA with batch processing frameworks?");
    }

    /**
     * Lesson 49: JPA with batch processing.
     *
     * <p><b>Purpose:</b> Show chunk-style persistence for large batch workloads.
     * <p><b>Role:</b> Adds operational scaling practice to transaction fundamentals.
     * <p><b>Demonstration:</b> Persists multiple Authors and flushes/clears at intervals.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: open transaction for chunk-oriented insert batch.
        EntityManager em = ctx.studyEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        final int batch = 20;
        for (int i = 0; i < batch; i++) {
            em.persist(new Author("batch-" + i));
            // Story boundary: flush+clear periodically to limit managed-entity memory growth.
            if (i % 5 == 0) {
                em.flush();
                em.clear();
            }
        }
        tx.commit();
        em.close();
        // Story takeaway: chunk boundaries improve scalability in batch frameworks.
        System.out.println("Chunk pattern: flush + clear to bound persistence context memory (tune JDBC batch separately).");
    }
}
