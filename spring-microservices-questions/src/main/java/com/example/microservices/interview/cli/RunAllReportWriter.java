package com.example.microservices.interview.cli;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;

/**
 * Writes a machine- and human-readable summary for {@code run-all} (PASS/FAIL per lesson + totals).
 * Console output from each lesson is unchanged; this captures outcomes only.
 */
public final class RunAllReportWriter implements AutoCloseable {

    private final PrintWriter out;
    private final Path absolutePath;

    public RunAllReportWriter(Path reportFile) throws java.io.IOException {
        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        this.absolutePath = reportFile.toAbsolutePath().normalize();
        this.out = new PrintWriter(Files.newBufferedWriter(
                this.absolutePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING));
        out.println("microservices-study run-all report");
        out.println("writtenAt=" + Instant.now());
        out.println("user.dir=" + System.getProperty("user.dir"));
        out.println("java.version=" + System.getProperty("java.version"));
        out.println();
        out.flush();
    }

    public Path absolutePath() {
        return absolutePath;
    }

    public void lessonResult(int number, String title, boolean passed, String failureMessageOrNull) {
        if (passed) {
            out.printf("PASS  #%02d  %s%n", number, singleLine(title));
        } else {
            out.printf("FAIL  #%02d  %s%n", number, singleLine(title));
            String detail = failureMessageOrNull == null ? "(no message)" : singleLine(failureMessageOrNull);
            out.printf("      %s%n", detail);
        }
        out.flush();
    }

    public void summary(int passed, int failed, List<Integer> failedNumbers, List<String> failedMessages) {
        out.println();
        out.println("========== summary ==========");
        out.println("passed=" + passed);
        out.println("failed=" + failed);
        if (failed > 0) {
            for (int i = 0; i < failedNumbers.size(); i++) {
                out.println("  #" + failedNumbers.get(i) + " — " + singleLine(failedMessages.get(i)));
            }
        }
        out.println();
        out.println("outputFile=" + absolutePath);
        out.flush();
    }

    private static String singleLine(String s) {
        if (s == null) {
            return "";
        }
        return s.replace('\r', ' ').replace('\n', ' ').trim();
    }

    @Override
    public void close() {
        out.close();
    }
}
