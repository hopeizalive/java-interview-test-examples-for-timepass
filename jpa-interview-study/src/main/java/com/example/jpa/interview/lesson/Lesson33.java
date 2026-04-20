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
 * Lesson 33 demonstrates criteria-based query construction for flexible optimization.
 *
 * <p>It separates dataset setup from typed query building so readers can focus on dynamic predicate
 * construction patterns.
 */
public final class Lesson33 extends AbstractLesson {

    public Lesson33() {
        super(33, "What advanced techniques do you use for query optimization in JPQL or Criteria API?");
    }

    /**
     * Lesson 33: advanced query optimization with Criteria API.
     *
     * <p><b>Purpose:</b> Show type-safe, programmatic query building for dynamic filters.
     * <p><b>Role:</b> Complements JPQL string queries with an API-driven alternative.
     * <p><b>Demonstration:</b> Persists sample data, builds criteria query with `lower`/`like`, and prints matches.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: seed a row that should match criteria condition.
        ctx.withTransaction(em -> {
            Author a = new Author("Criteria");
            a.addBook(new Book("Java persistence"));
            em.persist(a);
        });

        // Story action: assemble criteria query with composable predicates.
        EntityManager em = ctx.studyEmf().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> b = cq.from(Book.class);
        cq.select(b).where(cb.like(cb.lower(b.get("title")), "%persist%"));
        List<Book> books = em.createQuery(cq).getResultList();
        em.close();
        // Story observation: result count verifies criteria predicate behavior.
        System.out.println("Criteria API matched books: " + books.size());
    }
}
