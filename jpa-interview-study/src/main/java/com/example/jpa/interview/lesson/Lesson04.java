package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityManager;

/**
 * Lesson 4 demonstrates EntityManager as the primary API for persistence-context operations.
 *
 * <p>The code walks through persist, find, and detach so interview answers can describe both
 * data access and lifecycle-state control.
 */
public final class Lesson04 extends AbstractLesson {

    public Lesson04() {
        super(4, "What is the role of the EntityManager interface in JPA?");
    }

    /**
     * Lesson 4: EntityManager responsibilities and core operations.
     *
     * <p><b>Purpose:</b> Show EntityManager methods that create, read, and detach managed entities.
     * <p><b>Role:</b> Serves as the operational foundation for later query and transaction lessons.
     * <p><b>Demonstration:</b> Persists an Author, finds it by id, then detaches and checks containment.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story boundary: run the full interaction inside one transactional unit of work.
        ctx.withTransaction(em -> {
            demo(em);
        });
    }

    private void demo(EntityManager em) {
        // Story action: persist introduces the entity into managed state.
        Author a = new Author("EntityManager API");
        em.persist(a);
        // Story observation: find resolves by primary key through the current context/database.
        Author found = em.find(Author.class, a.getId());
        System.out.println("find same id → managed instance: " + found.getName());
        // Story takeaway: detach removes tracking; contains() should become false.
        em.detach(found);
        System.out.println("After detach, contains? " + em.contains(found));
    }
}
