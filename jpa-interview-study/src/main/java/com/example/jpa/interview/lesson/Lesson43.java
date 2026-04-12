package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * How do you troubleshoot and debug performance issues in a JPA-based application?
 */
public final class Lesson43 extends AbstractLesson {

    public Lesson43() {
        super(43, "How do you troubleshoot and debug performance issues in a JPA-based application?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("Enable SQL logging: hibernate.show_sql / logging.level.org.hibernate.SQL=DEBUG");
        System.out.println("Watch N+1, use statistics (hibernate.generate_statistics), EXPLAIN in DB, profiler.");
        new Lesson30().run(ctx);
    }
}
