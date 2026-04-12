package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.StudyInvoice;
import com.example.jpa.interview.entity.StudyOrder;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * How do you configure and use multiple persistence units in JPA?
 */
public final class Lesson41 extends AbstractLesson {

    public Lesson41() {
        super(41, "How do you configure and use multiple persistence units in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        try (EntityManagerFactory orders = Persistence.createEntityManagerFactory("ordersPU");
             EntityManagerFactory billing = Persistence.createEntityManagerFactory("billingPU")) {

            EntityManager oem = orders.createEntityManager();
            oem.getTransaction().begin();
            oem.persist(new StudyOrder("ORD-42"));
            oem.getTransaction().commit();
            oem.close();

            EntityManager bem = billing.createEntityManager();
            bem.getTransaction().begin();
            bem.persist(new StudyInvoice("BILL-9"));
            bem.getTransaction().commit();
            bem.close();

            System.out.println("ordersPU and billingPU use separate in-memory H2 URLs (see persistence.xml).");
        }
    }
}
