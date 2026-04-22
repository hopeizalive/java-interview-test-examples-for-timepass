package com.example.javadp.interview.lesson.blocks;

import com.example.javadp.interview.lesson.DesignPatternsLesson;
import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.Objects;

/**
 * <h2>Block 1 — Lessons 1–5: Gang-of-Four <em>creational</em> patterns</h2>
 *
 * <p>Creational patterns answer: <b>who constructs objects, and how do we hide construction complexity?</b> They keep
 * object graphs flexible when requirements change (new product families, new configuration steps, new cloning rules).
 *
 * <p>Interview arc: explain why Singleton is often discouraged except for true OS-level resources; when Factory Method
 * beats {@code new}; how Abstract Factory differs from Factory Method; why Builder helps with many optional
 * parameters; when Prototype avoids subclass explosion.
 */
public final class DesignPatternsBlock01CreationalGof {

    private DesignPatternsBlock01CreationalGof() {}

    public static void run(DesignPatternsLesson lesson, StudyContext ctx) {
        switch (lesson) {
            case L01 -> lesson01Singleton(ctx);
            case L02 -> lesson02FactoryMethod(ctx);
            case L03 -> lesson03AbstractFactory(ctx);
            case L04 -> lesson04Builder(ctx);
            case L05 -> lesson05Prototype(ctx);
            default -> throw new IllegalStateException("Block 1 received " + lesson);
        }
    }

    /**
     * Singleton: exactly one instance + global access. Here we use the enum idiom (effective Java) which gives
     * serialization and reflection safety for free.
     */
    private static void lesson01Singleton(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Singleton",
                "GoF Creational",
                "Restrict construction so one shared instance exists; access is centralized.",
                "Two calls to INSTANCE return the same object reference.");
        ctx.log("Same instance? " + (AppConfig.INSTANCE == AppConfig.INSTANCE));
        ctx.log("App name: " + AppConfig.INSTANCE.getName());
    }

    /** Enum singleton — thread-safe, single instance, minimal surface. */
    private enum AppConfig {
        INSTANCE;

        private final String name = "demo-app";

        String getName() {
            return name;
        }
    }

    /**
     * Factory Method: define an interface for creating an object, but let subclasses decide which class to
     * instantiate. The “creator” depends on abstractions, not concrete parsers.
     */
    private static void lesson02FactoryMethod(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Factory Method",
                "GoF Creational",
                "Defer instantiation to a subclass (or anonymous implementation) so clients depend on DocumentParser.",
                "Two concrete factories each produce a different Parser implementation without the client using new.");
        DocumentParser pdfLine = new PdfParserFactory().createParser();
        DocumentParser htmlLine = new HtmlParserFactory().createParser();
        ctx.log(pdfLine.parse("demo"));
        ctx.log(htmlLine.parse("demo"));
    }

    private interface DocumentParser {
        String parse(String raw);
    }

    private abstract static class ParserFactory {
        abstract DocumentParser createParser();
    }

    private static final class PdfParserFactory extends ParserFactory {
        @Override
        DocumentParser createParser() {
            return raw -> "PDF:" + raw;
        }
    }

    private static final class HtmlParserFactory extends ParserFactory {
        @Override
        DocumentParser createParser() {
            return raw -> "HTML:" + raw;
        }
    }

    /**
     * Abstract Factory: provide an interface for creating families of related objects without naming concrete
     * classes. Here “light” vs “dark” widget families share Button + Checkbox contracts.
     */
    private static void lesson03AbstractFactory(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Abstract Factory",
                "GoF Creational",
                "Compose interchangeable factories that produce matched sets of products (consistent theme).",
                "Same client code renders UI with LightThemeFactory vs DarkThemeFactory.");
        renderUi(ctx, new LightThemeFactory());
        renderUi(ctx, new DarkThemeFactory());
    }

    private static void renderUi(StudyContext ctx, UiFactory factory) {
        Button b = factory.createButton();
        Checkbox c = factory.createCheckbox();
        ctx.log(b.paint() + " + " + c.paint());
    }

    private interface Button {
        String paint();
    }

    private interface Checkbox {
        String paint();
    }

    private interface UiFactory {
        Button createButton();

        Checkbox createCheckbox();
    }

    private static final class LightThemeFactory implements UiFactory {
        @Override
        public Button createButton() {
            return () -> "LightButton";
        }

        @Override
        public Checkbox createCheckbox() {
            return () -> "LightCheckbox";
        }
    }

    private static final class DarkThemeFactory implements UiFactory {
        @Override
        public Button createButton() {
            return () -> "DarkButton";
        }

        @Override
        public Checkbox createCheckbox() {
            return () -> "DarkCheckbox";
        }
    }

    /**
     * Builder: construct a complex object step by step; the director (or fluent API) can enforce ordering and
     * defaults. Avoids telescoping constructors.
     */
    private static void lesson04Builder(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Builder",
                "GoF Creational",
                "Separate the construction algorithm from the representation; especially helpful with optional fields.",
                "Fluent steps set host/path/tls; build() validates and returns an immutable HttpRequest snapshot.");
        HttpRequest req =
                new HttpRequest.Builder().host("api.example").path("/v1/ping").tls(true).build();
        ctx.log(req.toString());
    }

    /** Immutable product; builder is static nested type (classic structure). */
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

    /**
     * Prototype: create new objects by copying a prototype instead of always subclassing factories. In Java,
     * {@link Cloneable} is fragile; copying via constructor or factory method from a template is clearer.
     */
    private static void lesson05Prototype(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Prototype",
                "GoF Creational",
                "Clone or copy-from-template to duplicate configured objects cheaply.",
                "SlideDeck copies slides list from a template deck without re-parsing source files.");
        SlideDeck template = new SlideDeck("Keynote", java.util.List.of("Intro", "Architecture", "Q&A"));
        SlideDeck copyForCustomerA = template.copy();
        ctx.log("Template title=" + template.title() + " slides=" + template.slides());
        ctx.log("Copy title=" + copyForCustomerA.title() + " slides=" + copyForCustomerA.slides());
        ctx.log("Different objects? " + (template != copyForCustomerA));
    }

    private record SlideDeck(String title, java.util.List<String> slides) {
        SlideDeck copy() {
            return new SlideDeck(title + " (copy)", java.util.List.copyOf(slides));
        }
    }
}
