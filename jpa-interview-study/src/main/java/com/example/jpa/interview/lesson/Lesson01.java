package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 1 introduces the core JPA building blocks used across this module.
 *
 * <p>It connects interview terminology (entity, persistence context, and EntityManager) to one
 * small transaction so the reader sees how these parts fit together in practice.
 */
public final class Lesson01 extends AbstractLesson {

    public Lesson01() {
        super(1, "What is Java Persistence API (JPA) and what are its primary components?");
    }

    /**
     * Lesson 1: JPA core components in one flow.
     *
     * <p><b>Purpose:</b> Show how an entity becomes managed and persisted through EntityManager.
     * <p><b>Role:</b> Establishes shared vocabulary before relation, query, and transaction topics.
     * <p><b>Demonstration:</b> Persists one Author and prints the generated id as observable proof.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: name the moving parts before executing persistence operations.
        System.out.println("Components: Entity, EntityManager, Persistence Unit, EntityManagerFactory, JPQL/Criteria.");
        ctx.withTransaction(em -> {
            // Story action: create a plain entity instance, then make it managed with persist().
            Author author = new Author("Components demo");
            em.persist(author);
            // Story observation: generated id confirms insert orchestration through JPA.
            System.out.println("EntityManager.persist → assigned id: " + author.getId());
        });
    }
}
