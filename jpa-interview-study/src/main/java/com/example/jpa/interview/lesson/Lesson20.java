package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Address;
import com.example.jpa.interview.entity.Customer;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 20 introduces embeddables as reusable value-object mappings.
 *
 * <p>It maps `Address` into `Customer` to demonstrate that embedded fields are stored in the same
 * table as the owning entity rather than separate entity tables.
 */
public final class Lesson20 extends AbstractLesson {

    public Lesson20() {
        super(20, "What is an Embedded Object in JPA and how is it used?");
    }

    /**
     * Lesson 20: embedded object mapping.
     *
     * <p><b>Purpose:</b> Show how value objects can be composed into entity schema.
     * <p><b>Role:</b> Expands modeling options beyond entity associations.
     * <p><b>Demonstration:</b> Persists Customer with embedded Address and prints storage implication.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story action: create owner entity with embedded value object payload.
            Customer c = new Customer("ACME", new Address("1 Main", "Oslo", "0001"));
            em.persist(c);
            // Story takeaway: embedded attributes are flattened into owner table columns.
            System.out.println("Customer row embeds Address columns in same table.");
        });
    }
}
