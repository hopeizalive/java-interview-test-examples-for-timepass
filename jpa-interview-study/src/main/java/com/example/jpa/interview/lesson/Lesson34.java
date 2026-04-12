package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * How do you implement caching in JPA to enhance application performance?
 */
public final class Lesson34 extends AbstractLesson {

    public Lesson34() {
        super(34, "How do you implement caching in JPA to enhance application performance?");
    }

    @Override
    public void run(StudyContext ctx) {
        System.out.println("L1 cache = persistence context (per EntityManager).");
        System.out.println("L2: @Cacheable + provider region factory, e.g. hibernate-jcache + ehcache.");
        System.out.println("hibernate.cache.use_second_level_cache=true");
        new Lesson04().run(ctx);
    }
}
