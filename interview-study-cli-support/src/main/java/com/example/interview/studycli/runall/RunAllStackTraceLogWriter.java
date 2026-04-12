package com.example.interview.studycli.runall;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Optional single file collecting <strong>full stack traces</strong> for every failed lesson in one
 * {@code run-all} invocation. First failure truncates and writes the header; later failures append.
 * <p>
 * Methods are {@code synchronized} so the same writer can be shared safely if run-all is ever
 * parallelized; sequential CLIs still benefit from a single documented concurrency story.
 */
public final class RunAllStackTraceLogWriter {

    private final Path path;
    private final String moduleBannerTitle;
    private boolean headerWritten;

    public RunAllStackTraceLogWriter(Path path, String moduleBannerTitle) {
        this.path = path;
        this.moduleBannerTitle = moduleBannerTitle;
    }

    public Path path() {
        return path;
    }

    public synchronized void appendFailureSection(int lessonNumber, String lessonTitle, Throwable t) throws IOException {
        var section = new StringBuilder(512);
        if (!headerWritten) {
            section.append(moduleBannerTitle).append(" — run-all failure log\n");
            section.append("Started: ").append(Instant.now()).append("\n");
            section.append("(One file for every failure in this run; not one file per lesson.)\n\n");
            headerWritten = true;
            Files.writeString(path, section.toString(), StandardCharsets.UTF_8, CREATE, TRUNCATE_EXISTING);
            section.setLength(0);
        }
        section.append("================================================================================\n");
        section.append("Lesson ").append(lessonNumber).append(" — ").append(lessonTitle).append('\n');
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
