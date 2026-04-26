package com.example.javadp.interview.lesson.patterns.creational;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.Objects;

/**
 * GoF Builder: construct a complex object stepwise; fluent APIs and validation at build() time avoid telescoping
 * constructors.
 */
public final class BuilderPatternLesson {

    private BuilderPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.net.http.HttpRequest.Builder",
                "Java 11 HttpClient uses a builder to set method, URI, headers, and body before an immutable request.",
                "Highlight immutability after build() - safe to share across threads.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Protobuf / Avro generated builders",
                "IDL compilers emit fluent setters for optional fields and required-field checks at build time.",
                "Good story for schema evolution: defaults and oneofs map cleanly to builder steps.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Test data builders",
                "Teams wrap constructors with TestUserBuilder so tests only override fields they care about.",
                "Interview: builder reduces duplication vs dozens of overloaded new User(...) helpers.");

        ctx.log("--- Java core tie-in ---");
        String built = new StringBuilder().append("java").append('.').append("lang.StringBuilder").toString();
        ctx.log("java.lang.StringBuilder fluent append chain builds text: " + built);

        PatternLessonHeader.print(
                ctx,
                "Builder",
                "GoF Creational",
                "Separate the construction algorithm from the representation; especially helpful with optional fields.",
                "Fluent steps set host/path/tls; build() validates and returns an immutable HttpRequest snapshot.");
        HttpRequest req = new HttpRequest.Builder().host("api.example").path("/v1/ping").tls(true).build();
        ctx.log(req.toString());
    }

    private record HttpRequest(String host, String path, boolean tls) {
        private static final class Builder {
            private String host;
            private String path = "/";
            private boolean tls;

            Builder host(String host) {
                this.host = host;
                return this;
            }

            Builder path(String path) {
                this.path = path;
                return this;
            }

            Builder tls(boolean tls) {
                this.tls = tls;
                return this;
            }

            HttpRequest build() {
                Objects.requireNonNull(host, "host");
                return new HttpRequest(host, path, tls);
            }
        }
    }
}
