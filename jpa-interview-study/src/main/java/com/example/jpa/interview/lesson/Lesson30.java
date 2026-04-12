package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Can you explain the concept of N+1 problem in JPA and how to resolve it?
 */
public final class Lesson30 extends AbstractLesson {

    public Lesson30() {
        super(30, "Can you explain the concept of N+1 problem in JPA and how to resolve it?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author a = new Author("N+1");
            a.addBook(new Book("One"));
            a.addBook(new Book("Two"));
            em.persist(a);
        });

        EntityManager em = ctx.studyEmf().createEntityManager();
        em.getTransaction().begin();
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
        List<Book> eager = em.createQuery(
                        "select distinct b from Book b join fetch b.author ar where ar.name = :n", Book.class)
                .setParameter("n", "N+1")
                .getResultList();
        System.out.println("JOIN FETCH loads authors in one query; batch size=" + eager.size());
        em.getTransaction().commit();
        em.close();
    }
}
