package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 6 explains persistence context behavior as JPA's first-level tracking scope.
 *
 * <p>It highlights dirty checking so readers can describe why entity changes are synchronized
 * without calling explicit update APIs.
 */
public final class Lesson06 extends AbstractLesson {

    public Lesson06() {
        super(6, "What is a persistence context in JPA and why is it important?");
    }

    /**
     * Lesson 6: persistence context and dirty checking.
     *
     * <p><b>Purpose:</b> Show how managed entities are tracked and auto-synchronized.
     * <p><b>Role:</b> Introduces state management before detach/merge and caching discussions.
     * <p><b>Demonstration:</b> Changes an entity after persist and flushes without explicit update call.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story setup: create and persist an entity so it becomes managed in the context.
            Author a = new Author("Dirty checking");
            em.persist(a);
            // Story action: mutate the managed object; the context records this change.
            a.setName("Dirty checking — renamed");
            // Story boundary: flush forces SQL synchronization before transaction commit.
            em.flush();
            // Story takeaway: update is inferred from managed state transition, not manual SQL.
            System.out.println("No explicit em.update(...) — PC tracks changes until flush/commit.");
        });
    }
}
