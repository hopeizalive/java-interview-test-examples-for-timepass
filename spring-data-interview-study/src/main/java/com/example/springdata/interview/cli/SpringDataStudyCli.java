package com.example.springdata.interview.cli;

import com.example.springdata.interview.lesson.LessonCatalog;
import com.example.springdata.interview.study.StudyContext;
import com.example.springdata.interview.study.StudyLesson;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

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
        description = "Run Spring Data interview lessons (1–50)."
)
public class SpringDataStudyCli implements Callable<Integer> {

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
            StudyContext ctx = new StudyContext();
            runLesson(ctx, LessonCatalog.byNumber(lesson));
            return 0;
        }
    }

    @Command(name = "run-all", description = "Run every lesson in order; continue on failure and print a summary (exit 1 if any failed)")
    static class RunAll implements Callable<Integer> {
        @Override
        public Integer call() {
            StudyContext ctx = new StudyContext();
            int passed = 0;
            List<Integer> failedNumbers = new ArrayList<>();
            List<String> failedMessages = new ArrayList<>();
            for (StudyLesson lesson : LessonCatalog.all()) {
                try {
                    runLesson(ctx, lesson);
                    passed++;
                } catch (Throwable t) {
                    failedNumbers.add(lesson.number());
                    failedMessages.add(failureMessage(t));
                    System.err.println("[FAILED] Lesson " + lesson.number() + ": " + failureMessage(t));
                }
            }
            int failed = failedNumbers.size();
            System.out.println("========== run-all complete: " + passed + " passed, " + failed + " failed ==========");
            if (failed > 0) {
                System.out.println("Failed lessons:");
                for (int i = 0; i < failedNumbers.size(); i++) {
                    System.out.println("  #" + failedNumbers.get(i) + " — " + failedMessages.get(i));
                }
                return 1;
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

    private static String failureMessage(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null && c.getCause() != c) {
            c = c.getCause();
        }
        String m = c.getMessage();
        if (m == null || m.isBlank()) {
            return c.getClass().getSimpleName();
        }
        return c.getClass().getSimpleName() + ": " + m;
    }
}
