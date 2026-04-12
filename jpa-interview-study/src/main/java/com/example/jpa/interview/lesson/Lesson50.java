package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * What are the challenges and solutions for integrating JPA in a cloud-native environment?
 */
public final class Lesson50 extends AbstractLesson {

    public Lesson50() {
        super(50, "What are the challenges and solutions for integrating JPA in a cloud-native environment?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("Pool sizing: spring.datasource.hikari.maximum-pool-size=10");
        System.out.println("Health checks, externalized config, readiness probes — match pool to container CPU/RAM.");
        System.out.println("Mitigate latency with caching; scale stateless app tier; use DB proxy/reader replicas when applicable.");
        new Lesson01().run(ctx);
    }
}
