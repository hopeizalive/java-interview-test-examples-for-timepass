package com.example.concurrency.interview.lesson;

import com.example.concurrency.interview.study.StudyLesson;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** All concurrency lessons; {@link ConcurrencyLesson#EXPECTED_LESSON_COUNT} is the single source of truth for size. */
public final class LessonCatalog {

    private static final List<StudyLesson> LESSONS =
            Arrays.stream(ConcurrencyLesson.values()).map(Function.identity()).collect(Collectors.toUnmodifiableList());

    private static final Map<Integer, StudyLesson> BY_NUMBER =
            LESSONS.stream().collect(Collectors.toUnmodifiableMap(StudyLesson::number, Function.identity()));

    private LessonCatalog() {}

    public static List<StudyLesson> all() {
        return LESSONS;
    }

    public static StudyLesson byNumber(int n) {
        StudyLesson lesson = BY_NUMBER.get(n);
        if (lesson == null) {
            throw new IllegalArgumentException(
                    "No lesson " + n + "; valid range 1–" + ConcurrencyLesson.EXPECTED_LESSON_COUNT);
        }
        return lesson;
    }

    public static void assertCoverage() {
        if (LESSONS.size() != ConcurrencyLesson.EXPECTED_LESSON_COUNT) {
            throw new IllegalStateException(
                    "Expected " + ConcurrencyLesson.EXPECTED_LESSON_COUNT + " lessons, got " + LESSONS.size());
        }
        long distinct = LESSONS.stream().mapToInt(StudyLesson::number).distinct().count();
        if (distinct != ConcurrencyLesson.EXPECTED_LESSON_COUNT) {
            throw new IllegalStateException("Duplicate or missing lesson numbers");
        }
    }
}
