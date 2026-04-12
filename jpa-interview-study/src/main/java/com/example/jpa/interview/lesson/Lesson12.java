package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * What is the purpose of the @Entity annotation in JPA?
 */
public final class Lesson12 extends AbstractLesson {

    public Lesson12() {
        super(12, "What is the purpose of the @Entity annotation in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("@Entity marks a managed type mapped to a table (see any class under entity/).");
        new Lesson01().run(ctx);
    }
}
