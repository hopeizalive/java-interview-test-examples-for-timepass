package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Ticket;
import com.example.jpa.interview.study.StudyContext;

/**
 * What is the role of the @Version annotation in JPA, and how does it work?
 */
public final class Lesson40 extends AbstractLesson {

    public Lesson40() {
        super(40, "What is the role of the @Version annotation in JPA, and how does it work?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Ticket t = new Ticket("Support");
            em.persist(t);
            System.out.println("Initial @Version=" + t.getVersion());
        });
    }
}
