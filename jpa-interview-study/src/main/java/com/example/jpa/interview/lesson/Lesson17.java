package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 17 demonstrates JPQL bulk DML for set-based data changes.
 *
 * <p>The key interview point is that bulk update/delete operates directly in the database and
 * reports affected row count.
 */
public final class Lesson17 extends AbstractLesson {

    public Lesson17() {
        super(17, "How can you perform a bulk update or delete in JPA?");
    }

    /**
     * Lesson 17: bulk update/delete with JPQL.
     *
     * <p><b>Purpose:</b> Show how to execute set-based updates without loading entities one by one.
     * <p><b>Role:</b> Extends query usage into write-optimized operations.
     * <p><b>Demonstration:</b> Seeds a Book row, runs bulk update query, and prints affected row count.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story setup: create data to be targeted by the bulk statement.
            Author a = new Author("Bulk");
            a.addBook(new Book("Title A"));
            em.persist(a);
            em.flush();

            // Story action: execute JPQL update directly in the database.
            int rows = em.createQuery("update Book b set b.title = :t where b.author.id = :id")
                    .setParameter("t", "Renamed in bulk")
                    .setParameter("id", a.getId())
                    .executeUpdate();
            // Story observation: affected row count confirms batch update scope.
            System.out.println("Bulk JPQL update affected rows: " + rows);
        });
    }
}
