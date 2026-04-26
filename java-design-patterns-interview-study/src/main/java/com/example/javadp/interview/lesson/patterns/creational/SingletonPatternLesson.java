package com.example.javadp.interview.lesson.patterns.creational;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Singleton: one shared instance and a well-known access point. Prefer enum singleton in Java when you truly need
 * one instance (Effective Java).
 */
public final class SingletonPatternLesson {

    private SingletonPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Java runtime environment",
                "java.lang.Runtime#getRuntime() returns the single Runtime for the JVM process.",
                "Contrast true OS singletons with abuse: most so-called singletons in apps are better as injected beans.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Spring default scope",
                "A Spring @Component with default singleton scope shares one instance per ApplicationContext.",
                "Interview: explain lifecycle vs enum singleton; testability often favors explicit DI over static access.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Logging framework roots",
                "SLF4J's LoggerFactory bridges to one binding per classloader - global coordination without sprinkling new.",
                "Mention classloader isolation: a singleton per classloader is not one instance for the whole distributed system.");

        ctx.log("--- Java core tie-in ---");
        Runtime rtA = Runtime.getRuntime();
        Runtime rtB = Runtime.getRuntime();
        ctx.log("Runtime.getRuntime() same object? " + (rtA == rtB) + " (JVM-wide single Runtime).");
        ClassLoader cl = AppConfig.class.getClassLoader();
        ctx.log("AppConfig INSTANCE is unique per classloader: " + cl);
        ctx.log("Another classloader with its own AppConfig class would hold a different INSTANCE.");

        PatternLessonHeader.print(
                ctx,
                "Singleton",
                "GoF Creational",
                "Restrict construction so one shared instance exists; access is centralized.",
                "Two calls to INSTANCE return the same object reference.");
        ctx.log("Same instance? " + (AppConfig.INSTANCE == AppConfig.INSTANCE));
        ctx.log("App name: " + AppConfig.INSTANCE.getName());
    }

    private enum AppConfig {
        INSTANCE;

        private final String name = "demo-app";

        String getName() {
            return name;
        }
    }
}
