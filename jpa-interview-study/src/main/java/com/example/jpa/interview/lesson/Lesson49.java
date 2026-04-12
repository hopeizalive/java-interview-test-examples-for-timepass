package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Can you discuss the integration of JPA with batch processing frameworks?
 */
public final class Lesson49 extends AbstractLesson {

    public Lesson49() {
        super(49, "Can you discuss the integration of JPA with batch processing frameworks?");
    }

    @Override
    public void run(StudyContext ctx) {
        EntityManager em = ctx.studyEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        final int batch = 20;
        for (int i = 0; i < batch; i++) {
            em.persist(new Author("batch-" + i));
            if (i % 5 == 0) {
                em.flush();
                em.clear();
            }
        }
        tx.commit();
        em.close();
        System.out.println("Chunk pattern: flush + clear to bound persistence context memory (tune JDBC batch separately).");
    }
}
