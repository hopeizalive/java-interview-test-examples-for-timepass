package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Invoice;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

/**
 * Lesson 39 demonstrates auditing entity modifications via lifecycle callbacks.
 *
 * <p>It performs create and update phases, then verifies that callback-driven metadata is stamped
 * on mutation.
 */
public final class Lesson39 extends AbstractLesson {

    public Lesson39() {
        super(39, "Can you explain the process of auditing entity changes in JPA?");
    }

    /**
     * Lesson 39: auditing entity changes.
     *
     * <p><b>Purpose:</b> Show automatic update-time metadata handling.
     * <p><b>Role:</b> Builds practical understanding of callback-based audit fields.
     * <p><b>Demonstration:</b> Persists and updates Invoice, then prints callback-populated `lastModified`.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story phase A: create invoice and capture its id.
        final Long[] id = new Long[1];
        ctx.withTransaction(em -> {
            Invoice inv = new Invoice("INV-1");
            em.persist(inv);
            id[0] = inv.getId();
        });

        // Story phase B: modify entity to trigger pre-update auditing callback.
        ctx.withTransaction(em -> {
            Invoice inv = em.find(Invoice.class, id[0]);
            inv.setCode("INV-1-revised");
            em.flush();
        });

        // Story observation: reload and inspect audited timestamp set by listener.
        EntityManager em = ctx.studyEmf().createEntityManager();
        Invoice loaded = em.find(Invoice.class, id[0]);
        em.close();
        LocalDateTime ts = loaded.getLastModified();
        System.out.println("@PreUpdate listener stamped lastModified=" + ts);
    }
}
