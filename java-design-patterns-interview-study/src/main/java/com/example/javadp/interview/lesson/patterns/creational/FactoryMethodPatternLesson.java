package com.example.javadp.interview.lesson.patterns.creational;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Factory Method: subclasses (or dedicated factory types) choose which concrete product to instantiate so callers
 * depend on abstractions.
 */
public final class FactoryMethodPatternLesson {

    private FactoryMethodPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.util.Calendar#getInstance(Locale)",
                "JDK factories pick calendar, number, or currency concrete classes based on locale or parameters.",
                "Say: creation is centralized; callers depend on Calendar, not GregorianCalendar.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "JDBC DriverManager",
                "Driver implementations register themselves; getConnection delegates to a driver that can handle the URL.",
                "SPI-style indirection: the factory surface is stable while vendors plug in products.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Spring BeanFactory / AbstractAutowireCapableBeanFactory",
                "Framework decides which concrete bean to construct from configuration metadata and scopes.",
                "Relate to IoC: object creation is externalized; Factory Method is one way to hide concrete types.");

        ctx.log("--- Java core tie-in ---");
        java.util.Calendar cal = java.util.Calendar.getInstance(java.util.Locale.ROOT);
        ctx.log("java.util.Calendar.getInstance(Locale) returns abstract Calendar; concrete class is " + cal.getClass().getName());

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
}
