package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

/**
 * What is the role of the EntityManager interface in JPA?
 */
public final class Lesson04 extends AbstractLesson {

    public Lesson04() {
        super(4, "What is the role of the EntityManager interface in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            demo(em);
        });
    }

    private void demo(EntityManager em) {
        Author a = new Author("EntityManager API");
        em.persist(a);
        Author found = em.find(Author.class, a.getId());
        System.out.println("find same id → managed instance: " + found.getName());
        em.detach(found);
        System.out.println("After detach, contains? " + em.contains(found));
    }
}
