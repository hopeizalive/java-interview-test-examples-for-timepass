package com.example.javadp.interview.lesson.patterns.modern;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * Null object: replace null references with a deliberate no-op implementation of the same interface.
 */
public final class NullObjectPatternLesson {

    private NullObjectPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.io.OutputStream#nullOutputStream()",
                "JDK ships a discarding stream so callers always invoke write without null checks.",
                "Official null object for I/O sinks.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "SLF4J NOP logger implementation",
                "Classpath without a binding still provides a no-op logger - avoids if (log != null) everywhere.",
                "Framework-level null object.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Collections emptyList() sentinel",
                "Returning immutable empty lists avoids null and still supports foreach uniformly.",
                "Related idea: safe default object instead of missing reference.");

        ctx.log("--- Java core tie-in ---");
        try (java.io.OutputStream sink = java.io.OutputStream.nullOutputStream()) {
            sink.write(new byte[] {1, 2, 3});
            ctx.log("java.io.OutputStream.nullOutputStream() discards writes (JDK null object).");
        } catch (java.io.IOException e) {
            ctx.log("nullOutputStream demo: " + e.getMessage());
        }

        PatternLessonHeader.print(
                ctx,
                "Null Object",
                "Modern / robustness",
                "Avoid null checks everywhere by injecting a logger that either prints or intentionally does nothing.",
                "ApplicationService logs with RealLogger vs NullLogger; both satisfy the same Logger interface.");
        new ApplicationService(new RealLogger(ctx)).run(ctx);
        new ApplicationService(NullLogger.INSTANCE).run(ctx);
    }

    private interface Logger {
        void info(String msg);
    }

    private static final class RealLogger implements Logger {
        private final StudyContext ctx;

        RealLogger(StudyContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void info(String msg) {
            ctx.log("[INFO] " + msg);
        }
    }

    private enum NullLogger implements Logger {
        INSTANCE;

        @Override
        public void info(String msg) {
            // intentional no-op - replaces null checks at call sites
        }
    }

    private static final class ApplicationService {
        private final Logger log;

        ApplicationService(Logger log) {
            this.log = log;
        }

        void run(StudyContext ctx) {
            log.info("starting job");
            ctx.log("job finished");
        }
    }
}
