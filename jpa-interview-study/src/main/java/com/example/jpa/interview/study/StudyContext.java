package com.example.jpa.interview.study;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.function.Consumer;

/** Per-run scope: holds the primary {@code studyPU} factory used by most lessons. */
public final class StudyContext {

    private final EntityManagerFactory studyEmf;

    public StudyContext(EntityManagerFactory studyEmf) {
        this.studyEmf = studyEmf;
    }

    public EntityManagerFactory studyEmf() {
        return studyEmf;
    }

    public void withTransaction(Consumer<EntityManager> work) {
        EntityManager em = studyEmf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            work.accept(em);
            tx.commit();
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
