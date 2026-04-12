package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * Can you differentiate between transient, persistent, and detached objects in JPA?
 */
public final class Lesson07 extends AbstractLesson {

    public Lesson07() {
        super(7, "Can you differentiate between transient, persistent, and detached objects in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author t = new Author("lifecycle");
            System.out.println("Before persist, contains? " + em.contains(t));
            em.persist(t);
            System.out.println("After persist (managed): " + em.contains(t));
            em.detach(t);
            System.out.println("After detach: " + em.contains(t));
        });
    }
}
