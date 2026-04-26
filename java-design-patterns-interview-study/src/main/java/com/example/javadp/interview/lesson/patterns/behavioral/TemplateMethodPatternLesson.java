package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Template Method: define algorithm skeleton in a base class; subclasses override specific steps.
 */
public final class TemplateMethodPatternLesson {

    private TemplateMethodPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "JUnit 5 test lifecycle",
                "@BeforeEach / @AfterEach hooks let frameworks run a fixed sequence around each @Test method.",
                "Template method controls flow; tests supply concrete steps.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "javax.servlet.HttpServlet",
                "service() dispatches to doGet/doPost - servlet container owns the skeleton; apps override hooks.",
                "Classic JEE template method.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Spring AbstractRoutingDataSource",
                "Framework resolves datasource key then delegates to subclass determineCurrentLookupKey().",
                "Invariant routing in base, variant hook in subclass.");

        ctx.log("--- Java core tie-in ---");
        java.util.Timer timer = new java.util.Timer(true);
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        timer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        latch.countDown();
                    }
                },
                1);
        try {
            latch.await();
            ctx.log("java.util.Timer schedules java.util.TimerTask; subclass overrides run() hook.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ctx.log("Timer tie-in interrupted");
        } finally {
            timer.cancel();
        }

        PatternLessonHeader.print(
                ctx,
                "Template Method",
                "GoF Behavioral",
                "Invariant steps live in a final method; hooks are overridden; the framework controls the flow.",
                "DataImporter.run() always open, parse, persist; CSV vs JSON override parse only.");
        new CsvImporter().run(ctx);
        new JsonImporter().run(ctx);
    }

    private abstract static class DataImporter {
        final void run(StudyContext ctx) {
            ctx.log("open resource");
            parse(ctx);
            ctx.log("persist rows");
        }

        protected abstract void parse(StudyContext ctx);
    }

    private static final class CsvImporter extends DataImporter {
        @Override
        protected void parse(StudyContext ctx) {
            ctx.log("parse CSV rows");
        }
    }

    private static final class JsonImporter extends DataImporter {
        @Override
        protected void parse(StudyContext ctx) {
            ctx.log("parse JSON documents");
        }
    }
}
