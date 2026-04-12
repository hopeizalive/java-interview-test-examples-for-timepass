package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

/**
 * What is Java Persistence API (JPA) and what are its primary components?
 */
public final class Lesson01 extends AbstractLesson {

    public Lesson01() {
        super(1, "What is Java Persistence API (JPA) and what are its primary components?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("Components: Entity, EntityManager, Persistence Unit, EntityManagerFactory, JPQL/Criteria.");
        ctx.withTransaction(em -> {
            Author author = new Author("Components demo");
            em.persist(author);
            System.out.println("EntityManager.persist → assigned id: " + author.getId());
        });
    }
}
