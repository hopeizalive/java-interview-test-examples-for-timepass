package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * What are the best practices for managing transactions in JPA for consistency and integrity?
 */
public final class Lesson31 extends AbstractLesson {

    public Lesson31() {
        super(31, "What are the best practices for managing transactions in JPA for consistency and integrity?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("Keep transactions short; rollback on failure; choose isolation/propagation in Spring.");
        EntityManager em = ctx.studyEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(new Author("Tx integrity"));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        System.out.println("Pattern: begin → work → commit; catch → rollback.");
    }
}
