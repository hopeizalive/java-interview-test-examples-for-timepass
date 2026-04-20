package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyContext;

/**
 * Lesson 34 summarizes first-level and second-level caching in JPA stacks.
 *
 * <p>It frames cache layers and key provider settings, then ties back to EntityManager behavior.
 */
public final class Lesson34 extends AbstractLesson {

    public Lesson34() {
        super(34, "How do you implement caching in JPA to enhance application performance?");
    }

    /**
     * Lesson 34: caching strategies in JPA.
     *
     * <p><b>Purpose:</b> Distinguish persistence-context cache from shared second-level cache.
     * <p><b>Role:</b> Adds cache literacy to performance optimization lessons.
     * <p><b>Demonstration:</b> Logs L1/L2 guidance and reuses EntityManager operations from Lesson 4.
     */
    @Override
    public void run(StudyContext ctx) {
        // Story setup: explain cache layers and required provider configuration.
        System.out.println("L1 cache = persistence context (per EntityManager).");
        System.out.println("L2: @Cacheable + provider region factory, e.g. hibernate-jcache + ehcache.");
        System.out.println("hibernate.cache.use_second_level_cache=true");
        // Story action: invoke a lesson where EntityManager lifecycle effects are visible.
        new Lesson04().run(ctx);
    }
}
