package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyLesson;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** All 50 Flexiple-style lessons, ordered 1–50. */
public final class LessonCatalog {

    private static final List<StudyLesson> LESSONS = List.of(
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
            new Lesson31(),
            new Lesson32(),
            new Lesson33(),
            new Lesson34(),
            new Lesson35(),
            new Lesson36(),
            new Lesson37(),
            new Lesson38(),
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

    private static final Map<Integer, StudyLesson> BY_NUMBER =
            LESSONS.stream().collect(Collectors.toUnmodifiableMap(StudyLesson::number, Function.identity()));

    private LessonCatalog() {
    }

    public static List<StudyLesson> all() {
        return LESSONS;
    }

    public static StudyLesson byNumber(int n) {
        StudyLesson lesson = BY_NUMBER.get(n);
        if (lesson == null) {
            throw new IllegalArgumentException("No lesson " + n + "; valid range 1–" + LESSONS.size());
        }
        return lesson;
    }

    public static void assertCoverage() {
        if (LESSONS.size() != 50) {
            throw new IllegalStateException("Expected 50 lessons, got " + LESSONS.size());
        }
        long distinct = LESSONS.stream().mapToInt(StudyLesson::number).distinct().count();
        if (distinct != 50) {
            throw new IllegalStateException("Duplicate or missing lesson numbers");
        }
    }
}
