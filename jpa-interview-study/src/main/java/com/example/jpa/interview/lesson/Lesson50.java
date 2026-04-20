package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 50 summarizes cloud-native considerations for JPA-backed services.
 *
 * <p>It focuses on connection pool sizing, observability, and latency mitigation patterns relevant
 * to containerized deployments.
 */
public final class Lesson50 extends AbstractLesson {

    public Lesson50() {
        super(50, "What are the challenges and solutions for integrating JPA in a cloud-native environment?");
    }

    /**
     * Lesson 50: cloud-native JPA practices.
     *
     * <p><b>Purpose:</b> Highlight production concerns when running JPA in elastic environments.
     * <p><b>Role:</b> Concludes the module with operational architecture guidance.
     * <p><b>Demonstration:</b> Logs cloud-tuning recommendations and reuses baseline persistence flow.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: list key runtime controls in containerized deployments.
        System.out.println("Pool sizing: spring.datasource.hikari.maximum-pool-size=10");
        System.out.println("Health checks, externalized config, readiness probes — match pool to container CPU/RAM.");
        System.out.println("Mitigate latency with caching; scale stateless app tier; use DB proxy/reader replicas when applicable.");
        // Story action: retain a concrete transactional example as module anchor.
        new Lesson01().run(ctx);
    }
}
