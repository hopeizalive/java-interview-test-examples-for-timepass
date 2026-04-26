package com.example.javadp.interview.lesson.blocks;

import com.example.javadp.interview.lesson.DesignPatternsLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.ChainOfResponsibilityPatternLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.CommandPatternLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.InterpreterPatternLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.IteratorPatternLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.MediatorPatternLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.MementoPatternLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.ObserverPatternLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.StatePatternLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.StrategyPatternLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.TemplateMethodPatternLesson;
import com.example.javadp.interview.lesson.patterns.behavioral.VisitorPatternLesson;
import com.example.javadp.interview.study.StudyContext;

/**
 * <h2>Block 3 — Lessons 13–23: Gang-of-Four <em>behavioral</em> patterns</h2>
 *
 * <p>Behavioral patterns characterize <b>how objects collaborate and distribute responsibility</b>: algorithms,
 * notifications, state transitions, and visitor-style extensibility without endless conditionals.
 *
 * <p>Demos: {@code com.example.javadp.interview.lesson.patterns.behavioral}.
 */
public final class DesignPatternsBlock03BehavioralGof {

    private DesignPatternsBlock03BehavioralGof() {}

    public static void run(DesignPatternsLesson lesson, StudyContext ctx) {
        switch (lesson) {
            case L13 -> ChainOfResponsibilityPatternLesson.run(ctx);
            case L14 -> CommandPatternLesson.run(ctx);
            case L15 -> InterpreterPatternLesson.run(ctx);
            case L16 -> IteratorPatternLesson.run(ctx);
            case L17 -> MediatorPatternLesson.run(ctx);
            case L18 -> MementoPatternLesson.run(ctx);
            case L19 -> ObserverPatternLesson.run(ctx);
            case L20 -> StatePatternLesson.run(ctx);
            case L21 -> StrategyPatternLesson.run(ctx);
            case L22 -> TemplateMethodPatternLesson.run(ctx);
            case L23 -> VisitorPatternLesson.run(ctx);
            default -> throw new IllegalStateException("Block 3 received " + lesson);
        }
    }
}
