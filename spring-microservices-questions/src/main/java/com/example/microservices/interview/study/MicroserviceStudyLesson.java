package com.example.microservices.interview.study;

/** One Spring microservices interview topic as an executable lesson. */
public interface MicroserviceStudyLesson {

    int number();

    String title();

    void run(MicroservicesStudyContext ctx) throws Exception;
}
