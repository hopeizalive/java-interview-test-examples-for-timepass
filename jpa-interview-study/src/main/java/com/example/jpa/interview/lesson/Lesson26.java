package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.entity.Category;
import com.example.jpa.interview.study.StudyContext;
import jakarta.persistence.EntityGraph;

/**
 * How do you handle complex mappings and relationships in JPA for large-scale applications?
 */
public final class Lesson26 extends AbstractLesson {

    public Lesson26() {
        super(26, "How do you handle complex mappings and relationships in JPA for large-scale applications?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author a = new Author("Graph");
            Book b = new Book("Graph book");
            b.getCategories().add(new Category("SciFi"));
            a.addBook(b);
            em.persist(a);
        });

        ctx.withTransaction(em -> {
            EntityGraph<?> graph = em.getEntityGraph(Book.GRAPH_WITH_DETAILS);
            Book b = em.createQuery("select distinct b from Book b", Book.class)
                    .setHint("jakarta.persistence.fetchgraph", graph)
                    .setMaxResults(1)
                    .getResultList()
                    .getFirst();
            System.out.println("Entity graph loaded author=" + b.getAuthor().getName()
                    + ", categories=" + b.getCategories().size());
        });
    }
}
