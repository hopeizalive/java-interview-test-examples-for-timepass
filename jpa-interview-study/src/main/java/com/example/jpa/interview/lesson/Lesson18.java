package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 18 shows how named queries are declared once and reused by name.
 *
 * <p>The lesson emphasizes readability and centralized query maintenance versus scattering JPQL
 * literals across service methods.
 */
public final class Lesson18 extends AbstractLesson {

    public Lesson18() {
        super(18, "What are named queries in JPA and how are they declared?");
    }

    /**
     * Lesson 18: named query declaration and usage.
     *
     * <p><b>Purpose:</b> Demonstrate query reuse through metadata-defined query names.
     * <p><b>Role:</b> Follows dynamic JPQL with a maintainability-oriented alternative.
     * <p><b>Demonstration:</b> Persists data and executes `Book.NAMED_FIND_BY_AUTHOR_NAME`.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story setup: persist data matching the named query predicate.
            Author a = new Author("Named");
            a.addBook(new Book("Named book"));
            em.persist(a);

            // Story action: execute named query by symbolic key instead of inline JPQL.
            var books = em.createNamedQuery(Book.NAMED_FIND_BY_AUTHOR_NAME, Book.class)
                    .setParameter("name", "Named")
                    .getResultList();
            // Story observation: result size proves query registration and execution path.
            System.out.println("@NamedQuery on Book: " + books.size());
        });
    }
}
