package com.example.javadp.interview.lesson.patterns.structural;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Adapter: wrap an existing type so it satisfies the interface your new code expects, without modifying legacy
 * sources.
 */
public final class AdapterPatternLesson {

    private AdapterPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.io.InputStreamReader",
                "Adapts byte-oriented InputStream to character-oriented Reader for BufferedReader chains.",
                "Classic JDK adapter: two incompatible interfaces bridged by composition.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Jakarta Servlet HttpServletRequestWrapper",
                "Frameworks wrap the container request to add headers, caching, or security context for downstream filters.",
                "Mention decorator vs adapter: adapter fixes interface mismatch; decorator adds behavior with same type.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Legacy SOAP to REST facades",
                "Integration teams wrap old XML-RPC clients with REST-shaped ports so new microservices stay clean.",
                "Anti-corruption layer in DDD is often adapter-shaped.");

        ctx.log("--- Java core tie-in ---");
        try (java.io.InputStreamReader adapter =
                new java.io.InputStreamReader(
                        new java.io.ByteArrayInputStream("J".getBytes(java.nio.charset.StandardCharsets.UTF_8)),
                        java.nio.charset.StandardCharsets.UTF_8)) {
            int ch = adapter.read();
            ctx.log("java.io.InputStreamReader adapts bytes to chars; first code unit: " + ch);
        } catch (java.io.IOException e) {
            ctx.log("InputStreamReader demo: " + e.getMessage());
        }

        PatternLessonHeader.print(
                ctx,
                "Adapter",
                "GoF Structural",
                "Wrap an existing type so it satisfies a target interface without modifying legacy code.",
                "MetricWeatherService (Celsius) is adapted to ImperialReporter expecting Fahrenheit.");
        ImperialReporter reporter = new ImperialReporter(new CelsiusToFahrenheitAdapter(new MetricWeatherService()));
        ctx.log(reporter.describe());
    }

    private interface ImperialTemperature {
        double fahrenheit();
    }

    private static final class MetricWeatherService {
        double celsius() {
            return 20.0;
        }
    }

    private static final class CelsiusToFahrenheitAdapter implements ImperialTemperature {
        private final MetricWeatherService metric;

        CelsiusToFahrenheitAdapter(MetricWeatherService metric) {
            this.metric = metric;
        }

        @Override
        public double fahrenheit() {
            return metric.celsius() * 9.0 / 5.0 + 32.0;
        }
    }

    private static final class ImperialReporter {
        private final ImperialTemperature sensor;

        ImperialReporter(ImperialTemperature sensor) {
            this.sensor = sensor;
        }

        String describe() {
            return "Temperature (F): " + sensor.fahrenheit();
        }
    }
}
