package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Lesson 30 demonstrates the N+1 select problem and a fetch-join based fix.
 *
 * <p>It runs two query patterns back-to-back so learners can compare lazy-triggered extra selects
 * against single-query eager retrieval.
 */
public final class Lesson30 extends AbstractLesson {

    public Lesson30() {
        super(30, "Can you explain the concept of N+1 problem in JPA and how to resolve it?");
    }

    /**
     * Lesson 30: N+1 problem and resolution.
     *
     * <p><b>Purpose:</b> Show how lazy association access can explode query count.
     * <p><b>Role:</b> Core performance troubleshooting pattern for JPA interviews.
     * <p><b>Demonstration:</b> Executes baseline query causing extra loads, then uses `join fetch`.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: create author-book data used for both query approaches.
        ctx.withTransaction(em -> {
            Author a = new Author("N+1");
            a.addBook(new Book("One"));
            a.addBook(new Book("Two"));
            em.persist(a);
        });

        EntityManager em = ctx.studyEmf().createEntityManager();
        em.getTransaction().begin();
        // Story phase A: baseline query, then lazy access triggers additional selects.
        List<Book> books = em.createQuery("select b from Book b join b.author ar where ar.name = :n", Book.class)
                .setParameter("n", "N+1")
                .getResultList();
        for (Book b : books) {
            System.out.println("Lazy author load causes extra SELECT: " + b.getAuthor().getName());
        }
        em.getTransaction().commit();
        em.close();

        em = ctx.studyEmf().createEntityManager();
        em.getTransaction().begin();
        // Story phase B: fetch join loads association within the same query.
        List<Book> eager = em.createQuery(
                        "select distinct b from Book b join fetch b.author ar where ar.name = :n", Book.class)
                .setParameter("n", "N+1")
                .getResultList();
        // Story takeaway: fetch join avoids per-row lazy loading round-trips.
        System.out.println("JOIN FETCH loads authors in one query; batch size=" + eager.size());
        em.getTransaction().commit();
        em.close();
    }
}
