package com.example.javadp.interview.lesson.patterns.modern;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * Dependency injection (Fowler): depend on abstractions; collaborators supplied externally - usually constructor
 * injection in modern Java.
 */
public final class DependencyInjectionPatternLesson {

    private DependencyInjectionPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Spring @Autowired / constructor injection",
                "ApplicationContext wires beans - classes declare dependencies on interfaces, not concrete lookups.",
                "Default story for enterprise Java interviews.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Jakarta CDI @Inject",
                "Java EE / Quarkus / Micronaut use CDI producers and scopes to satisfy injection points.",
                "Portable standard across runtimes.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Test doubles via DI",
                "Unit tests pass FakeClock or in-memory repositories without subclassing production code.",
                "Primary motivation: testability and explicit composition root.");

        ctx.log("--- Java core tie-in ---");
        java.util.ServiceLoader<java.nio.charset.spi.CharsetProvider> providers =
                java.util.ServiceLoader.load(java.nio.charset.spi.CharsetProvider.class);
        long count = providers.stream().count();
        ctx.log("java.util.ServiceLoader discovers CharsetProvider implementations (SPI / composition root): " + count);

        PatternLessonHeader.print(
                ctx,
                "Dependency Injection",
                "Modern / wiring (Martin Fowler) - not a GoF book pattern but universal in Java apps",
                "Invert control: the service never chooses its clock; tests inject a fake time source.",
                "ReportService receives Clock via constructor; production uses system clock, test uses fixed clock.");
        Clock fixed = () -> 1_700_000_000_000L;
        new ReportService(fixed).publish(ctx);
        new ReportService(System::currentTimeMillis).publish(ctx);
    }

    private interface Clock {
        long millis();
    }

    private static final class ReportService {
        private final Clock clock;

        ReportService(Clock clock) {
            this.clock = clock;
        }

        void publish(StudyContext ctx) {
            ctx.log("Report generated at epochMillis=" + clock.millis());
        }
    }
}
