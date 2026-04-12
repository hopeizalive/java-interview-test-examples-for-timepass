package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Can you detail the process of integrating JPA with a NoSQL database?
 */
public final class Lesson42 extends AbstractLesson {

    public Lesson42() {
        super(42, "Can you detail the process of integrating JPA with a NoSQL database?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("JPA targets relational stores. For MongoDB/Cassandra, prefer native drivers or Spring Data modules.");
        System.out.println("Some providers experimented with JPA-style NoSQL mapping; most teams skip ORM for document DBs.");
    }
}
