package com.example.springdata.interview.cli;

import com.example.interview.studycli.runall.RunAllTask;
import com.example.interview.studycli.runall.StudyRunAllExecutor;
import com.example.interview.studycli.runall.StudyRunAllResult;
import com.example.springdata.interview.lesson.LessonCatalog;
import com.example.springdata.interview.study.StudyContext;
import com.example.springdata.interview.study.StudyLesson;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Spring Data interview lessons (1–50), H2-backed Boot contexts per lesson.
 *
 * <pre>
 *   mvn -pl spring-data-interview-study exec:java -Dexec.args="list"
 *   mvn -pl spring-data-interview-study exec:java -Dexec.args="run 1"
 *   mvn -pl spring-data-interview-study exec:java -Dexec.args="run-all"
 *   mvn -pl spring-data-interview-study exec:java -Dexec.args="run-all --errors-log /tmp/sd-errors.log"
 * </pre>
 */
@Command(
        name = "spring-data-study",
        mixinStandardHelpOptions = true,
        version = "1.0",
        subcommands = {
                SpringDataStudyCli.ListLessons.class,
                SpringDataStudyCli.RunLesson.class,
                SpringDataStudyCli.RunAll.class
        },
        description = "Run Spring Data interview lessons (1–34); each lesson runs real persistence code."
)
public class SpringDataStudyCli implements Callable<Integer> {

    private static final String RUN_ALL_FAILURE_BANNER = "Spring Data interview study";

    public static void main(String[] args) {
        LessonCatalog.assertCoverage();
        int exit = new CommandLine(new SpringDataStudyCli()).execute(args);
        System.exit(exit);
    }

    @Override
    public Integer call() {
        CommandLine.usage(this, System.out);
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  spring-data-study list");
        System.out.println("  spring-data-study run 1");
        System.out.println("  spring-data-study run-all");
        return 0;
    }

    @Command(name = "list", description = "List lessons 1–34")
    static class ListLessons implements Callable<Integer> {
        @Override
        public Integer call() {
            LessonCatalog.all().forEach(l ->
                    System.out.printf("%2d  %s%n", l.number(), l.title()));
            return 0;
        }
    }

    @Command(name = "run", description = "Run a single lesson (1–34)")
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

    @Command(name = "run-all", description = "Run every lesson in order; continue on failure and print a summary (exit 1 if any failed)")
    static class RunAll implements Callable<Integer> {

        @Option(
                names = {"--errors-log", "-e"},
                defaultValue = "spring-data-study-run-all-errors.log",
                description = "Single file for full stack traces of all failed lessons (created on first failure in this run).")
        private Path errorsLog;

        @Override
        public Integer call() throws Exception {
            StudyContext ctx = new StudyContext();
            List<RunAllTask> tasks = new ArrayList<>();
            for (StudyLesson lesson : LessonCatalog.all()) {
                StudyLesson l = lesson;
                tasks.add(new RunAllTask(l.number(), l.title(), () -> runLesson(ctx, l)));
            }
            StudyRunAllResult result =
                    StudyRunAllExecutor.execute(RUN_ALL_FAILURE_BANNER, errorsLog, tasks);
            StudyRunAllExecutor.printStandardSummary(result, System.out);
            StudyRunAllExecutor.printStackTraceLogPointer(result, System.out);
            return result.exitCode();
        }
    }

    private static void runLesson(StudyContext ctx, StudyLesson lesson) throws Exception {
        System.out.println("--- Lesson " + lesson.number() + " ---");
        System.out.println(lesson.title());
        lesson.run(ctx);
        System.out.println();
    }
}
