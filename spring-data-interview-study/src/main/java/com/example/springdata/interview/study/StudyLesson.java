package com.example.springdata.interview.study;

/** One Spring Data interview lesson as an executable unit. */
public interface StudyLesson {

    int number();

    String title();

    void run(StudyContext ctx) throws Exception;
}
