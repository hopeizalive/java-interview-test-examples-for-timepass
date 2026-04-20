package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 28 summarizes practical JPA performance strategies for high-load systems.
 *
 * <p>It combines configuration tips with a reusable pagination demo, reflecting how interview
 * answers should pair principles with concrete techniques.
 */
public final class Lesson28 extends AbstractLesson {

    public Lesson28() {
        super(28, "What strategies do you use for optimizing JPA performance in high-traffic applications?");
    }

    /**
     * Lesson 28: JPA performance optimization strategies.
     *
     * <p><b>Purpose:</b> Highlight common optimization levers across query and write paths.
     * <p><b>Role:</b> Provides system-level guidance beyond single-API usage.
     * <p><b>Demonstration:</b> Logs key settings and references pagination as one core strategy.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: enumerate optimization options expected in senior-level interviews.
        System.out.println("Examples: JDBC batching (Hibernate), 2nd-level cache, lazy + fetch joins, pagination, DTO projections.");
        System.out.println("hibernate.jdbc.batch_size=30");
        System.out.println("hibernate.order_inserts=true");
        System.out.println("hibernate.order_updates=true");
        // Story action: invoke concrete paging demo as one optimization pattern.
        new Lesson15().run(ctx);
    }
}
