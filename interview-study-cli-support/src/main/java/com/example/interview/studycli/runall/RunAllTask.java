package com.example.interview.studycli.runall;

/**
 * One lesson attempt for {@link StudyRunAllExecutor}: number and title label the lesson in logs;
 * {@link #action()} runs the lesson (typically closes over a shared context).
 */
public record RunAllTask(int number, String title, StudyLessonAction action) {}
