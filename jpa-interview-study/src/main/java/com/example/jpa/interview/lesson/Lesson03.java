package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * Can you explain the concept of JPA Entity and how it is declared?
 */
public final class Lesson03 extends AbstractLesson {

    public Lesson03() {
        super(3, "Can you explain the concept of JPA Entity and how it is declared?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("See entity.Author: @Entity, @Table, @Id + managed lifecycle.");
        ctx.withTransaction(em -> em.persist(new Author("Entity declaration")));
    }
}
