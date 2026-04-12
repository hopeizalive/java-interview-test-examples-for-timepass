package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * How do you use JPA in a distributed or microservices architecture?
 */
public final class Lesson45 extends AbstractLesson {

    public Lesson45() {
        super(45, "How do you use JPA in a distributed or microservices architecture?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("One service owns one schema/bounded context; no shared EntityManager across services.");
        System.out.println("Coordinate via APIs/events; avoid 2PC — prefer sagas/outbox.");
        new Lesson41().run(ctx);
    }
}
