package com.example.javads.interview.study;

/** One executable Java collections / DSA comparison lesson. */
public interface StudyLesson {

    int number();

    String title();

    void run(StudyContext ctx) throws Exception;
}
