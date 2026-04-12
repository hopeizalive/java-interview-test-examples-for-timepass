package com.example.security.interview.study;

/** One Spring Security interview topic as an executable lesson. */
public interface StudyLesson {

    int number();

    String title();

    void run(SecurityStudyContext ctx) throws Exception;
}
