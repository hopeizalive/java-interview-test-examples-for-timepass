package com.example.javadp.interview.lesson.patterns.structural;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Proxy: surrogate in front of the real subject to add lazy init, access control, logging, or remote indirection.
 */
public final class ProxyPatternLesson {

    private ProxyPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.lang.reflect.Proxy and Spring AOP",
                "Interfaces are implemented by generated proxies that intercept calls before delegating to a target bean.",
                "Mention JDK dynamic proxies vs CGLIB subclass proxies.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Hibernate lazy associations",
                "Proxies stand in for unloaded collections or entities until the session actually touches them.",
                "Classic lazy-loading proxy - indirection hides expensive work until needed.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "API gateways",
                "Edge proxies authenticate, rate-limit, and cache before forwarding to internal microservices.",
                "Structural proxy at the network boundary.");

        ctx.log("--- Java core tie-in ---");
        Runnable real = () -> ctx.log("  (real Runnable.run invoked through proxy)");
        Runnable proxied = (Runnable)
                java.lang.reflect.Proxy.newProxyInstance(
                        ProxyPatternLesson.class.getClassLoader(),
                        new Class<?>[] {Runnable.class},
                        (proxy, method, args) -> {
                            ctx.log("  java.lang.reflect.Proxy intercept: " + method.getName());
                            return method.invoke(real, args);
                        });
        proxied.run();

        PatternLessonHeader.print(
                ctx,
                "Proxy",
                "GoF Structural",
                "Stand in front of a real subject to add indirection: lazy init, logging, access control.",
                "ImageProxy delays loading disk bytes until display() is called.");
        Image img = new ImageProxy("banner.png");
        ctx.log("Created proxy (no load yet)");
        img.display(ctx);
        img.display(ctx);
    }

    private interface Image {
        void display(StudyContext ctx);
    }

    private static final class ImageProxy implements Image {
        private final String filename;
        private HighResImage real;

        ImageProxy(String filename) {
            this.filename = filename;
        }

        @Override
        public void display(StudyContext ctx) {
            if (real == null) {
                real = new HighResImage(filename);
            }
            real.display(ctx);
        }
    }

    private static final class HighResImage implements Image {
        private final String filename;

        HighResImage(String filename) {
            this.filename = filename;
        }

        @Override
        public void display(StudyContext ctx) {
            ctx.log("Displaying bytes of " + filename);
        }
    }
}
