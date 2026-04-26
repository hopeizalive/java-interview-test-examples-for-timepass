package com.example.javadp.interview.lesson;

import com.example.javadp.interview.lesson.blocks.DesignPatternsBlock01CreationalGof;
import com.example.javadp.interview.lesson.blocks.DesignPatternsBlock02StructuralGof;
import com.example.javadp.interview.lesson.blocks.DesignPatternsBlock03BehavioralGof;
import com.example.javadp.interview.lesson.blocks.DesignPatternsBlock04ModernArchitectural;
import com.example.javadp.interview.study.StudyContext;

/**
 * Routes each {@link DesignPatternsLesson} to one of four blocks (creational, structural, behavioral, modern). New
 * enum constants must be assigned here so routing stays compile-time checked.
 */
public final class DesignPatternsLessonDispatch {

    private DesignPatternsLessonDispatch() {}

    public static void run(DesignPatternsLesson lesson, StudyContext ctx) {
        switch (lesson) {
            case L01, L02, L03, L04, L05 -> DesignPatternsBlock01CreationalGof.run(lesson, ctx);
            case L06, L07, L08, L09, L10, L11, L12 -> DesignPatternsBlock02StructuralGof.run(lesson, ctx);
            case L13, L14, L15, L16, L17, L18, L19, L20, L21, L22, L23 -> DesignPatternsBlock03BehavioralGof.run(lesson, ctx);
            case L24, L25, L26, L27, L28, L29, L30 -> DesignPatternsBlock04ModernArchitectural.run(lesson, ctx);
        }
    }
}
