package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Lesson 37 covers native SQL usage from JPA and its trade-offs.
 *
 * <p>It demonstrates parameterized native query execution while calling out portability and
 * maintainability concerns versus JPQL.
 */
public final class Lesson37 extends AbstractLesson {

    public Lesson37() {
        super(37, "What are the implications of using native SQL queries in JPA?");
    }

    /**
     * Lesson 37: native SQL queries in JPA.
     *
     * <p><b>Purpose:</b> Show how to run vendor SQL when JPQL is insufficient.
     * <p><b>Role:</b> Adds escape-hatch strategy to query toolbox.
     * <p><b>Demonstration:</b> Inserts an Author, executes native SELECT with binding, and logs row count.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: persist row targeted by the native SQL query.
        ctx.withTransaction(em -> em.persist(new Author("Native")));

        EntityManager em = ctx.studyEmf().createEntityManager();
        // Story action: execute bound native SQL to avoid literal concatenation.
        @SuppressWarnings("unchecked")
        List<Number> ids = em.createNativeQuery("SELECT id FROM authors WHERE name = :n")
                .setParameter("n", "Native")
                .getResultList();
        em.close();
        // Story takeaway: native SQL is powerful but reduces portability across databases.
        System.out.println("Native SQL + bound parameter matched rows: " + ids.size() + " (watch portability/SQL injection).");
    }
}
