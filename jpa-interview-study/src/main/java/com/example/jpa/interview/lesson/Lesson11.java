package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.OrderLine;
import com.example.jpa.interview.entity.Product;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import org.hibernate.LazyInitializationException;

/**
 * How does JPA handle lazy and eager loading?
 */
public final class Lesson11 extends AbstractLesson {

    public Lesson11() {
        super(11, "How does JPA handle lazy and eager loading?");
    }

    @Override
    public void run(StudyContext ctx) {
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
        Product proxy = loaded.getProduct();
        em.close();

        try {
            System.out.println(proxy.getName());
        } catch (LazyInitializationException e) {
            System.out.println("Caught LazyInitializationException when touching LAZY assoc outside session.");
        }
    }
}
