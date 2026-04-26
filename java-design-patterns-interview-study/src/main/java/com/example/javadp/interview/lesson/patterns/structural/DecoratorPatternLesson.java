package com.example.javadp.interview.lesson.patterns.structural;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Decorator: attach responsibilities dynamically by wrapping a component and delegating through, preserving the
 * original type.
 */
public final class DecoratorPatternLesson {

    private DecoratorPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.io.BufferedInputStream",
                "Wraps another InputStream to add buffering without changing callers that already accept InputStream.",
                "JDK streams are the textbook decorator stack.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Spring @Transactional / @Cacheable proxies",
                "Framework-generated proxies wrap beans to add cross-cutting behavior around existing interfaces.",
                "Contrast with subclassing: open-closed - add aspects without editing business classes.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "HTTP client middleware",
                "Libraries wrap transports with retries, metrics, and auth refresh while keeping the core client API.",
                "Same pattern: outer decorator sees request/response; inner does I/O.");

        ctx.log("--- Java core tie-in ---");
        java.util.List<Integer> base = new java.util.ArrayList<>(java.util.List.of(1));
        java.util.List<Integer> synced = java.util.Collections.synchronizedList(base);
        synced.add(2);
        ctx.log("java.util.Collections.synchronizedList decorates List; size=" + synced.size());

        PatternLessonHeader.print(
                ctx,
                "Decorator",
                "GoF Structural",
                "Wrap a component and delegate through, adding behavior before/after (open-closed friendly).",
                "Beverage is wrapped by MilkDecorator then SugarDecorator; cost accumulates.");
        Beverage coffee = new SugarDecorator(new MilkDecorator(new Coffee()));
        ctx.log(coffee.describe() + " cost=" + coffee.cost());
    }

    private interface Beverage {
        int cost();

        String describe();
    }

    private static final class Coffee implements Beverage {
        @Override
        public int cost() {
            return 2;
        }

        @Override
        public String describe() {
            return "Coffee";
        }
    }

    private abstract static class BeverageDecorator implements Beverage {
        private final Beverage inner;

        BeverageDecorator(Beverage inner) {
            this.inner = inner;
        }

        @Override
        public int cost() {
            return inner.cost();
        }

        @Override
        public String describe() {
            return inner.describe();
        }
    }

    private static final class MilkDecorator extends BeverageDecorator {
        MilkDecorator(Beverage inner) {
            super(inner);
        }

        @Override
        public int cost() {
            return super.cost() + 1;
        }

        @Override
        public String describe() {
            return super.describe() + " + milk";
        }
    }

    private static final class SugarDecorator extends BeverageDecorator {
        SugarDecorator(Beverage inner) {
            super(inner);
        }

        @Override
        public int cost() {
            return super.cost() + 1;
        }

        @Override
        public String describe() {
            return super.describe() + " + sugar";
        }
    }
}
