package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

/**
 * What is the CascadeType in JPA and what are its different types?
 */
public final class Lesson14 extends AbstractLesson {

    public Lesson14() {
        super(14, "What is the CascadeType in JPA and what are its different types?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("CascadeType: ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH.");
        ctx.withTransaction(em -> {
            Author a = new Author("Cascade");
            a.addBook(new Book("Child book"));
            em.persist(a);
            System.out.println("CascadeType.ALL + orphanRemoval on authors.books persists Book with Author.");
        });
    }
}
