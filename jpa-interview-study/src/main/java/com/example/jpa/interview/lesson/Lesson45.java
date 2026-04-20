package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 45 maps JPA usage patterns to distributed and microservice architectures.
 *
 * <p>It emphasizes data ownership boundaries and coordination patterns instead of shared
 * transactional context across services.
 */
public final class Lesson45 extends AbstractLesson {

    public Lesson45() {
        super(45, "How do you use JPA in a distributed or microservices architecture?");
    }

    /**
     * Lesson 45: JPA in microservices.
     *
     * <p><b>Purpose:</b> Explain service-level data isolation and cross-service coordination.
     * <p><b>Role:</b> Connects persistence design to distributed system constraints.
     * <p><b>Demonstration:</b> Logs architectural guidance and references multiple-PU example.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: establish ownership and transaction boundaries per service.
        System.out.println("One service owns one schema/bounded context; no shared EntityManager across services.");
        System.out.println("Coordinate via APIs/events; avoid 2PC — prefer sagas/outbox.");
        // Story action: point to concrete multi-unit configuration pattern.
        new Lesson41().run(ctx);
    }
}
