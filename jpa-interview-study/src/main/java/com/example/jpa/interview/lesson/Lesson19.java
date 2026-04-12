package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Reservation;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.LockModeType;

/**
 * How does JPA handle optimistic and pessimistic locking?
 */
public final class Lesson19 extends AbstractLesson {

    public Lesson19() {
        super(19, "How does JPA handle optimistic and pessimistic locking?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Reservation r = new Reservation("12A");
            em.persist(r);
            System.out.println("Optimistic @Version on Reservation starts at: " + r.getVersion());
        });

        ctx.withTransaction(em -> {
            Author a = new Author("Lock");
            em.persist(a);
            em.flush();
            Author locked = em.find(Author.class, a.getId(), LockModeType.PESSIMISTIC_WRITE);
            System.out.println("Pessimistic lock loaded author id=" + locked.getId());
        });
    }
}
