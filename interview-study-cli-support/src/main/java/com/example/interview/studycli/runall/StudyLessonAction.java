package com.example.interview.studycli.runall;

/** Lesson body invoked by {@link StudyRunAllExecutor}; checked exceptions propagate to the executor. */
@FunctionalInterface
public interface StudyLessonAction {

    void run() throws Exception;
}
