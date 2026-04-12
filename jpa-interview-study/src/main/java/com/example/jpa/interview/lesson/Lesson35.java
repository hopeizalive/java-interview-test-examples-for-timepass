package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

/**
 * Can you discuss the differences and use cases for JOIN FETCH and JOIN in JPQL?
 */
public final class Lesson35 extends AbstractLesson {

    public Lesson35() {
        super(35, "Can you discuss the differences and use cases for JOIN FETCH and JOIN in JPQL?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author a = new Author("Join");
            a.addBook(new Book("Join book"));
            em.persist(a);
        });

        ctx.withTransaction(em -> {
            var joinOnly = em.createQuery("select b from Book b join b.author a where a.name = :n", Book.class)
                    .setParameter("n", "Join")
                    .getResultList();
            var fetch = em.createQuery(
                            "select distinct b from Book b join fetch b.author a where a.name = :n", Book.class)
                    .setParameter("n", "Join")
                    .getResultList();
            System.out.println("JOIN filter count=" + joinOnly.size() + "; JOIN FETCH eager count=" + fetch.size());
        });
    }
}
