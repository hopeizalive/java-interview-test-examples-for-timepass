package com.example.springdata.interview.lesson;

import com.example.springdata.interview.study.StudyLesson;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** All 50 Spring Data lessons; each run uses {@link com.example.springdata.interview.SpringDataStudyApplication} once per lesson. */
public final class LessonCatalog {

    private static final List<StudyLesson> LESSONS =
            Arrays.stream(SpringDataLesson.values()).map(Function.identity()).collect(Collectors.toUnmodifiableList());

    private static final Map<Integer, StudyLesson> BY_NUMBER =
            LESSONS.stream().collect(Collectors.toUnmodifiableMap(StudyLesson::number, Function.identity()));

    private LessonCatalog() {}

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
