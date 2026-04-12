package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

import java.util.List;

/**
 * What is JPQL (Java Persistence Query Language) and how is it different from SQL?
 */
public final class Lesson10 extends AbstractLesson {

    public Lesson10() {
        super(10, "What is JPQL (Java Persistence Query Language) and how is it different from SQL?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author a = new Author("JPQL Author");
            Book b = new Book("JPQL Book");
            a.addBook(b);
            em.persist(a);

            List<Book> books = em.createQuery(
                            "select bk from Book bk where bk.author.name = :n", Book.class)
                    .setParameter("n", "JPQL Author")
                    .getResultList();
            System.out.println("JPQL uses entity/attribute paths; provider emits SQL: count=" + books.size());
        });
    }
}
