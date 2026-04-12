package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

/**
 * How can you perform a bulk update or delete in JPA?
 */
public final class Lesson17 extends AbstractLesson {

    public Lesson17() {
        super(17, "How can you perform a bulk update or delete in JPA?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author a = new Author("Bulk");
            a.addBook(new Book("Title A"));
            em.persist(a);
            em.flush();

            int rows = em.createQuery("update Book b set b.title = :t where b.author.id = :id")
                    .setParameter("t", "Renamed in bulk")
                    .setParameter("id", a.getId())
                    .executeUpdate();
            System.out.println("Bulk JPQL update affected rows: " + rows);
        });
    }
}
