package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * What are the implications of using native SQL queries in JPA?
 */
public final class Lesson37 extends AbstractLesson {

    public Lesson37() {
        super(37, "What are the implications of using native SQL queries in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> em.persist(new Author("Native")));

        EntityManager em = ctx.studyEmf().createEntityManager();
        @SuppressWarnings("unchecked")
        List<Number> ids = em.createNativeQuery("SELECT id FROM authors WHERE name = :n")
                .setParameter("n", "Native")
                .getResultList();
        em.close();
        System.out.println("Native SQL + bound parameter matched rows: " + ids.size() + " (watch portability/SQL injection).");
    }
}
