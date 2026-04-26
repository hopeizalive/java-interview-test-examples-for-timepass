package com.example.javadp.interview.lesson;

import com.example.javadp.interview.study.StudyLesson;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** All design-pattern lessons, ordered by {@link DesignPatternsLesson} enum declaration. */
public final class LessonCatalog {

    private static final List<StudyLesson> LESSONS =
            Arrays.stream(DesignPatternsLesson.values()).map(Function.identity()).collect(Collectors.toUnmodifiableList());

    private static final Map<Integer, StudyLesson> BY_NUMBER =
            LESSONS.stream().collect(Collectors.toUnmodifiableMap(StudyLesson::number, Function.identity()));

    private static final int MIN_LESSON_NUMBER =
            LESSONS.stream().mapToInt(StudyLesson::number).min().orElse(1);
    private static final int MAX_LESSON_NUMBER =
            LESSONS.stream().mapToInt(StudyLesson::number).max().orElse(1);

    private LessonCatalog() {}

    public static List<StudyLesson> all() {
        return LESSONS;
    }

    public static StudyLesson byNumber(int n) {
        StudyLesson lesson = BY_NUMBER.get(n);
        if (lesson == null) {
            throw new IllegalArgumentException(
                    "No lesson " + n + "; valid range " + MIN_LESSON_NUMBER + "–" + MAX_LESSON_NUMBER);
        }
        return lesson;
    }

    static {
        long distinct = LESSONS.stream().mapToInt(StudyLesson::number).distinct().count();
        if (distinct != LESSONS.size()) {
            throw new IllegalStateException("Duplicate lesson numbers in DesignPatternsLesson");
        }
    }
}
