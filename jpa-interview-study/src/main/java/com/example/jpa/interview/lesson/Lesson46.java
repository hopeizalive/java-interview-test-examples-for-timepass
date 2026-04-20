package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Document;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Lesson 46 demonstrates soft deletion using a boolean marker column.
 *
 * <p>It contrasts persisted active and logically deleted rows, then queries only active records to
 * show filtering strategy.
 */
public final class Lesson46 extends AbstractLesson {

    public Lesson46() {
        super(46, "Can you explain how to implement soft deletion in JPA entities?");
    }

    /**
     * Lesson 46: implementing soft delete.
     *
     * <p><b>Purpose:</b> Show logical deletion without physical row removal.
     * <p><b>Role:</b> Supports auditing/recovery-friendly data lifecycle design.
     * <p><b>Demonstration:</b> Marks one Document as deleted and queries only `deleted = false`.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: persist one active document baseline.
        ctx.withTransaction(em -> em.persist(new Document("Keep me")));

        // Story action: mark second document as logically deleted.
        ctx.withTransaction(em -> {
            Document d = new Document("Soft trash");
            em.persist(d);
            d.setDeleted(true);
        });

        // Story observation: active query excludes soft-deleted row.
        EntityManager em = ctx.studyEmf().createEntityManager();
        List<Document> active = em.createQuery("select d from Document d where d.deleted = false", Document.class)
                .getResultList();
        em.close();
        System.out.println("Active documents (deleted flag = false): " + active.size());
    }
}
