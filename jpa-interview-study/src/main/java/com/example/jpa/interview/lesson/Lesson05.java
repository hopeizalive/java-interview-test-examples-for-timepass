package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * How would you define and use a primary key in JPA?
 */
public final class Lesson05 extends AbstractLesson {

    public Lesson05() {
        super(5, "How would you define and use a primary key in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author a = new Author("Primary key");
            em.persist(a);
            System.out.println("@Id + @GeneratedValue(IDENTITY) assigned: " + a.getId());
        });
    }
}
