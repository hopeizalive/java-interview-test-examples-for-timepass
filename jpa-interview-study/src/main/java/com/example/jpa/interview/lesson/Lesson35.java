package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 35 contrasts plain joins and fetch joins in JPQL.
 *
 * <p>It executes both forms on the same dataset so learners can see filtering semantics versus
 * eager association loading intent.
 */
public final class Lesson35 extends AbstractLesson {

    public Lesson35() {
        super(35, "Can you discuss the differences and use cases for JOIN FETCH and JOIN in JPQL?");
    }

    /**
     * Lesson 35: JOIN versus JOIN FETCH.
     *
     * <p><b>Purpose:</b> Explain when joins are for predicates and when joins should preload relations.
     * <p><b>Role:</b> Builds on N+1 mitigation patterns with focused syntax comparison.
     * <p><b>Demonstration:</b> Runs both query variants and logs resulting counts.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: persist one author-book pair used by both queries.
        ctx.withTransaction(em -> {
            Author a = new Author("Join");
            a.addBook(new Book("Join book"));
            em.persist(a);
        });

        // Story comparison: plain join for filtering, fetch join for eager relation loading.
        ctx.withTransaction(em -> {
            var joinOnly = em.createQuery("select b from Book b join b.author a where a.name = :n", Book.class)
                    .setParameter("n", "Join")
                    .getResultList();
            var fetch = em.createQuery(
                            "select distinct b from Book b join fetch b.author a where a.name = :n", Book.class)
                    .setParameter("n", "Join")
                    .getResultList();
            // Story observation: both match rows, but fetch join controls initialization behavior.
            System.out.println("JOIN filter count=" + joinOnly.size() + "; JOIN FETCH eager count=" + fetch.size());
        });
    }
}
