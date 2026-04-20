package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 44 summarizes trade-offs between lazy and eager fetching.
 *
 * <p>It presents practical decision criteria and references a runnable lazy-loading pitfall example.
 */
public final class Lesson44 extends AbstractLesson {

    public Lesson44() {
        super(44, "What are the trade-offs between using lazy and eager fetching in JPA?");
    }

    /**
     * Lesson 44: lazy versus eager fetch trade-offs.
     *
     * <p><b>Purpose:</b> Explain cost/benefit of deferred versus immediate association loading.
     * <p><b>Role:</b> Supports fetch-plan design and N+1 prevention choices.
     * <p><b>Demonstration:</b> Logs trade-offs and invokes Lesson 11 lazy-loading behavior.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: compare both strategies in terms of query count and data volume.
        System.out.println("Lazy: fewer queries upfront; risk N+1 / LazyInitializationException.");
        System.out.println("Eager: simpler navigation; risk large joins / cartesian products.");
        // Story action: run lazy example to visualize one common failure mode.
        new Lesson11().run(ctx);
    }
}
