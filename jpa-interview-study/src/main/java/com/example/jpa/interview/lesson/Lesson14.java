package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 14 explains cascade operations in parent-child associations.
 *
 * <p>It pairs a summary of cascade options with a persist example that demonstrates propagation
 * from aggregate root to related child entity.
 */
public final class Lesson14 extends AbstractLesson {

    public Lesson14() {
        super(14, "What is the CascadeType in JPA and what are its different types?");
    }

    /**
     * Lesson 14: CascadeType usage and options.
     *
     * <p><b>Purpose:</b> Clarify when parent operations should propagate to related entities.
     * <p><b>Role:</b> Supports relationship lessons by showing lifecycle coordination.
     * <p><b>Demonstration:</b> Persists Author with child Book and explains cascade behavior.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: list cascade options commonly discussed in interviews.
        System.out.println("CascadeType: ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH.");
        ctx.withTransaction(em -> {
            // Story action: persist only parent; cascade mapping handles child persistence.
            Author a = new Author("Cascade");
            a.addBook(new Book("Child book"));
            em.persist(a);
            // Story takeaway: configured mapping drives propagation semantics.
            System.out.println("CascadeType.ALL + orphanRemoval on authors.books persists Book with Author.");
        });
    }
}
