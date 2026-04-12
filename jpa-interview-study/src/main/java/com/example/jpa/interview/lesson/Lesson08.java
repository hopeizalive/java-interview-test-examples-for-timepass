package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * What is an EntityTransaction in JPA and how is it used?
 */
public final class Lesson08 extends AbstractLesson {

    public Lesson08() {
        super(8, "What is an EntityTransaction in JPA and how is it used?");
    }

    @Override
    public void run(StudyContext ctx) {
        EntityManager em = ctx.studyEmf().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(new Author("Transaction commit"));
            tx.commit();
            System.out.println("Committed successfully.");
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
