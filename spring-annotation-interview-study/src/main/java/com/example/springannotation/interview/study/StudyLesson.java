package com.example.springannotation.interview.study;

/** One executable Spring annotation interview lesson. */
public interface StudyLesson {

    int number();

    String title();

    void run(StudyContext ctx) throws Exception;
}
