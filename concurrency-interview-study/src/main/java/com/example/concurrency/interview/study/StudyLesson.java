package com.example.concurrency.interview.study;

/** One executable concurrency interview lesson. */
public interface StudyLesson {

    int number();

    String title();

    void run(StudyContext ctx) throws Exception;
}
