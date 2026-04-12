package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * Can you explain the difference between merge and persist methods in JPA?
 */
public final class Lesson24 extends AbstractLesson {

    public Lesson24() {
        super(24, "Can you explain the difference between merge and persist methods in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        final Long[] id = new Long[1];
        ctx.withTransaction(em -> {
            Author a = new Author("persist-only");
            em.persist(a);
            id[0] = a.getId();
        });

        ctx.withTransaction(em -> {
            Author managed = em.find(Author.class, id[0]);
            em.detach(managed);
            managed.setName("merged-name");
            Author reattached = em.merge(managed);
            System.out.println("merge returned managed instance with name=" + reattached.getName());
        });
    }
}
