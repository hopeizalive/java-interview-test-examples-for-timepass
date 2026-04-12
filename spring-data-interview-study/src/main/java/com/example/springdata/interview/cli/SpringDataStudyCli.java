package com.example.springdata.interview.cli;

import com.example.springdata.interview.lesson.LessonCatalog;
import com.example.springdata.interview.study.StudyContext;
import com.example.springdata.interview.study.StudyLesson;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

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

        @Option(
                names = {"--errors-log", "-e"},
                defaultValue = "spring-data-study-run-all-errors.log",
                description = "Single file for full stack traces of all failed lessons (truncated when the first failure is recorded).")
        private Path errorsLog;

        @Override
        public Integer call() throws Exception {
            StudyContext ctx = new StudyContext();
            int passed = 0;
            List<Integer> failedNumbers = new ArrayList<>();
            List<String> failedMessages = new ArrayList<>();
            boolean[] logStarted = {false};
            for (StudyLesson lesson : LessonCatalog.all()) {
                try {
                    runLesson(ctx, lesson);
                    passed++;
                } catch (Throwable t) {
                    failedNumbers.add(lesson.number());
                    failedMessages.add(failureMessage(t));
                    System.err.println("[FAILED] Lesson " + lesson.number() + ": " + failureMessage(t));
                    appendFailureToLog(errorsLog, logStarted, lesson, t);
                }
            }
            int failed = failedNumbers.size();
            System.out.println("========== run-all complete: " + passed + " passed, " + failed + " failed ==========");
            if (failed > 0) {
                System.out.println("Failed lessons:");
                for (int i = 0; i < failedNumbers.size(); i++) {
                    System.out.println("  #" + failedNumbers.get(i) + " — " + failedMessages.get(i));
                }
                System.out.println();
                System.out.println("Full stack traces (all failures in one file):");
                System.out.println("  " + errorsLog.toAbsolutePath().normalize());
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

    /**
     * One file for the whole {@code run-all}: first failure truncates and writes a header; later failures append
     * a clearly delimited section with the full stack trace each time.
     */
    private static void appendFailureToLog(Path path, boolean[] logStarted, StudyLesson lesson, Throwable t)
            throws Exception {
        var section = new StringBuilder(512);
        if (!logStarted[0]) {
            section.append("Spring Data interview study — run-all failure log\n");
            section.append("Started: ").append(Instant.now()).append("\n");
            section.append("(One file for every failure in this run; not one file per lesson.)\n\n");
            logStarted[0] = true;
            Files.writeString(path, section.toString(), StandardCharsets.UTF_8, CREATE, TRUNCATE_EXISTING);
            section.setLength(0);
        }
        section.append("================================================================================\n");
        section.append("Lesson ").append(lesson.number()).append(" — ").append(lesson.title()).append('\n');
        section.append("Recorded: ").append(Instant.now()).append("\n");
        section.append("--------------------------------------------------------------------------------\n");
        var sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        section.append(sw);
        if (!section.isEmpty() && section.charAt(section.length() - 1) != '\n') {
            section.append('\n');
        }
        section.append('\n');
        Files.writeString(path, section.toString(), StandardCharsets.UTF_8, CREATE, APPEND);
    }
}
