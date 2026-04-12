package com.example.security.interview.lesson;

import com.example.security.interview.study.StudyLesson;

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
