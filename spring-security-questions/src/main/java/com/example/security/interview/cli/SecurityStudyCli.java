package com.example.security.interview.cli;

import com.example.interview.studycli.runall.RunAllTask;
import com.example.interview.studycli.runall.StudyRunAllExecutor;
import com.example.interview.studycli.runall.StudyRunAllResult;
import com.example.security.interview.lesson.LessonCatalog;
import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.study.StudyLesson;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Study CLI for Spring Security lessons 1–50.
 *
 * <pre>
 *   Windows (repo root): security-study.cmd list
 *   Windows (after package): security-study-jar.cmd run 11
 *   mvnw -pl spring-security-questions exec:java -Dexec.args="list"
 *   java -jar spring-security-questions/target/spring-security-questions-1.0-SNAPSHOT.jar run-all
 * </pre>
 */
@Command(
        name = "security-study",
        mixinStandardHelpOptions = true,
        version = "1.0",
        subcommands = {
                SecurityStudyCli.ListLessons.class,
                SecurityStudyCli.RunLesson.class,
                SecurityStudyCli.RunAll.class
        },
        description = "Run Spring Security interview lessons (1–50)."
)
public class SecurityStudyCli implements Callable<Integer> {

    private static final String RUN_ALL_FAILURE_BANNER = "Spring Security interview study";

    public static void main(String[] args) {
        LessonCatalog.assertCoverage();
        int exit = new CommandLine(new SecurityStudyCli()).execute(args);
        System.exit(exit);
    }

    @Override
    public Integer call() {
        CommandLine.usage(this, System.out);
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  security-study list");
        System.out.println("  security-study run 11");
        System.out.println("  security-study run-all");
        return 0;
    }

    @Command(name = "list", description = "List lessons 1–50")
    static class ListLessons implements Callable<Integer> {
        @Override
        public Integer call() {
            LessonCatalog.all().forEach(l ->
                    System.out.printf("%2d  %s%n", l.number(), l.title()));
            return 0;
        }
    }

    @Command(name = "run", description = "Run a single lesson (1–50)")
    static class RunLesson implements Callable<Integer> {
        @Parameters(index = "0", description = "Lesson number")
        int lesson;

        @Override
        public Integer call() throws Exception {
            SecurityStudyContext ctx = new SecurityStudyContext();
            runLesson(ctx, LessonCatalog.byNumber(lesson));
            return 0;
        }
    }

    @Command(name = "run-all", description = "Run every lesson in order; continue on failure and print a summary (exit 1 if any failed)")
    static class RunAll implements Callable<Integer> {

        @Option(
                names = {"--errors-log", "-e"},
                defaultValue = "security-study-run-all-errors.log",
                description = "Stack trace log path (created on first failure in this run).")
        private Path errorsLog;

        @Override
        public Integer call() throws Exception {
            SecurityStudyContext ctx = new SecurityStudyContext();
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

    private static void runLesson(SecurityStudyContext ctx, StudyLesson lesson) throws Exception {
        System.out.println("--- Lesson " + lesson.number() + " ---");
        System.out.println(lesson.title());
        lesson.run(ctx);
        System.out.println();
    }
}
