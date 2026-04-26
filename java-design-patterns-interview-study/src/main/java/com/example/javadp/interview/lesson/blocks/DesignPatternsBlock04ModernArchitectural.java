package com.example.javadp.interview.lesson.blocks;

import com.example.javadp.interview.lesson.DesignPatternsLesson;
import com.example.javadp.interview.lesson.patterns.modern.CircuitBreakerPatternLesson;
import com.example.javadp.interview.lesson.patterns.modern.CqrsPatternLesson;
import com.example.javadp.interview.lesson.patterns.modern.DependencyInjectionPatternLesson;
import com.example.javadp.interview.lesson.patterns.modern.NullObjectPatternLesson;
import com.example.javadp.interview.lesson.patterns.modern.ObjectPoolPatternLesson;
import com.example.javadp.interview.lesson.patterns.modern.RepositoryPatternLesson;
import com.example.javadp.interview.lesson.patterns.modern.SpecificationPatternLesson;
import com.example.javadp.interview.study.StudyContext;

/**
 * <h2>Block 4 — Lessons 24–30: widely used <em>modern</em> patterns (not all GoF)</h2>
 *
 * <p>These names appear constantly in Java microservices, DDD-style domains, and resilience libraries. They complement
 * GoF: repositories hide persistence mechanics, specifications compose business rules, CQRS separates read/write models,
 * circuit breakers protect callers, pools amortize expensive resources, null objects remove {@code null} checks, and
 * dependency injection inverts control for testability.
 *
 * <p>Contemporary catalogs (for example <a href="https://java-design-patterns.com/patterns">java-design-patterns.com</a>)
 * document many of these alongside GoF; interviews often expect you to connect both worlds.
 *
 * <p>Demos: {@code com.example.javadp.interview.lesson.patterns.modern}.
 */
public final class DesignPatternsBlock04ModernArchitectural {

    private DesignPatternsBlock04ModernArchitectural() {}

    public static void run(DesignPatternsLesson lesson, StudyContext ctx) {
        switch (lesson) {
            case L24 -> RepositoryPatternLesson.run(ctx);
            case L25 -> SpecificationPatternLesson.run(ctx);
            case L26 -> CqrsPatternLesson.run(ctx);
            case L27 -> CircuitBreakerPatternLesson.run(ctx);
            case L28 -> ObjectPoolPatternLesson.run(ctx);
            case L29 -> NullObjectPatternLesson.run(ctx);
            case L30 -> DependencyInjectionPatternLesson.run(ctx);
            default -> throw new IllegalStateException("Block 4 received " + lesson);
        }
    }
}
