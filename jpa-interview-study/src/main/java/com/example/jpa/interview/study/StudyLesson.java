package com.example.jpa.interview.study;

/** One Flexiple JPA interview topic, runnable for self-study. */
public interface StudyLesson {

    /** 1–50 matching {@link com.example.jpa.interview.cli.JpaStudyCli} ordering. */
    int number();

    /** Short title from the interview guide. */
    String title();

    /** Run the demo; may open additional persistence units or factories. */
    void run(StudyContext ctx) throws Exception;
}
