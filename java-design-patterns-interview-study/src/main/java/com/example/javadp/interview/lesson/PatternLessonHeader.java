package com.example.javadp.interview.lesson;

import com.example.javadp.interview.study.StudyContext;

/**
 * Prints a consistent banner so every lesson clearly segregates <em>which pattern</em> is in focus, which catalog
 * bucket it belongs to (GoF creational / structural / behavioral vs modern architectural idioms), and what the
 * accompanying code is meant to prove.
 */
public final class PatternLessonHeader {

    private PatternLessonHeader() {}

    public static void print(
            StudyContext ctx,
            String patternName,
            String taxonomyLabel,
            String theorySummary,
            String codeDemonstrates) {
        ctx.log("");
        ctx.log("--- Pattern focus: " + patternName + " ---");
        ctx.log("Taxonomy: " + taxonomyLabel);
        ctx.log("Theory (short): " + theorySummary);
        ctx.log("This runnable snippet shows: " + codeDemonstrates);
        ctx.log("");
    }
}
