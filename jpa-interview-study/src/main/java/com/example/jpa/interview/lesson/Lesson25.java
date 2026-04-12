package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Article;
import com.example.jpa.interview.study.StudyContext;

/**
 * What are callbacks in JPA and how can they be used in entity lifecycle events?
 */
public final class Lesson25 extends AbstractLesson {

    public Lesson25() {
        super(25, "What are callbacks in JPA and how can they be used in entity lifecycle events?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Article article = new Article("Lifecycle");
            em.persist(article);
            System.out.println("@PrePersist listener set createdAt=" + article.getCreatedAt());
        });
    }
}
