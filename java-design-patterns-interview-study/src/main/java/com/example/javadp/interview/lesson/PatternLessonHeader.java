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

    /**
     * One concrete “where you’ve seen this” anchor for interview answers: domain, recognizable system, and how to tie
     * it back to the pattern.
     */
    public static void printRealWorldExample(
            StudyContext ctx, int index, String scenario, String whereYouSeeIt, String interviewAngle) {
        ctx.log("Real-world example " + index + ": " + scenario);
        ctx.log("  Where: " + whereYouSeeIt);
        ctx.log("  Interview angle: " + interviewAngle);
        ctx.log("");
    }
}
