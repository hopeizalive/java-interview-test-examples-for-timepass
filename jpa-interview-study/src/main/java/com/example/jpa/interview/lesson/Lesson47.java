package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * What are the best practices for securing sensitive data in JPA entities?
 */
public final class Lesson47 extends AbstractLesson {

    public Lesson47() {
        super(47, "What are the best practices for securing sensitive data in JPA entities?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("Do not log entities with secrets; encrypt at rest (AttributeConverter / DB features).");
        System.out.println("Enforce access at service layer (@PreAuthorize in Spring); validate input.");
        System.out.println("See TrimConverter in the codebase as a pattern for AttributeConverter.");
        new Lesson27().run(ctx);
    }
}
