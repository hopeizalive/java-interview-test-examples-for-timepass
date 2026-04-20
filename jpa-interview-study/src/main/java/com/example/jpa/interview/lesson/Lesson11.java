package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.OrderLine;
import com.example.jpa.interview.entity.Product;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import org.hibernate.LazyInitializationException;

/**
 * Lesson 11 illustrates lazy-loading behavior and its session-boundary implications.
 *
 * <p>It intentionally accesses a lazy association after closing EntityManager to surface the
 * common `LazyInitializationException` interview scenario.
 */
public final class Lesson11 extends AbstractLesson {

    public Lesson11() {
        super(11, "How does JPA handle lazy and eager loading?");
    }

    /**
     * Lesson 11: lazy versus eager loading behavior.
     *
     * <p><b>Purpose:</b> Show when lazy proxies require an open persistence context.
     * <p><b>Role:</b> Prepares learners for fetch-strategy and query tuning discussions.
     * <p><b>Demonstration:</b> Loads an OrderLine, closes EntityManager, then triggers lazy access failure.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: create data with a lazily associated product.
        final Long[] lineId = {null};
        ctx.withTransaction(em -> {
            Product p = new Product("Lazy demo", "SKU");
            em.persist(p);
            OrderLine line = new OrderLine(p);
            em.persist(line);
            lineId[0] = line.getId();
        });

        EntityManager em = ctx.studyEmf().createEntityManager();
        OrderLine loaded = em.find(OrderLine.class, lineId[0]);
        // Story action: hold lazy proxy reference but close session before touching it.
        Product proxy = loaded.getProduct();
        em.close();

        try {
            System.out.println(proxy.getName());
        } catch (LazyInitializationException e) {
            // Story observation: lazy association access fails outside managed context.
            System.out.println("Caught LazyInitializationException when touching LAZY assoc outside session.");
        }
    }
}
