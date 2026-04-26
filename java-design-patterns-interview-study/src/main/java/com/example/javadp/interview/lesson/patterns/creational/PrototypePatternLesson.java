package com.example.javadp.interview.lesson.patterns.creational;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.List;

/**
 * GoF Prototype: clone or copy from a template to avoid expensive construction or subclass explosion of parallel factory
 * hierarchies.
 */
public final class PrototypePatternLesson {

    private PrototypePatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Kubernetes Pod templates",
                "Controllers clone pod specs (labels, volumes, probes) for each replica instead of hand-authoring N YAMLs.",
                "Emphasize cheap duplication from a canonical prototype configuration.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Spring bean @Scope(\"prototype\")",
                "Each getBean returns a fresh instance copied from the same definition metadata.",
                "Contrast with singleton scope - prototype is literally a new object per getBean from the same template.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Game engines and entity prefabs",
                "Designers place prefabs; runtime duplicates components/physics from the archetype for each enemy spawn.",
                "Java angle: prefer copy constructors or factories over Cloneable's brittle contracts.");

        ctx.log("--- Java core tie-in ---");
        int[] original = {1, 2, 3};
        int[] shallowCopy = original.clone();
        shallowCopy[0] = 99;
        ctx.log("int[].clone() copies array body: original[0]=" + original[0] + " clone[0]=" + shallowCopy[0]);

        PatternLessonHeader.print(
                ctx,
                "Prototype",
                "GoF Creational",
                "Clone or copy-from-template to duplicate configured objects cheaply.",
                "SlideDeck copies slides list from a template deck without re-parsing source files.");
        SlideDeck template = new SlideDeck("Keynote", List.of("Intro", "Architecture", "Q&A"));
        SlideDeck copyForCustomerA = template.copy();
        ctx.log("Template title=" + template.title() + " slides=" + template.slides());
        ctx.log("Copy title=" + copyForCustomerA.title() + " slides=" + copyForCustomerA.slides());
        ctx.log("Different objects? " + (template != copyForCustomerA));
    }

    private record SlideDeck(String title, List<String> slides) {
        SlideDeck copy() {
            return new SlideDeck(title + " (copy)", List.copyOf(slides));
        }
    }
}
