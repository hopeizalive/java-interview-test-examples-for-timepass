package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Product;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 27 explains custom attribute converters in JPA.
 *
 * <p>It persists a value that should be normalized by converter logic and then reloads it to prove
 * database round-trip transformation.
 */
public final class Lesson27 extends AbstractLesson {

    public Lesson27() {
        super(27, "Can you describe the process of implementing a custom converter in JPA?");
    }

    /**
     * Lesson 27: custom converter implementation.
     *
     * <p><b>Purpose:</b> Show how converter hooks transform values during persist/read operations.
     * <p><b>Role:</b> Extends field-mapping control for domain normalization needs.
     * <p><b>Demonstration:</b> Persists Product with padded SKU and verifies trimmed value after reload.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story action: write raw value that converter should normalize.
            Product p = new Product("Desk", "   trim-me   ");
            em.persist(p);
            em.flush();
            em.clear();
            // Story observation: reloaded entity shows converted value from database round-trip.
            Product loaded = em.find(Product.class, p.getId());
            System.out.println("SKU after TrimConverter round-trip: '" + loaded.getSku() + "'");
        });
    }
}
