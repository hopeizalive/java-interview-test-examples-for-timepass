package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * What is a persistence context in JPA and why is it important?
 */
public final class Lesson06 extends AbstractLesson {

    public Lesson06() {
        super(6, "What is a persistence context in JPA and why is it important?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author a = new Author("Dirty checking");
            em.persist(a);
            a.setName("Dirty checking — renamed");
            em.flush();
            System.out.println("No explicit em.update(...) — PC tracks changes until flush/commit.");
        });
    }
}
