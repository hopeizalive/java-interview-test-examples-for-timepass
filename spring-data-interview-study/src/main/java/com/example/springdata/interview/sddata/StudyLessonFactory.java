package com.example.springdata.interview.sddata;

import com.example.springdata.interview.study.StudyContext;
import org.springframework.context.ConfigurableApplicationContext;

public final class StudyLessonFactory {

    private StudyLessonFactory() {}

    public static LessonRunnable lesson(int number, Body body) {
        return new LessonRunnable() {
            @Override
            public int lessonNumber() {
                return number;
            }

            @Override
            public void run(ConfigurableApplicationContext app, StudyContext ctx) throws Exception {
                body.execute(app, ctx);
            }
        };
    }

    @FunctionalInterface
    public interface Body {
        void execute(ConfigurableApplicationContext app, StudyContext ctx) throws Exception;
    }
}
