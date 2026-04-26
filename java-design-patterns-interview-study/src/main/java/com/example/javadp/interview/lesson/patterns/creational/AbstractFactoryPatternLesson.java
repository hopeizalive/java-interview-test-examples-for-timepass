package com.example.javadp.interview.lesson.patterns.creational;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Abstract Factory: produce families of related objects (same theme) without tying callers to concrete classes.
 */
public final class AbstractFactoryPatternLesson {

    private AbstractFactoryPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "GUI toolkits",
                "Swing's UIManager and pluggable look-and-feel shipped coordinated buttons, borders, and colors per theme.",
                "Contrast with Factory Method: here the win is *consistency across a family*, not a single product.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Cloud SDK client families",
                "AWS v2 clients group configuration, credentials, and HTTP client so S3 + Dynamo feel the same.",
                "Abstract factory = one entry builds compatible collaborators for a deployment profile.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Test doubles vs production stacks",
                "Teams swap InMemoryMessagingFactory vs KafkaMessagingFactory in tests while domain code stays identical.",
                "Interview: show how you avoid if (test) new Fake() scattered through the codebase.");

        ctx.log("--- Java core tie-in ---");
        try {
            javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            ctx.log("javax.xml.parsers: one factory type builds a matched XML parser family: " + db.getClass().getName());
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            ctx.log("XML abstract factory demo skipped: " + e.getMessage());
        }

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
}
