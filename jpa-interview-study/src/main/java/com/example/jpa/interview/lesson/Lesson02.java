package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * How does JPA differ from JDBC in terms of database interaction?
 */
public final class Lesson02 extends AbstractLesson {

    public Lesson02() {
        super(2, "How does JPA differ from JDBC in terms of database interaction?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("JDBC: Connection + PreparedStatement + manual row mapping.");
        System.out.println("JPA: map objects; provider generates SQL and tracks instances in the persistence context.");
        ctx.withTransaction(em -> em.persist(new Author("JDBC vs JPA")));
        System.out.println("This module used EntityManager (object-level) instead of writing INSERT SQL.");
    }
}
