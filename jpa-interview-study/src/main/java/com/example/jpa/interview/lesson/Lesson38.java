package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Job;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 38 outlines concurrency considerations for multi-threaded JPA workloads.
 *
 * <p>It emphasizes EntityManager thread confinement and version-based optimistic locking as the
 * default contention-control approach.
 */
public final class Lesson38 extends AbstractLesson {

    public Lesson38() {
        super(38, "How do you handle concurrency and locking issues in a multi-threaded JPA application?");
    }

    /**
     * Lesson 38: concurrency and locking in multi-threaded applications.
     *
     * <p><b>Purpose:</b> Reinforce safe threading model and optimistic version checks.
     * <p><b>Role:</b> Connects runtime architecture concerns to JPA locking features.
     * <p><b>Demonstration:</b> Logs thread-safety rule and persists versioned Job entity.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: highlight non-thread-safe nature of EntityManager instances.
        System.out.println("EntityManager is not thread-safe — one per request/thread.");
        ctx.withTransaction(em -> {
            // Story action: persist versioned entity used for optimistic concurrency control.
            Job j = new Job("Worker");
            em.persist(j);
            // Story observation: initial version value shows optimistic lock column in use.
            System.out.println("@Version optimistic field=" + j.getVersion());
        });
    }
}
