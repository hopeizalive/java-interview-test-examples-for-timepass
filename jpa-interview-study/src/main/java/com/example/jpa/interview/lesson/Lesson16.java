package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * What is the significance of the @Id annotation in JPA?
 */
public final class Lesson16 extends AbstractLesson {

    public Lesson16() {
        super(16, "What is the significance of the @Id annotation in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("@Id marks the primary key; every entity must have exactly one.");
        new Lesson05().run(ctx);
    }
}
