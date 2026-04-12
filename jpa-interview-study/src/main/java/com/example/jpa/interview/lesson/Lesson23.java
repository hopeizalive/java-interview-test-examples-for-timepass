package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.SeqAuthor;
import com.example.jpa.interview.study.StudyContext;

/**
 * How do you configure a JPA entity to use a sequence generator for primary keys?
 */
public final class Lesson23 extends AbstractLesson {

    public Lesson23() {
        super(23, "How do you configure a JPA entity to use a sequence generator for primary keys?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            SeqAuthor a = new SeqAuthor("Sequence PK");
            em.persist(a);
            System.out.println("GenerationType.SEQUENCE id=" + a.getId());
        });
    }
}
