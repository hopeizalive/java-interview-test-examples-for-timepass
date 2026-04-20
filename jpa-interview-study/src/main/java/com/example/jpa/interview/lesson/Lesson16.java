package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 16 focuses on `@Id` as the identity anchor for every JPA entity.
 *
 * <p>It keeps implementation lightweight by restating the rule, then reusing the key-generation
 * lesson that already demonstrates id assignment at runtime.
 */
public final class Lesson16 extends AbstractLesson {

    public Lesson16() {
        super(16, "What is the significance of the @Id annotation in JPA?");
    }

    /**
     * Lesson 16: significance of @Id.
     *
     * <p><b>Purpose:</b> Reinforce that every entity must define a primary-key attribute.
     * <p><b>Role:</b> Revisits identity fundamentals before advanced key and relation topics.
     * <p><b>Demonstration:</b> Prints the rule and delegates to Lesson 5 for concrete id generation flow.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: state the core identity contract required by JPA.
        System.out.println("@Id marks the primary key; every entity must have exactly one.");
        // Story action: reuse existing runnable example that persists and shows generated id.
        new Lesson05().run(ctx);
    }
}
