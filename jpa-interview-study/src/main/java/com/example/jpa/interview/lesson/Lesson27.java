package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Product;
import com.example.jpa.interview.study.StudyContext;

/**
 * Can you describe the process of implementing a custom converter in JPA?
 */
public final class Lesson27 extends AbstractLesson {

    public Lesson27() {
        super(27, "Can you describe the process of implementing a custom converter in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Product p = new Product("Desk", "   trim-me   ");
            em.persist(p);
            em.flush();
            em.clear();
            Product loaded = em.find(Product.class, p.getId());
            System.out.println("SKU after TrimConverter round-trip: '" + loaded.getSku() + "'");
        });
    }
}
