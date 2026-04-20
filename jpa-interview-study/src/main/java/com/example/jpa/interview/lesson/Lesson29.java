package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 29 discusses how JPA fits with common Java ecosystem frameworks.
 *
 * <p>It clarifies provider versus framework roles and keeps the runnable sample plain JPA so
 * abstraction boundaries remain explicit.
 */
public final class Lesson29 extends AbstractLesson {

    public Lesson29() {
        super(29, "How do you integrate JPA with other Java frameworks like Spring or Hibernate?");
    }

    /**
     * Lesson 29: integrating JPA with Hibernate/Spring.
     *
     * <p><b>Purpose:</b> Explain integration patterns and responsibilities across framework layers.
     * <p><b>Role:</b> Connects interview theory to practical stack composition.
     * <p><b>Demonstration:</b> Logs provider/framework usage guidance and reuses baseline JPA flow.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: distinguish JPA spec, provider implementation, and framework integration.
        System.out.println("Hibernate is a JPA provider — already used via persistence.xml provider.");
        System.out.println("Spring: @PersistenceContext EntityManager + @Transactional; or Spring Data JpaRepository.");
        System.out.println("This CLI module stays plain JPA; see jpa-interview-code-answers.md for Spring snippets.");
        // Story action: keep a concrete JPA execution path in the same lesson.
        new Lesson01().run(ctx);
    }
}
