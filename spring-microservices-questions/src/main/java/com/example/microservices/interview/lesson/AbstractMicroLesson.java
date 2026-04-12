package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroserviceStudyLesson;

abstract class AbstractMicroLesson implements MicroserviceStudyLesson {

    private final int number;
    private final String title;

    protected AbstractMicroLesson(int number, String title) {
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
