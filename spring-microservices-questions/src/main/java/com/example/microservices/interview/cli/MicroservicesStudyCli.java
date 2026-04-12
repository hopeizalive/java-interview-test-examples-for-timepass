package com.example.microservices.interview.cli;

import com.example.interview.studycli.runall.RunAllStackTraceLogWriter;
import com.example.interview.studycli.runall.RunAllThrowableFormatter;
import com.example.microservices.interview.lesson.MicroservicesLessonCatalog;
import com.example.microservices.interview.study.MicroserviceStudyLesson;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Study CLI for Spring microservices lessons 1–50.
 *
 * <pre>
 *   Windows (repo root): microservices-study.cmd list
 *   mvnw -pl spring-microservices-questions exec:java -Dexec.args="run 11"
 *   microservices-study run-all   (always writes a report; path printed at the end)
 * </pre>
 * <p>{@code run} is console-only. Report files apply only to {@code run-all}.</p>
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
        System.out.println("  microservices-study run-all   (report file path shown when finished)");
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

    @Command(name = "run", description = "Run a single lesson (1–50); console output only (no report file)")
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

    @Command(
            name = "run-all",
            description = "Run every lesson in order; continue on failure; PASS/FAIL report under target/microservices-reports/; optional stack trace log (exit 1 if any failed)")
    static class RunAll implements Callable<Integer> {

        @Option(
                names = {"--errors-log", "-e"},
                defaultValue = "microservices-study-run-all-errors.log",
                description = "Full stack traces for failures (same contract as other study modules; created on first failure).")
        private Path errorsLog;

        @Override
        public Integer call() {
            Path reportPath;
            try {
                reportPath = defaultRunAllReportPath();
            } catch (Exception e) {
                System.err.println("Could not build report path: " + e.getMessage());
                return 2;
            }

            MicroservicesStudyContext ctx = new MicroservicesStudyContext();
            int passed = 0;
            List<Integer> failedNumbers = new ArrayList<>();
            List<String> failedMessages = new ArrayList<>();
            RunAllStackTraceLogWriter traceLog =
                    new RunAllStackTraceLogWriter(errorsLog, "Spring microservices interview study");

            try (RunAllReportWriter report = new RunAllReportWriter(reportPath)) {
                for (MicroserviceStudyLesson lesson : MicroservicesLessonCatalog.all()) {
                    try {
                        runLesson(ctx, lesson);
                        passed++;
                        report.lessonResult(lesson.number(), lesson.title(), true, null);
                    } catch (Throwable t) {
                        String msg = RunAllThrowableFormatter.rootCauseMessage(t);
                        failedNumbers.add(lesson.number());
                        failedMessages.add(msg);
                        System.err.println("[FAILED] Lesson " + lesson.number() + ": " + msg);
                        report.lessonResult(lesson.number(), lesson.title(), false, msg);
                        try {
                            traceLog.appendFailureSection(lesson.number(), lesson.title(), t);
                        } catch (Exception io) {
                            System.err.println("Could not append stack trace log: " + io.getMessage());
                        }
                    }
                }
                int failed = failedNumbers.size();
                report.summary(passed, failed, failedNumbers, failedMessages);
                Path written = report.absolutePath();
                System.out.println("========== run-all complete: " + passed + " passed, " + failed + " failed ==========");
                if (failed > 0) {
                    System.out.println("Failed lessons:");
                    for (int i = 0; i < failedNumbers.size(); i++) {
                        System.out.println("  #" + failedNumbers.get(i) + " — " + failedMessages.get(i));
                    }
                    System.out.println();
                    System.out.println("Full stack traces (all failures in this run):");
                    System.out.println("  " + errorsLog.toAbsolutePath().normalize());
                }
                printReportLocationBanner(written);
                if (failed > 0) {
                    return 1;
                }
            } catch (Exception e) {
                System.err.println("Could not write report file: " + e.getMessage());
                return 2;
            }
            return 0;
        }
    }

    /** Last thing on stdout for {@code run-all} so the report path is easy to spot. */
    private static void printReportLocationBanner(Path reportFile) {
        String line = "=".repeat(72);
        System.out.println(line);
        System.out.println("Run-all report (PASS/FAIL summary) written to:");
        System.out.println("  " + reportFile);
        System.out.println(line);
    }

    /**
     * Resolves report directory: prefer {@code spring-microservices-questions/target/microservices-reports}
     * when the module folder exists under the current working directory; otherwise {@code target/microservices-reports}.
     */
    private static Path defaultRunAllReportPath() {
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        Path moduleReports = cwd.resolve("spring-microservices-questions").resolve("target").resolve("microservices-reports");
        Path dir = Files.isDirectory(cwd.resolve("spring-microservices-questions"))
                ? moduleReports
                : cwd.resolve("target").resolve("microservices-reports");
        String stamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
        return dir.resolve("run-all-" + stamp + ".txt");
    }

    private static void runLesson(MicroservicesStudyContext ctx, MicroserviceStudyLesson lesson) throws Exception {
        System.out.println("--- Lesson " + lesson.number() + " ---");
        System.out.println(lesson.title());
        lesson.run(ctx);
        System.out.println();
    }
}
