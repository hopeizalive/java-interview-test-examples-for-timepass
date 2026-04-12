package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Address;
import com.example.jpa.interview.entity.Customer;
import com.example.jpa.interview.study.StudyContext;

/**
 * What is an Embedded Object in JPA and how is it used?
 */
public final class Lesson20 extends AbstractLesson {

    public Lesson20() {
        super(20, "What is an Embedded Object in JPA and how is it used?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Customer c = new Customer("ACME", new Address("1 Main", "Oslo", "0001"));
            em.persist(c);
            System.out.println("Customer row embeds Address columns in same table.");
        });
    }
}
