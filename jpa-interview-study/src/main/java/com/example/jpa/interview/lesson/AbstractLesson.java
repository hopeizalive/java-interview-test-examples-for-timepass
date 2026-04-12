package com.example.jpa.interview.lesson;

import com.example.jpa.interview.study.StudyLesson;

/** Base class so each numbered lesson stays a one-line constructor in subclasses. */
abstract class AbstractLesson implements StudyLesson {

    private final int number;
    private final String title;

    protected AbstractLesson(int number, String title) {
        this.number = number;
        this.title = title;
    }

    @Override
    public final int number() {
        return number;
    }

    @Override
    public final String title() {
        return title;
    }
}
