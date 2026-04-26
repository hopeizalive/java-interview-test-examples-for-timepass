package com.example.javadp.interview.lesson.patterns.structural;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Facade: one coherent operation hides a multi-step subsystem so callers stay simple.
 */
public final class FacadePatternLesson {

    private FacadePatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.nio.file.Files helper methods",
                "High-level helpers like readString(Path) orchestrate charset handling, buffering, and close semantics.",
                "Facade vs utility: still hiding several cooperating objects behind one call.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Spring JdbcTemplate",
                "execute/query/update hide connection acquisition, statement prep, and exception translation.",
                "Interview: facade simplifies; it doesn't remove the need to understand transactions underneath.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Payment checkout APIs",
                "POST /checkout may fan out to inventory, tax, fraud, and ledger services but exposes one endpoint.",
                "Relate to BFF pattern: orchestration layer as facade for frontends.");

        ctx.log("--- Java core tie-in ---");
        try {
            java.nio.file.Path p = java.nio.file.Files.createTempFile("javadp-facade", ".txt");
            java.nio.file.Files.writeString(p, "x");
            ctx.log("java.nio.file.Files.writeString/readString one-call facade over streams: len="
                    + java.nio.file.Files.readString(p).length());
            java.nio.file.Files.deleteIfExists(p);
        } catch (java.io.IOException e) {
            ctx.log("Files facade demo skipped: " + e.getMessage());
        }

        PatternLessonHeader.print(
                ctx,
                "Facade",
                "GoF Structural",
                "Hide complexity behind one coherent operation; callers do not juggle low-level steps.",
                "ComputerFacade.boot() sequences CPU, memory, disk without exposing internals.");
        new ComputerFacade().boot(ctx);
    }

    private static final class ComputerFacade {
        private final Cpu cpu = new Cpu();
        private final Memory mem = new Memory();
        private final Disk disk = new Disk();

        void boot(StudyContext ctx) {
            ctx.log(cpu.freeze());
            ctx.log(mem.selfTest());
            ctx.log(disk.spinUp());
            ctx.log("Boot sequence complete.");
        }
    }

    private static final class Cpu {
        String freeze() {
            return "CPU microcode loaded";
        }
    }

    private static final class Memory {
        String selfTest() {
            return "Memory OK";
        }
    }

    private static final class Disk {
        String spinUp() {
            return "Disk ready";
        }
    }
}
