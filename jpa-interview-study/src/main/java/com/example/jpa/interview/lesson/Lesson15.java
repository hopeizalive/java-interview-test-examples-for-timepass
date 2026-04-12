package com.example.jpa.interview.lesson;

import com.example.jpa.interview.entity.Author;
import com.example.jpa.interview.entity.Book;
import com.example.jpa.interview.study.StudyContext;

import java.util.stream.IntStream;

/**
 * How do you perform pagination in a JPQL query?
 */
public final class Lesson15 extends AbstractLesson {

    public Lesson15() {
        super(15, "How do you perform pagination in a JPQL query?");
    }

    @Override
    public void run(StudyContext ctx) {
        ctx.withTransaction(em -> {
            Author a = new Author("Paging");
            IntStream.range(0, 15).forEach(i -> a.addBook(new Book("Book " + i)));
            em.persist(a);

            var page = em.createQuery("select b from Book b where b.author.id = :id order by b.title", Book.class)
                    .setParameter("id", a.getId())
                    .setFirstResult(5)
                    .setMaxResults(5)
                    .getResultList();
            System.out.println("Page 2 (5 items): " + page.size());
        });
    }
}
