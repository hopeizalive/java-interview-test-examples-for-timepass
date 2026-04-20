package com.example.springannotation.interview.lesson;

import com.example.springannotation.interview.study.StudyLesson;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** All Spring annotation lessons; {@link AnnotationLesson#EXPECTED_LESSON_COUNT} is the source of truth. */
public final class LessonCatalog {

    private static final List<StudyLesson> LESSONS =
            Arrays.stream(AnnotationLesson.values()).map(Function.identity()).collect(Collectors.toUnmodifiableList());

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
                    "No lesson " + n + "; valid range 1–" + AnnotationLesson.EXPECTED_LESSON_COUNT);
        }
        return lesson;
    }

    public static void assertCoverage() {
        if (LESSONS.size() != AnnotationLesson.EXPECTED_LESSON_COUNT) {
            throw new IllegalStateException(
                    "Expected " + AnnotationLesson.EXPECTED_LESSON_COUNT + " lessons, got " + LESSONS.size());
        }
        long distinct = LESSONS.stream().mapToInt(StudyLesson::number).distinct().count();
        if (distinct != AnnotationLesson.EXPECTED_LESSON_COUNT) {
            throw new IllegalStateException("Duplicate or missing lesson numbers");
        }
    }
}
