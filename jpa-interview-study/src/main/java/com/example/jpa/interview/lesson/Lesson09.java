package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.*;
import com.example.jpa.interview.study.StudyContext;

/**
 * How do you map relationships in JPA (One-to-One, One-to-Many, Many-to-One, Many-to-Many)?
 */
public final class Lesson09 extends AbstractLesson {

    public Lesson09() {
        super(9, "How do you map relationships in JPA (One-to-One, One-to-Many, Many-to-One, Many-to-Many)?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            User user = new User("learner");
            UserProfile profile = new UserProfile("Bio text");
            user.setProfile(profile);

            Author author = new Author("Relation author");
            Book book = new Book("Relation book");
            author.addBook(book);
            Category cat = new Category("Fiction");
            book.getCategories().add(cat);

            em.persist(user);
            em.persist(cat);
            em.persist(author);
            System.out.println("Stored 1-1 user/profile, M-1 + 1-M author/book, M-M book/category.");
        });
    }
}
