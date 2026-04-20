package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.StudyInvoice;
import com.example.jpa.interview.entity.StudyOrder;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Lesson 41 demonstrates using multiple persistence units in one application.
 *
 * <p>It opens separate EntityManagerFactory instances to show isolated datasource and model
 * boundaries for modular architectures.
 */
public final class Lesson41 extends AbstractLesson {

    public Lesson41() {
        super(41, "How do you configure and use multiple persistence units in JPA?");
    }

    /**
     * Lesson 41: configuring multiple persistence units.
     *
     * <p><b>Purpose:</b> Show how independent units can target different databases/schemas.
     * <p><b>Role:</b> Supports modular and service-boundary data design discussions.
     * <p><b>Demonstration:</b> Persists data through `ordersPU` and `billingPU` separately.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: create two independent persistence units.
        try (EntityManagerFactory orders = Persistence.createEntityManagerFactory("ordersPU");
             EntityManagerFactory billing = Persistence.createEntityManagerFactory("billingPU")) {

            // Story action: write through first unit.
            EntityManager oem = orders.createEntityManager();
            oem.getTransaction().begin();
            oem.persist(new StudyOrder("ORD-42"));
            oem.getTransaction().commit();
            oem.close();

            // Story action: write through second unit.
            EntityManager bem = billing.createEntityManager();
            bem.getTransaction().begin();
            bem.persist(new StudyInvoice("BILL-9"));
            bem.getTransaction().commit();
            bem.close();

            // Story takeaway: each PU can map to isolated storage configuration.
            System.out.println("ordersPU and billingPU use separate in-memory H2 URLs (see persistence.xml).");
        }
    }
}
