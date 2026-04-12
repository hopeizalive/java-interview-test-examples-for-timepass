package com.example.springdata.interview.sddata;

import com.example.springdata.interview.study.StudyContext;
import org.springframework.context.ConfigurableApplicationContext;

/** One runnable unit per lesson number; collected into {@link LessonRuntime}. */
public interface LessonRunnable {

    int lessonNumber();

    void run(ConfigurableApplicationContext app, StudyContext ctx) throws Exception;
}
