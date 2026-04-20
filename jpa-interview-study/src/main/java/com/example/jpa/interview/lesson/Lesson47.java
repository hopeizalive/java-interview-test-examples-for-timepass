package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 47 outlines security best practices around JPA-managed data.
 *
 * <p>It focuses on encryption, safe logging, and access-control layering rather than exposing
 * sensitive details in entity representations.
 */
public final class Lesson47 extends AbstractLesson {

    public Lesson47() {
        super(47, "What are the best practices for securing sensitive data in JPA entities?");
    }

    /**
     * Lesson 47: securing sensitive entity data.
     *
     * <p><b>Purpose:</b> Provide practical controls for confidentiality in persistence layers.
     * <p><b>Role:</b> Adds security perspective to data-model and converter topics.
     * <p><b>Demonstration:</b> Logs guidance and points to converter-based transformation pattern.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: list high-impact security controls around entity persistence.
        System.out.println("Do not log entities with secrets; encrypt at rest (AttributeConverter / DB features).");
        System.out.println("Enforce access at service layer (@PreAuthorize in Spring); validate input.");
        System.out.println("See TrimConverter in the codebase as a pattern for AttributeConverter.");
        // Story action: reuse converter lesson as concrete implementation anchor.
        new Lesson27().run(ctx);
    }
}
