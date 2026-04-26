package com.example.javadp.interview.lesson.blocks;

import com.example.javadp.interview.lesson.DesignPatternsLesson;
import com.example.javadp.interview.lesson.patterns.structural.AdapterPatternLesson;
import com.example.javadp.interview.lesson.patterns.structural.BridgePatternLesson;
import com.example.javadp.interview.lesson.patterns.structural.CompositePatternLesson;
import com.example.javadp.interview.lesson.patterns.structural.DecoratorPatternLesson;
import com.example.javadp.interview.lesson.patterns.structural.FacadePatternLesson;
import com.example.javadp.interview.lesson.patterns.structural.FlyweightPatternLesson;
import com.example.javadp.interview.lesson.patterns.structural.ProxyPatternLesson;
import com.example.javadp.interview.study.StudyContext;

/**
 * <h2>Block 2 — Lessons 6–12: Gang-of-Four <em>structural</em> patterns</h2>
 *
 * <p>Structural patterns explain <b>how classes and objects compose</b> to form larger structures while keeping
 * relationships flexible (adapter for legacy, bridge for dimensions, composite for trees, decorator for layering,
 * facade for simplicity, flyweight for sharing, proxy for controlled access).
 *
 * <p>Demos: {@code com.example.javadp.interview.lesson.patterns.structural}.
 */
public final class DesignPatternsBlock02StructuralGof {

    private DesignPatternsBlock02StructuralGof() {}

    public static void run(DesignPatternsLesson lesson, StudyContext ctx) {
        switch (lesson) {
            case L06 -> AdapterPatternLesson.run(ctx);
            case L07 -> BridgePatternLesson.run(ctx);
            case L08 -> CompositePatternLesson.run(ctx);
            case L09 -> DecoratorPatternLesson.run(ctx);
            case L10 -> FacadePatternLesson.run(ctx);
            case L11 -> FlyweightPatternLesson.run(ctx);
            case L12 -> ProxyPatternLesson.run(ctx);
            default -> throw new IllegalStateException("Block 2 received " + lesson);
        }
    }
}
