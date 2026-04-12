package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * How do you handle database schema migrations in a JPA-based application?
 */
public final class Lesson32 extends AbstractLesson {

    public Lesson32() {
        super(32, "How do you handle database schema migrations in a JPA-based application?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("Production: Flyway/Liquibase versioned DDL; set ddl-auto=validate (Spring) or equivalent.");
        System.out.println("Example: src/main/resources/db/migration/V1__init.sql");
        new Lesson01().run(ctx);
    }
}
