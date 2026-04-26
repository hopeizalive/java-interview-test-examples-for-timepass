package com.example.javadp.interview.lesson;

import com.example.javadp.interview.study.StudyContext;
import com.example.javadp.interview.study.StudyLesson;

/**
 * Catalog of lessons: twenty-three Gang-of-Four patterns in book order (creational, structural, behavioral), plus
 * widely used modern architectural idioms (repository, specification, CQRS, circuit breaker, pool, null object,
 * dependency injection). Each enum entry is one numbered lesson.
 *
 * <p>Blocks in {@link com.example.javadp.interview.lesson.blocks} route to per-pattern classes under
 * {@code com.example.javadp.interview.lesson.patterns}. Each lesson prints {@link PatternLessonHeader} output (including
 * real-world anchors), then runs a small demo.
 *
 * <p><b>Sources consulted for scope:</b> classic GoF catalog; contemporary pattern catalogs such as
 * <a href="https://java-design-patterns.com/patterns">java-design-patterns.com</a> (broad modern list including
 * circuit breaker, CQRS, repository, specification); interview materials still anchor on GoF plus these service-layer
 * patterns.
 */
public enum DesignPatternsLesson implements StudyLesson {

    L01(1, "[GoF Creational] Singleton - single instance & global access."),
    L02(2, "[GoF Creational] Factory Method - subclass decides which product to create."),
    L03(3, "[GoF Creational] Abstract Factory - families of related objects."),
    L04(4, "[GoF Creational] Builder - stepwise construction of a complex object."),
    L05(5, "[GoF Creational] Prototype - clone as a cheaper alternative to subclassing factories."),

    L06(6, "[GoF Structural] Adapter - translate one interface into another."),
    L07(7, "[GoF Structural] Bridge - decouple abstraction from implementation so both vary."),
    L08(8, "[GoF Structural] Composite - tree structures, uniform leaf and composite API."),
    L09(9, "[GoF Structural] Decorator - attach responsibilities without subclass explosion."),
    L10(10, "[GoF Structural] Facade - one simple entry over a complex subsystem."),
    L11(11, "[GoF Structural] Flyweight - share intrinsic state, keep extrinsic context outside."),
    L12(12, "[GoF Structural] Proxy - surrogate controls access (lazy, protection, remote)."),

    L13(13, "[GoF Behavioral] Chain of Responsibility - pass along a handler chain."),
    L14(14, "[GoF Behavioral] Command - encapsulate request as an object (undo/log)."),
    L15(15, "[GoF Behavioral] Interpreter - grammar + AST evaluation for simple languages."),
    L16(16, "[GoF Behavioral] Iterator - sequential access without exposing internals."),
    L17(17, "[GoF Behavioral] Mediator - objects talk through a hub to reduce coupling."),
    L18(18, "[GoF Behavioral] Memento - capture & restore object state without breaking encapsulation."),
    L19(19, "[GoF Behavioral] Observer - notify dependents automatically on change."),
    L20(20, "[GoF Behavioral] State - object changes class-like behavior when internal state changes."),
    L21(21, "[GoF Behavioral] Strategy - interchangeable algorithms behind one interface."),
    L22(22, "[GoF Behavioral] Template Method - skeleton in base class, hooks in subclasses."),
    L23(23, "[GoF Behavioral] Visitor - new operations on a stable object structure without editing each type."),

    L24(24, "[Modern / DDD] Repository - persistence-oriented collection facade over aggregates."),
    L25(25, "[Modern / DDD] Specification - composable business rules reusable in validation and queries."),
    L26(26, "[Modern / Distributed] CQRS - segregate commands (writes) from queries (reads)."),
    L27(27, "[Modern / Resilience] Circuit Breaker - fail fast when a dependency is unhealthy."),
    L28(28, "[Modern / Performance] Object Pool - reuse expensive objects instead of churn."),
    L29(29, "[Modern / Robustness] Null Object - replace null references with a no-op behavior."),
    L30(30, "[Modern / Wiring] Dependency Injection - depend on abstractions; compose from the outside.");

    private final int number;
    private final String title;

    DesignPatternsLesson(int number, String title) {
        this.number = number;
        this.title = title;
    }

    @Override
    public int number() {
        return number;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public void run(StudyContext ctx) throws Exception {
        DesignPatternsLessonDispatch.run(this, ctx);
    }
}
