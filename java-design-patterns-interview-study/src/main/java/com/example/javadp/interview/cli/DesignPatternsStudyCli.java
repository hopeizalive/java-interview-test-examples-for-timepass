package com.example.javadp.interview.cli;

import com.example.interview.studycli.runall.RunAllTask;
import com.example.interview.studycli.runall.StudyRunAllExecutor;
import com.example.interview.studycli.runall.StudyRunAllResult;
import com.example.javadp.interview.lesson.LessonCatalog;
import com.example.javadp.interview.study.StudyContext;
import com.example.javadp.interview.study.StudyLesson;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Java design patterns interview lessons: all GoF patterns (lessons 1–23) plus seven modern idioms (24–30).
 *
 * <pre>
 *   mvn -pl java-design-patterns-interview-study exec:java -Dexec.args="list"
 *   mvn -pl java-design-patterns-interview-study exec:java -Dexec.args="run 1"
 *   mvn -pl java-design-patterns-interview-study exec:java -Dexec.args="run-all"
 * </pre>
 */
@Command(
        name = "java-design-patterns-study",
        mixinStandardHelpOptions = true,
        version = "1.0",
        subcommands = {
                DesignPatternsStudyCli.ListLessons.class,
                DesignPatternsStudyCli.RunLesson.class,
                DesignPatternsStudyCli.RunAll.class
        },
        description = "Run Java design pattern lessons (GoF + modern idioms). Use `list` to see lesson numbers.")
public final class DesignPatternsStudyCli implements Callable<Integer> {

    private static final String RUN_ALL_FAILURE_BANNER = "Java design patterns interview study";

    public static void main(String[] args) {
        int exit = new CommandLine(new DesignPatternsStudyCli()).execute(args);
        System.exit(exit);
    }

    @Override
    public Integer call() {
        CommandLine.usage(this, System.out);
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java-design-patterns-study list");
        System.out.println("  java-design-patterns-study run 1");
        System.out.println("  java-design-patterns-study run-all");
        return 0;
    }

    @Command(name = "list", description = "List all lessons")
    static class ListLessons implements Callable<Integer> {
        @Override
        public Integer call() {
            LessonCatalog.all().forEach(l -> System.out.printf("%2d  %s%n", l.number(), l.title()));
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

    @Command(
            name = "run-all",
            description = "Run every lesson in order; continue on failure; exit 1 if any failed.")
    static class RunAll implements Callable<Integer> {

        @Option(
                names = {"--errors-log", "-e"},
                defaultValue = "java-design-patterns-study-run-all-errors.log",
                description = "Stack trace log path (created on first failure in this run).")
        private Path errorsLog;

        @Override
        public Integer call() throws Exception {
            StudyContext ctx = new StudyContext();
            List<RunAllTask> tasks = new ArrayList<>();
            for (StudyLesson lesson : LessonCatalog.all()) {
                StudyLesson l = lesson;
                tasks.add(new RunAllTask(l.number(), l.title(), () -> runLesson(ctx, l)));
            }
            StudyRunAllResult result = StudyRunAllExecutor.execute(RUN_ALL_FAILURE_BANNER, errorsLog, tasks);
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
