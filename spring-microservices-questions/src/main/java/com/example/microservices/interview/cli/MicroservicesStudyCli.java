package com.example.microservices.interview.cli;

import com.example.microservices.interview.lesson.MicroservicesLessonCatalog;
import com.example.microservices.interview.study.MicroserviceStudyLesson;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Study CLI for Spring microservices lessons 1–50.
 *
 * <pre>
 *   Windows (repo root): microservices-study.cmd list
 *   mvnw -pl spring-microservices-questions exec:java -Dexec.args="run 11"
 * </pre>
 */
@Command(
        name = "microservices-study",
        mixinStandardHelpOptions = true,
        version = "1.0",
        subcommands = {
                MicroservicesStudyCli.ListLessons.class,
                MicroservicesStudyCli.RunLesson.class,
                MicroservicesStudyCli.RunAll.class
        },
        description = "Run Spring microservices interview lessons (1–50)."
)
public class MicroservicesStudyCli implements Callable<Integer> {

    public static void main(String[] args) {
        MicroservicesLessonCatalog.assertCoverage();
        int exit = new CommandLine(new MicroservicesStudyCli()).execute(args);
        System.exit(exit);
    }

    @Override
    public Integer call() {
        CommandLine.usage(this, System.out);
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  microservices-study list");
        System.out.println("  microservices-study run 11");
        System.out.println("  microservices-study run-all");
        return 0;
    }

    @Command(name = "list", description = "List lessons 1–50")
    static class ListLessons implements Callable<Integer> {
        @Override
        public Integer call() {
            MicroservicesLessonCatalog.all().forEach(l ->
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
            MicroservicesStudyContext ctx = new MicroservicesStudyContext();
            runLesson(ctx, MicroservicesLessonCatalog.byNumber(lesson));
            return 0;
        }
    }

    @Command(name = "run-all", description = "Run every lesson in order; continue on failure and print a summary (exit 1 if any failed)")
    static class RunAll implements Callable<Integer> {
        @Override
        public Integer call() {
            MicroservicesStudyContext ctx = new MicroservicesStudyContext();
            int passed = 0;
            List<Integer> failedNumbers = new ArrayList<>();
            List<String> failedMessages = new ArrayList<>();
            for (MicroserviceStudyLesson lesson : MicroservicesLessonCatalog.all()) {
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

    private static void runLesson(MicroservicesStudyContext ctx, MicroserviceStudyLesson lesson) throws Exception {
        System.out.println("--- Lesson " + lesson.number() + " ---");
        System.out.println(lesson.title());
        lesson.run(ctx);
        System.out.println();
    }

    /** Short message for summaries (prefers root cause). */
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
