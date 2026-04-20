package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.*;
import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 9 surveys common JPA relationship mappings using one compact data graph.
 *
 * <p>It combines one-to-one, one-to-many/many-to-one, and many-to-many examples so readers can
 * see owning-side setup patterns before deeper cascade/fetch tuning.
 */
public final class Lesson09 extends AbstractLesson {

    public Lesson09() {
        super(9, "How do you map relationships in JPA (One-to-One, One-to-Many, Many-to-One, Many-to-Many)?");
    }

    /**
     * Lesson 9: relationship mapping patterns.
     *
     * <p><b>Purpose:</b> Show how entity associations are wired before persistence.
     * <p><b>Role:</b> Establishes relational modeling context for join and fetch lessons.
     * <p><b>Demonstration:</b> Persists entities that cover 1-1, 1-M/M-1, and M-M mappings.
     */
    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            // Story setup: build a one-to-one pair.
            User user = new User("learner");
            UserProfile profile = new UserProfile("Bio text");
            user.setProfile(profile);

            // Story setup: build one-to-many/many-to-one link.
            Author author = new Author("Relation author");
            Book book = new Book("Relation book");
            author.addBook(book);
            // Story setup: attach many-to-many category association.
            Category cat = new Category("Fiction");
            book.getCategories().add(cat);

            // Story action: persist aggregate roots needed for each mapping demonstration.
            em.persist(user);
            em.persist(cat);
            em.persist(author);
            // Story takeaway: one run shows all major relationship shapes.
            System.out.println("Stored 1-1 user/profile, M-1 + 1-M author/book, M-M book/category.");
        });
    }
}
