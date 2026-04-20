package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 2 contrasts object-centric JPA with SQL-centric JDBC workflow.
 *
 * <p>The example focuses on interview-level differences in abstraction, mapping, and unit-of-work
 * behavior rather than raw driver APIs.
 */
public final class Lesson02 extends AbstractLesson {

    public Lesson02() {
        super(2, "How does JPA differ from JDBC in terms of database interaction?");
    }

    /**
     * Lesson 2: JPA versus JDBC interaction style.
     *
     * <p><b>Purpose:</b> Explain how JPA delegates SQL generation and object mapping.
     * <p><b>Role:</b> Builds on Lesson 1 to justify why EntityManager is used in this study path.
     * <p><b>Demonstration:</b> Persists an Author without writing INSERT SQL, then states the contrast.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: state the manual steps JDBC usually requires.
        System.out.println("JDBC: Connection + PreparedStatement + manual row mapping.");
        // Story comparison: clarify JPA's persistence-context-driven model.
        System.out.println("JPA: map objects; provider generates SQL and tracks instances in the persistence context.");
        // Story action: perform the same write through EntityManager API.
        ctx.withTransaction(em -> em.persist(new Author("JDBC vs JPA")));
        // Story takeaway: emphasize what changed from an interview perspective.
        System.out.println("This module used EntityManager (object-level) instead of writing INSERT SQL.");
    }
}
