package com.example.javadp.interview.study;

/** One executable design-pattern lesson (theory + runnable demo). */
public interface StudyLesson {

    int number();

    String title();

    void run(StudyContext ctx) throws Exception;
}
