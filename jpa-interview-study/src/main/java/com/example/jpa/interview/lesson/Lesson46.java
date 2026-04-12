package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Document;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Can you explain how to implement soft deletion in JPA entities?
 */
public final class Lesson46 extends AbstractLesson {

    public Lesson46() {
        super(46, "Can you explain how to implement soft deletion in JPA entities?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> em.persist(new Document("Keep me")));

        ctx.withTransaction(em -> {
            Document d = new Document("Soft trash");
            em.persist(d);
            d.setDeleted(true);
        });

        EntityManager em = ctx.studyEmf().createEntityManager();
        List<Document> active = em.createQuery("select d from Document d where d.deleted = false", Document.class)
                .getResultList();
        em.close();
        System.out.println("Active documents (deleted flag = false): " + active.size());
    }
}
