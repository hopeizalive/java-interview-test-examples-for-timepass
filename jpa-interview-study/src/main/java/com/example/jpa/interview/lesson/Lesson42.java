package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 42 explains JPA's relational focus and NoSQL integration caveats.
 *
 * <p>It frames interview guidance around choosing fit-for-purpose data access technology rather
 * than forcing ORM abstraction onto document/column stores.
 */
public final class Lesson42 extends AbstractLesson {

    public Lesson42() {
        super(42, "Can you detail the process of integrating JPA with a NoSQL database?");
    }

    /**
     * Lesson 42: JPA and NoSQL integration reality.
     *
     * <p><b>Purpose:</b> Clarify that JPA is designed for relational databases.
     * <p><b>Role:</b> Helps candidates discuss technology boundaries and trade-offs.
     * <p><b>Demonstration:</b> Logs practical guidance on selecting native NoSQL tooling.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story takeaway: choose native NoSQL APIs/modules where relational ORM is not a fit.
        System.out.println("JPA targets relational stores. For MongoDB/Cassandra, prefer native drivers or Spring Data modules.");
        System.out.println("Some providers experimented with JPA-style NoSQL mapping; most teams skip ORM for document DBs.");
    }
}
