package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * How do you manage database versioning and backward compatibility in JPA applications?
 */
public final class Lesson48 extends AbstractLesson {

    public Lesson48() {
        super(48, "How do you manage database versioning and backward compatibility in JPA applications?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("Use Flyway/Liquibase; additive migrations first; @Version for optimistic locking rows.");
        System.out.println("Example DDL: ALTER TABLE authors ADD COLUMN nickname VARCHAR(200);");
        new Lesson40().run(ctx);
    }
}
