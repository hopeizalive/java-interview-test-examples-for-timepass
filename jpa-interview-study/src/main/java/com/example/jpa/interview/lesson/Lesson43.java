package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 43 outlines a practical workflow for diagnosing JPA performance issues.
 *
 * <p>It combines instrumentation hints with a reusable N+1 scenario to connect symptoms to
 * root-cause analysis.
 */
public final class Lesson43 extends AbstractLesson {

    public Lesson43() {
        super(43, "How do you troubleshoot and debug performance issues in a JPA-based application?");
    }

    /**
     * Lesson 43: performance troubleshooting workflow.
     *
     * <p><b>Purpose:</b> Provide a debugging checklist for slow persistence operations.
     * <p><b>Role:</b> Turns optimization concepts into repeatable investigation steps.
     * <p><b>Demonstration:</b> Logs key diagnostics and reuses N+1 demo as representative bottleneck.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: enable observability before attempting query optimization.
        System.out.println("Enable SQL logging: hibernate.show_sql / logging.level.org.hibernate.SQL=DEBUG");
        System.out.println("Watch N+1, use statistics (hibernate.generate_statistics), EXPLAIN in DB, profiler.");
        // Story action: run known anti-pattern scenario for concrete troubleshooting.
        new Lesson30().run(ctx);
    }
}
