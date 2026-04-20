package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 32 covers schema migration discipline in JPA applications.
 *
 * <p>It emphasizes versioned DDL tools and runtime validation over automatic production schema
 * mutation.
 */
public final class Lesson32 extends AbstractLesson {

    public Lesson32() {
        super(32, "How do you handle database schema migrations in a JPA-based application?");
    }

    /**
     * Lesson 32: schema migration strategy.
     *
     * <p><b>Purpose:</b> Explain how schema evolution should be managed safely in production.
     * <p><b>Role:</b> Connects ORM mapping changes to database release practices.
     * <p><b>Demonstration:</b> Logs Flyway/Liquibase guidance and references a baseline runnable lesson.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: summarize recommended migration workflow.
        System.out.println("Production: Flyway/Liquibase versioned DDL; set ddl-auto=validate (Spring) or equivalent.");
        System.out.println("Example: src/main/resources/db/migration/V1__init.sql");
        // Story action: keep linkage to a known-good baseline persistence flow.
        new Lesson01().run(ctx);
    }
}
