package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.entity.Category;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityGraph;

/**
 * Lesson 26 showcases entity graphs for controlling fetch plans in complex models.
 *
 * <p>It separates write setup and read optimization phases so learners can see how graph hints
 * reduce ad-hoc loading behavior in relationship-heavy domains.
 */
public final class Lesson26 extends AbstractLesson {

    public Lesson26() {
        super(26, "How do you handle complex mappings and relationships in JPA for large-scale applications?");
    }

    /**
     * Lesson 26: complex relationship loading with entity graphs.
     *
     * <p><b>Purpose:</b> Demonstrate explicit fetch-plan tuning for rich object graphs.
     * <p><b>Role:</b> Bridges mapping complexity to predictable query behavior.
     * <p><b>Demonstration:</b> Persists related entities, then reads Book using named fetch graph hint.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: create graph-shaped test data (author, book, category).
        ctx.withTransaction(em -> {
            Author a = new Author("Graph");
            Book b = new Book("Graph book");
            b.getCategories().add(new Category("SciFi"));
            a.addBook(b);
            em.persist(a);
        });

        // Story action: load with fetch graph to control associated data initialization.
        ctx.withTransaction(em -> {
            EntityGraph<?> graph = em.getEntityGraph(Book.GRAPH_WITH_DETAILS);
            Book b = em.createQuery("select distinct b from Book b", Book.class)
                    .setHint("jakarta.persistence.fetchgraph", graph)
                    .setMaxResults(1)
                    .getResultList()
                    .getFirst();
            // Story observation: accessing related fields shows graph-loaded associations.
            System.out.println("Entity graph loaded author=" + b.getAuthor().getName()
                    + ", categories=" + b.getCategories().size());
        });
    }
}
