package com.example.springannotation.interview.cli;

import com.example.interview.studycli.runall.RunAllTask;
import com.example.interview.studycli.runall.StudyRunAllExecutor;
import com.example.interview.studycli.runall.StudyRunAllResult;
import com.example.springannotation.interview.lesson.AnnotationLesson;
import com.example.springannotation.interview.lesson.LessonCatalog;
import com.example.springannotation.interview.study.StudyContext;
import com.example.springannotation.interview.study.StudyLesson;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/** CLI to list and execute Spring annotation comparison lessons. */
@Command(
        name = "spring-annotation-study",
        mixinStandardHelpOptions = true,
        version = "1.0",
        subcommands = {
                SpringAnnotationStudyCli.ListLessons.class,
                SpringAnnotationStudyCli.RunLesson.class,
                SpringAnnotationStudyCli.RunAll.class
        },
        description = "Run Spring annotation interview lessons (1–" + AnnotationLesson.EXPECTED_LESSON_COUNT + ").")
public class SpringAnnotationStudyCli implements Callable<Integer> {

    private static final String RUN_ALL_FAILURE_BANNER = "Spring annotation interview study";

    public static void main(String[] args) {
        LessonCatalog.assertCoverage();
        int exit = new CommandLine(new SpringAnnotationStudyCli()).execute(args);
        System.exit(exit);
    }

    @Override
    public Integer call() {
        CommandLine.usage(this, System.out);
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  spring-annotation-study list");
        System.out.println("  spring-annotation-study run 9");
        System.out.println("  spring-annotation-study run-all");
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
                defaultValue = "spring-annotation-study-run-all-errors.log",
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
