package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Article;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 25 demonstrates lifecycle callbacks triggered during entity persistence.
 *
 * <p>It uses an entity with pre-persist logic to show how audit-like fields can be filled without
 * manual service-layer timestamps.
 */
public final class Lesson25 extends AbstractLesson {

    public Lesson25() {
        super(25, "What are callbacks in JPA and how can they be used in entity lifecycle events?");
    }

    /**
     * Lesson 25: entity lifecycle callbacks.
     *
     * <p><b>Purpose:</b> Show callback hooks such as `@PrePersist` in action.
     * <p><b>Role:</b> Introduces lifecycle automation patterns for auditing concerns.
     * <p><b>Demonstration:</b> Persists Article and prints callback-populated `createdAt` value.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story action: persist triggers lifecycle callback methods on entity/listener.
            Article article = new Article("Lifecycle");
            em.persist(article);
            // Story observation: timestamp proves callback executed before insert.
            System.out.println("@PrePersist listener set createdAt=" + article.getCreatedAt());
        });
    }
}
