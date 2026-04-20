package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 48 covers schema versioning and backward-compatible rollout practices.
 *
 * <p>It combines migration guidelines with optimistic-lock versioning reminder for data safety
 * during iterative releases.
 */
public final class Lesson48 extends AbstractLesson {

    public Lesson48() {
        super(48, "How do you manage database versioning and backward compatibility in JPA applications?");
    }

    /**
     * Lesson 48: database versioning and compatibility.
     *
     * <p><b>Purpose:</b> Explain controlled schema evolution in live systems.
     * <p><b>Role:</b> Aligns database change strategy with JPA model evolution.
     * <p><b>Demonstration:</b> Logs additive migration approach and references versioned-entity lesson.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: favor additive, versioned migrations for safe rolling deployments.
        System.out.println("Use Flyway/Liquibase; additive migrations first; @Version for optimistic locking rows.");
        System.out.println("Example DDL: ALTER TABLE authors ADD COLUMN nickname VARCHAR(200);");
        // Story action: tie guidance to a concrete @Version example.
        new Lesson40().run(ctx);
    }
}
