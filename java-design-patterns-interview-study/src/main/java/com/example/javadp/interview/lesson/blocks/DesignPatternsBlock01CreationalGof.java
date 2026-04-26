package com.example.javadp.interview.lesson.blocks;

import com.example.javadp.interview.lesson.DesignPatternsLesson;
import com.example.javadp.interview.lesson.patterns.creational.AbstractFactoryPatternLesson;
import com.example.javadp.interview.lesson.patterns.creational.BuilderPatternLesson;
import com.example.javadp.interview.lesson.patterns.creational.FactoryMethodPatternLesson;
import com.example.javadp.interview.lesson.patterns.creational.PrototypePatternLesson;
import com.example.javadp.interview.lesson.patterns.creational.SingletonPatternLesson;
import com.example.javadp.interview.study.StudyContext;

/**
 * <h2>Block 1 — Lessons 1–5: Gang-of-Four <em>creational</em> patterns</h2>
 *
 * <p>Creational patterns answer: <b>who constructs objects, and how do we hide construction complexity?</b> They keep
 * object graphs flexible when requirements change (new product families, new configuration steps, new cloning rules).
 *
 * <p>Interview arc: explain why Singleton is often discouraged except for true OS-level resources; when Factory Method
 * beats {@code new}; how Abstract Factory differs from Factory Method; why Builder helps with many optional
 * parameters; when Prototype avoids subclass explosion.
 *
 * <p>Runnable demos live in {@code com.example.javadp.interview.lesson.patterns.creational} — one class per pattern.
 */
public final class DesignPatternsBlock01CreationalGof {

    private DesignPatternsBlock01CreationalGof() {}

    public static void run(DesignPatternsLesson lesson, StudyContext ctx) {
        switch (lesson) {
            case L01 -> SingletonPatternLesson.run(ctx);
            case L02 -> FactoryMethodPatternLesson.run(ctx);
            case L03 -> AbstractFactoryPatternLesson.run(ctx);
            case L04 -> BuilderPatternLesson.run(ctx);
            case L05 -> PrototypePatternLesson.run(ctx);
            default -> throw new IllegalStateException("Block 1 received " + lesson);
        }
    }
}
