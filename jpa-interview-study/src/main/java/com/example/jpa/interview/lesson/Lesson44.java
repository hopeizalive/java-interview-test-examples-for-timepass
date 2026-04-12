package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * What are the trade-offs between using lazy and eager fetching in JPA?
 */
public final class Lesson44 extends AbstractLesson {

    public Lesson44() {
        super(44, "What are the trade-offs between using lazy and eager fetching in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("Lazy: fewer queries upfront; risk N+1 / LazyInitializationException.");
        System.out.println("Eager: simpler navigation; risk large joins / cartesian products.");
        new Lesson11().run(ctx);
    }
}
