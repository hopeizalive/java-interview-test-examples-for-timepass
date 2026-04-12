package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Job;
import com.example.jpa.interview.study.StudyContext;

/**
 * How do you handle concurrency and locking issues in a multi-threaded JPA application?
 */
public final class Lesson38 extends AbstractLesson {

    public Lesson38() {
        super(38, "How do you handle concurrency and locking issues in a multi-threaded JPA application?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("EntityManager is not thread-safe — one per request/thread.");
        ctx.withTransaction(em -> {
            Job j = new Job("Worker");
            em.persist(j);
            System.out.println("@Version optimistic field=" + j.getVersion());
        });
    }
}
