package com.example.springannotation.interview.lesson;

import com.example.springannotation.interview.lesson.blocks.LessonBlock01StereotypesAndContext;
import com.example.springannotation.interview.lesson.blocks.LessonBlock02ConfigurationAndWeb;
import com.example.springannotation.interview.lesson.blocks.LessonBlock03WebBindingAndTransactions;
import com.example.springannotation.interview.lesson.blocks.LessonBlock04ValidationAndTestingNarrative;
import com.example.springannotation.interview.lesson.blocks.LessonBlock05AsyncCachingAndRetry;
import com.example.springannotation.interview.study.StudyContext;

/**
 * Routes each {@link AnnotationLesson} constant to one of five lesson blocks (10 lessons per block).
 *
 * <p>The enum stays a small catalog; all runnable stories live in {@code blocks.*} so readers open one
 * “chapter” file at a time.
 */
public final class AnnotationLessonDispatch {

    private AnnotationLessonDispatch() {}

    public static void run(AnnotationLesson lesson, StudyContext ctx) throws Exception {
        int n = lesson.number();
        if (n <= 10) {
            LessonBlock01StereotypesAndContext.run(lesson, ctx);
        } else if (n <= 20) {
            LessonBlock02ConfigurationAndWeb.run(lesson, ctx);
        } else if (n <= 30) {
            LessonBlock03WebBindingAndTransactions.run(lesson, ctx);
        } else if (n <= 40) {
            LessonBlock04ValidationAndTestingNarrative.run(lesson, ctx);
        } else {
            LessonBlock05AsyncCachingAndRetry.run(lesson, ctx);
        }
    }
}
