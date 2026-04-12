package com.example.interview.studycli.runall;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Central {@code run-all} loop: continue on failure, stderr one-liners via
 * {@link RunAllThrowableFormatter}, optional {@link RunAllStackTraceLogWriter}, then a standard summary.
 * <p>
 * New interview study modules should depend on {@code interview-study-cli-support} and delegate
 * {@code run-all} to this class so behavior stays consistent (see repo {@code guide.md}).
 */
public final class StudyRunAllExecutor {

    private StudyRunAllExecutor() {}

    /**
     * @param stackTraceLogBanner first line of the optional stack-trace file (module name + context)
     * @param stackTraceLogFile   if non-null, full stack traces are appended here on each failure
     * @param tasks               lesson attempts in order
     */
    public static StudyRunAllResult execute(
            String stackTraceLogBanner, Path stackTraceLogFile, Iterable<RunAllTask> tasks) throws IOException {
        RunAllStackTraceLogWriter traceWriter =
                stackTraceLogFile != null ? new RunAllStackTraceLogWriter(stackTraceLogFile, stackTraceLogBanner) : null;

        int passed = 0;
        List<Integer> failedNumbers = new ArrayList<>();
        List<String> failedMessages = new ArrayList<>();

        for (RunAllTask task : tasks) {
            try {
                task.action().run();
                passed++;
            } catch (Throwable t) {
                String msg = RunAllThrowableFormatter.rootCauseMessage(t);
                failedNumbers.add(task.number());
                failedMessages.add(msg);
                System.err.println("[FAILED] Lesson " + task.number() + ": " + msg);
                if (traceWriter != null) {
                    traceWriter.appendFailureSection(task.number(), task.title(), t);
                }
            }
        }

        int failed = failedNumbers.size();
        return new StudyRunAllResult(
                passed, failed, List.copyOf(failedNumbers), List.copyOf(failedMessages), stackTraceLogFile);
    }

    public static void printStandardSummary(StudyRunAllResult result, PrintStream out) {
        out.println("========== run-all complete: " + result.passed() + " passed, " + result.failed() + " failed ==========");
        if (result.failed() > 0) {
            out.println("Failed lessons:");
            for (int i = 0; i < result.failedLessonNumbers().size(); i++) {
                out.println("  #" + result.failedLessonNumbers().get(i) + " — " + result.failedSummaries().get(i));
            }
        }
    }

    /** Call when failures occurred and a stack-trace log path was configured (even if no write happened). */
    public static void printStackTraceLogPointer(StudyRunAllResult result, PrintStream out) {
        if (result.failed() > 0 && result.stackTraceLogPath() != null) {
            out.println();
            out.println("Full stack traces (all failures in this run):");
            out.println("  " + result.stackTraceLogPath().toAbsolutePath().normalize());
        }
    }
}
