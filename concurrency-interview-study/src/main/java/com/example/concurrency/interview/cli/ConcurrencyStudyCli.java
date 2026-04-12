package com.example.concurrency.interview.cli;

import com.example.concurrency.interview.lesson.ConcurrencyLesson;
import com.example.concurrency.interview.lesson.LessonCatalog;
import com.example.concurrency.interview.study.StudyContext;
import com.example.concurrency.interview.study.StudyLesson;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

/**
 * Concurrency interview lessons (1–60), mostly plain JDK + optional Spring / Embedded Kafka per lesson.
 *
 * <pre>
 *   mvn -pl concurrency-interview-study exec:java -Dexec.args="list"
 *   mvn -pl concurrency-interview-study exec:java -Dexec.args="run 27"
 *   mvn -pl concurrency-interview-study exec:java -Dexec.args="run-all"
 * </pre>
 */
@Command(
        name = "concurrency-study",
        mixinStandardHelpOptions = true,
        version = "1.0",
        subcommands = {
                ConcurrencyStudyCli.ListLessons.class,
                ConcurrencyStudyCli.RunLesson.class,
                ConcurrencyStudyCli.RunAll.class
        },
        description = "Run concurrency interview lessons (1–" + ConcurrencyLesson.EXPECTED_LESSON_COUNT + ").")
public class ConcurrencyStudyCli implements Callable<Integer> {

    public static void main(String[] args) {
        LessonCatalog.assertCoverage();
        int exit = new CommandLine(new ConcurrencyStudyCli()).execute(args);
        System.exit(exit);
    }

    @Override
    public Integer call() {
        CommandLine.usage(this, System.out);
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  concurrency-study list");
        System.out.println("  concurrency-study run 27");
        System.out.println("  concurrency-study run-all");
        return 0;
    }

    @Command(name = "list", description = "List all lessons")
    static class ListLessons implements Callable<Integer> {
        @Override
        public Integer call() {
            LessonCatalog.all().forEach(l ->
                    System.out.printf("%2d  %s%n", l.number(), l.title()));
            return 0;
        }
    }

    @Command(name = "run", description = "Run a single lesson")
    static class RunLesson implements Callable<Integer> {
        @Parameters(index = "0", description = "Lesson number")
        int lesson;

        @Override
        public Integer call() throws Exception {
            StudyContext ctx = new StudyContext();
            runLesson(ctx, LessonCatalog.byNumber(lesson));
            return 0;
        }
    }

    @Command(name = "run-all", description = "Run every lesson in order")
    static class RunAll implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            StudyContext ctx = new StudyContext();
            for (StudyLesson lesson : LessonCatalog.all()) {
                runLesson(ctx, lesson);
            }
            return 0;
        }
    }

    private static void runLesson(StudyContext ctx, StudyLesson lesson) throws Exception {
        System.out.println("--- Lesson " + lesson.number() + " ---");
        System.out.println(lesson.title());
        lesson.run(ctx);
        System.out.println();
    }
}
