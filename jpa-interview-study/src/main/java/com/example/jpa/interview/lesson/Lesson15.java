package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

import java.util.stream.IntStream;

/**
 * Lesson 15 demonstrates JPQL pagination with deterministic ordering.
 *
 * <p>It seeds enough rows to show why `setFirstResult` and `setMaxResults` must be paired with
 * an `order by` clause for stable paging.
 */
public final class Lesson15 extends AbstractLesson {

    public Lesson15() {
        super(15, "How do you perform pagination in a JPQL query?");
    }

    /**
     * Lesson 15: paginating JPQL results.
     *
     * <p><b>Purpose:</b> Show how to retrieve one page from a larger result set.
     * <p><b>Role:</b> Extends query basics with production-style data slicing.
     * <p><b>Demonstration:</b> Inserts 15 books, then fetches page 2 using offset and limit.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story setup: create enough rows to make pagination visible.
            Author a = new Author("Paging");
            IntStream.range(0, 15).forEach(i -> a.addBook(new Book("Book " + i)));
            em.persist(a);

            // Story action: request second page (offset 5, size 5) with stable ordering.
            var page = em.createQuery("select b from Book b where b.author.id = :id order by b.title", Book.class)
                    .setParameter("id", a.getId())
                    .setFirstResult(5)
                    .setMaxResults(5)
                    .getResultList();
            // Story observation: page size confirms bounded retrieval.
            System.out.println("Page 2 (5 items): " + page.size());
        });
    }
}
