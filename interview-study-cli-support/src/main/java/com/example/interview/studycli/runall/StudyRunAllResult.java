package com.example.interview.studycli.runall;

import java.nio.file.Path;
import java.util.List;

/**
 * Outcome of {@link StudyRunAllExecutor#execute(String, Path, Iterable)}.
 */
public record StudyRunAllResult(
        int passed,
        int failed,
        List<Integer> failedLessonNumbers,
        List<String> failedSummaries,
        Path stackTraceLogPath) {

    public int exitCode() {
        return failed > 0 ? 1 : 0;
    }
}
