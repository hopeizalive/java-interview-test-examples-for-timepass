package com.example.javadp.interview.lesson;

import com.example.javadp.interview.lesson.blocks.DesignPatternsBlock01CreationalGof;
import com.example.javadp.interview.lesson.blocks.DesignPatternsBlock02StructuralGof;
import com.example.javadp.interview.lesson.blocks.DesignPatternsBlock03BehavioralGof;
import com.example.javadp.interview.lesson.blocks.DesignPatternsBlock04ModernArchitectural;
import com.example.javadp.interview.study.StudyContext;

/**
 * Routes each {@link DesignPatternsLesson} to one of four blocks: GoF creational (1–5), structural (6–12),
 * behavioral (13–23), modern architectural (24–30).
 */
public final class DesignPatternsLessonDispatch {

    private DesignPatternsLessonDispatch() {}

    public static void run(DesignPatternsLesson lesson, StudyContext ctx) {
        int n = lesson.number();
        if (n <= 5) {
            DesignPatternsBlock01CreationalGof.run(lesson, ctx);
        } else if (n <= 12) {
            DesignPatternsBlock02StructuralGof.run(lesson, ctx);
        } else if (n <= 23) {
            DesignPatternsBlock03BehavioralGof.run(lesson, ctx);
        } else {
            DesignPatternsBlock04ModernArchitectural.run(lesson, ctx);
        }
    }
}
