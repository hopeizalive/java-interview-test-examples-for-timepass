package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Invoice;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

/**
 * Can you explain the process of auditing entity changes in JPA?
 */
public final class Lesson39 extends AbstractLesson {

    public Lesson39() {
        super(39, "Can you explain the process of auditing entity changes in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        final Long[] id = new Long[1];
        ctx.withTransaction(em -> {
            Invoice inv = new Invoice("INV-1");
            em.persist(inv);
            id[0] = inv.getId();
        });

        ctx.withTransaction(em -> {
            Invoice inv = em.find(Invoice.class, id[0]);
            inv.setCode("INV-1-revised");
            em.flush();
        });

        EntityManager em = ctx.studyEmf().createEntityManager();
        Invoice loaded = em.find(Invoice.class, id[0]);
        em.close();
        LocalDateTime ts = loaded.getLastModified();
        System.out.println("@PreUpdate listener stamped lastModified=" + ts);
    }
}
