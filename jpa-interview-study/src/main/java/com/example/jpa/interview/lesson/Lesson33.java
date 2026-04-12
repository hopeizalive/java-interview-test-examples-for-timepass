package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * What advanced techniques do you use for query optimization in JPQL or Criteria API?
 */
public final class Lesson33 extends AbstractLesson {

    public Lesson33() {
        super(33, "What advanced techniques do you use for query optimization in JPQL or Criteria API?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author a = new Author("Criteria");
            a.addBook(new Book("Java persistence"));
            em.persist(a);
        });

        EntityManager em = ctx.studyEmf().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> b = cq.from(Book.class);
        cq.select(b).where(cb.like(cb.lower(b.get("title")), "%persist%"));
        List<Book> books = em.createQuery(cq).getResultList();
        em.close();
        System.out.println("Criteria API matched books: " + books.size());
    }
}
