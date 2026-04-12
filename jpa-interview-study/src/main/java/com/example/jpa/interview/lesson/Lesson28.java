package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * What strategies do you use for optimizing JPA performance in high-traffic applications?
 */
public final class Lesson28 extends AbstractLesson {

    public Lesson28() {
        super(28, "What strategies do you use for optimizing JPA performance in high-traffic applications?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("Examples: JDBC batching (Hibernate), 2nd-level cache, lazy + fetch joins, pagination, DTO projections.");
        System.out.println("hibernate.jdbc.batch_size=30");
        System.out.println("hibernate.order_inserts=true");
        System.out.println("hibernate.order_updates=true");
        new Lesson15().run(ctx);
    }
}
