package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 12 clarifies why `@Entity` is the entry point for ORM-managed domain types.
 *
 * <p>Instead of duplicating full mapping code, it references existing entities and reuses an
 * earlier runnable flow to keep the concept focused.
 */
public final class Lesson12 extends AbstractLesson {

    public Lesson12() {
        super(12, "What is the purpose of the @Entity annotation in JPA?");
    }

    /**
     * Lesson 12: the purpose of @Entity.
     *
     * <p><b>Purpose:</b> Explain that `@Entity` marks classes for persistence-context management.
     * <p><b>Role:</b> Reinforces metadata fundamentals before deeper annotation variants.
     * <p><b>Demonstration:</b> Prints the concept and reuses Lesson 1 runtime persistence path.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: anchor the annotation meaning in plain interview language.
        System.out.println("@Entity marks a managed type mapped to a table (see any class under entity/).");
        // Story action: reuse baseline flow so the concept ties to a real persist operation.
        new Lesson01().run(ctx);
    }
}
