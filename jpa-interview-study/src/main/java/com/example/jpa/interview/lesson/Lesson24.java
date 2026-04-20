package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 24 contrasts `persist` for new entities with `merge` for detached state.
 *
 * <p>The sequence intentionally detaches an entity before merge so the lifecycle distinction is
 * observable and interview-ready.
 */
public final class Lesson24 extends AbstractLesson {

    public Lesson24() {
        super(24, "Can you explain the difference between merge and persist methods in JPA?");
    }

    /**
     * Lesson 24: persist versus merge.
     *
     * <p><b>Purpose:</b> Clarify when to use persist and when to use merge.
     * <p><b>Role:</b> Deepens entity-state lifecycle handling after detach basics.
     * <p><b>Demonstration:</b> Persists new Author, detaches it, mutates it, then merges and logs returned managed instance.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story phase A: persist creates and stores a brand-new entity.
        final Long[] id = new Long[1];
        ctx.withTransaction(em -> {
            Author a = new Author("persist-only");
            em.persist(a);
            id[0] = a.getId();
        });

        // Story phase B: detached entity is reattached with merge.
        ctx.withTransaction(em -> {
            Author managed = em.find(Author.class, id[0]);
            em.detach(managed);
            managed.setName("merged-name");
            Author reattached = em.merge(managed);
            // Story observation: merge returns the managed copy used for persistence tracking.
            System.out.println("merge returned managed instance with name=" + reattached.getName());
        });
    }
}
