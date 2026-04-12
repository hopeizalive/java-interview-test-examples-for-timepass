package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroserviceStudyLesson;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Spring microservices lessons (1–46); narrative-only entries removed so each lesson runs executable code. */
public final class MicroservicesLessonCatalog {

    public static final int LESSON_COUNT = 46;

    private static final List<MicroserviceStudyLesson> LESSONS = List.of(
            new Lesson01(),
            new Lesson02(),
            new Lesson03(),
            new Lesson04(),
            new Lesson05(),
            new Lesson06(),
            new Lesson07(),
            new Lesson08(),
            new Lesson09(),
            new Lesson10(),
            new Lesson11(),
            new Lesson12(),
            new Lesson13(),
            new Lesson14(),
            new Lesson15(),
            new Lesson16(),
            new Lesson17(),
            new Lesson18(),
            new Lesson19(),
            new Lesson20(),
            new Lesson21(),
            new Lesson22(),
            new Lesson23(),
            new Lesson24(),
            new Lesson25(),
            new Lesson26(),
            new Lesson27(),
            new Lesson28(),
            new Lesson29(),
            new Lesson30(),
            new Lesson32(),
            new Lesson33(),
            new Lesson34(),
            new Lesson37(),
            new Lesson39(),
            new Lesson40(),
            new Lesson41(),
            new Lesson42(),
            new Lesson43(),
            new Lesson44(),
            new Lesson45(),
            new Lesson46(),
            new Lesson47(),
            new Lesson48(),
            new Lesson49(),
            new Lesson50()
    );

    private static final Map<Integer, MicroserviceStudyLesson> BY_NUMBER =
            LESSONS.stream().collect(Collectors.toUnmodifiableMap(MicroserviceStudyLesson::number, Function.identity()));

    private MicroservicesLessonCatalog() {
    }

    public static List<MicroserviceStudyLesson> all() {
        return LESSONS;
    }

    public static MicroserviceStudyLesson byNumber(int n) {
        MicroserviceStudyLesson lesson = BY_NUMBER.get(n);
        if (lesson == null) {
            throw new IllegalArgumentException("No lesson " + n + "; valid range 1–" + LESSON_COUNT);
        }
        return lesson;
    }

    public static void assertCoverage() {
        if (LESSONS.size() != LESSON_COUNT) {
            throw new IllegalStateException("Expected " + LESSON_COUNT + " lessons, got " + LESSONS.size());
        }
        long distinct = LESSONS.stream().mapToInt(MicroserviceStudyLesson::number).distinct().count();
        if (distinct != LESSON_COUNT) {
            throw new IllegalStateException("Duplicate or missing lesson numbers");
        }
    }
}
