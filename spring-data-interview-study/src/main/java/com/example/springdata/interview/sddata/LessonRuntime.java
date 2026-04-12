package com.example.springdata.interview.sddata;

import com.example.springdata.interview.study.StudyContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LessonRuntime {

    private final Map<Integer, LessonRunnable> byNumber;

    public LessonRuntime(List<LessonRunnable> runnables) {
        this.byNumber = runnables.stream()
                .collect(Collectors.toUnmodifiableMap(LessonRunnable::lessonNumber, Function.identity()));
    }

    public void runLesson(int lessonNumber, ConfigurableApplicationContext context, StudyContext studyContext)
            throws Exception {
        LessonRunnable r = byNumber.get(lessonNumber);
        if (r == null) {
            throw new UnsupportedOperationException("Lesson " + lessonNumber + " has no runner bean.");
        }
        r.run(context, studyContext);
    }
}
