package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * How do you integrate JPA with other Java frameworks like Spring or Hibernate?
 */
public final class Lesson29 extends AbstractLesson {

    public Lesson29() {
        super(29, "How do you integrate JPA with other Java frameworks like Spring or Hibernate?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("Hibernate is a JPA provider — already used via persistence.xml provider.");
        System.out.println("Spring: @PersistenceContext EntityManager + @Transactional; or Spring Data JpaRepository.");
        System.out.println("This CLI module stays plain JPA; see jpa-interview-code-answers.md for Spring snippets.");
        new Lesson01().run(ctx);
    }
}
