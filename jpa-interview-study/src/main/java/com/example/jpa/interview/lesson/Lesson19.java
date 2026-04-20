package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Reservation;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.LockModeType;

/**
 * Lesson 19 compares optimistic and pessimistic locking strategies in JPA.
 *
 * <p>It demonstrates version-based concurrency control and explicit DB-level write locking in
 * separate transactions to keep each behavior easy to observe.
 */
public final class Lesson19 extends AbstractLesson {

    public Lesson19() {
        super(19, "How does JPA handle optimistic and pessimistic locking?");
    }

    /**
     * Lesson 19: optimistic and pessimistic locking.
     *
     * <p><b>Purpose:</b> Show two concurrency-control approaches available through JPA.
     * <p><b>Role:</b> Introduces consistency controls for concurrent update scenarios.
     * <p><b>Demonstration:</b> Persists versioned Reservation, then loads Author with `PESSIMISTIC_WRITE`.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story phase A: optimistic locking relies on version columns.
        ctx.withTransaction(em -> {
            Reservation r = new Reservation("12A");
            em.persist(r);
            System.out.println("Optimistic @Version on Reservation starts at: " + r.getVersion());
        });

        // Story phase B: pessimistic locking acquires DB lock during read-for-update.
        ctx.withTransaction(em -> {
            Author a = new Author("Lock");
            em.persist(a);
            em.flush();
            Author locked = em.find(Author.class, a.getId(), LockModeType.PESSIMISTIC_WRITE);
            // Story observation: successful load with lock mode confirms lock acquisition path.
            System.out.println("Pessimistic lock loaded author id=" + locked.getId());
        });
    }
}
