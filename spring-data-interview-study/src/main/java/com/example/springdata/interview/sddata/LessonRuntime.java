package com.example.springdata.interview.sddata;

import com.example.springdata.interview.study.StudyContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Runtime component that executes Spring Data lessons based on lesson number.
 */
@Component
public class LessonRuntime {

    public void runLesson(int lessonNumber, ConfigurableApplicationContext context, StudyContext studyContext) {
        switch (lessonNumber) {
            case 1 -> runLesson01(context, studyContext);
            case 2 -> runLesson02(context, studyContext);
            // Add cases for other lessons as needed
            default -> studyContext.log("Lesson " + lessonNumber + " is not yet implemented.");
        }
    }

    private void runLesson01(ConfigurableApplicationContext context, StudyContext studyContext) {
        studyContext.log("Running Lesson 1: Spring Data JpaRepository vs EntityManager — less ceremony for CRUD.");
        // Lesson 1 implementation would go here
        studyContext.log("Lesson 1 completed.");
    }

    private void runLesson02(ConfigurableApplicationContext context, StudyContext studyContext) {
        studyContext.log("Running Lesson 2: JpaRepository layers CrudRepository + PagingAndSortingRepository + JPA helpers.");
        // Lesson 2 implementation would go here
        studyContext.log("Lesson 2 completed.");
    }
}