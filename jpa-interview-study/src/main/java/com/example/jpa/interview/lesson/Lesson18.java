package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

/**
 * What are named queries in JPA and how are they declared?
 */
public final class Lesson18 extends AbstractLesson {

    public Lesson18() {
        super(18, "What are named queries in JPA and how are they declared?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author a = new Author("Named");
            a.addBook(new Book("Named book"));
            em.persist(a);

            var books = em.createNamedQuery(Book.NAMED_FIND_BY_AUTHOR_NAME, Book.class)
                    .setParameter("name", "Named")
                    .getResultList();
            System.out.println("@NamedQuery on Book: " + books.size());
        });
    }
}
