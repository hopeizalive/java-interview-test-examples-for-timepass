package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

import java.util.List;

/**
 * Lesson 10 introduces JPQL as an object-model query language.
 *
 * <p>The example emphasizes entity/field navigation instead of table/column syntax to explain how
 * providers translate JPQL into SQL at runtime.
 */
public final class Lesson10 extends AbstractLesson {

    public Lesson10() {
        super(10, "What is JPQL (Java Persistence Query Language) and how is it different from SQL?");
    }

    /**
     * Lesson 10: JPQL versus SQL perspective.
     *
     * <p><b>Purpose:</b> Demonstrate query authoring with entity names and path expressions.
     * <p><b>Role:</b> Starts the query-focused sequence after mapping fundamentals.
     * <p><b>Demonstration:</b> Persists sample data, runs a JPQL predicate with parameter binding, and prints result count.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story setup: create author-book data that can be queried by association path.
            Author a = new Author("JPQL Author");
            Book b = new Book("JPQL Book");
            a.addBook(b);
            em.persist(a);

            // Story action: query using entity alias and attribute path, not table/column names.
            List<Book> books = em.createQuery(
                            "select bk from Book bk where bk.author.name = :n", Book.class)
                    .setParameter("n", "JPQL Author")
                    .getResultList();
            // Story observation: non-zero count confirms JPQL translation and execution.
            System.out.println("JPQL uses entity/attribute paths; provider emits SQL: count=" + books.size());
        });
    }
}
