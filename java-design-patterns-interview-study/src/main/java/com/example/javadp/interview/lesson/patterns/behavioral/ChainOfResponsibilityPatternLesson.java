package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Chain of Responsibility: give multiple handlers a chance to process a request without the sender knowing which
 * one will succeed.
 */
public final class ChainOfResponsibilityPatternLesson {

    private ChainOfResponsibilityPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Servlet FilterChain",
                "Each filter can short-circuit or pass to chain.doFilter - classic pipeline of responsibilities.",
                "Mention ordering in web.xml or @Order in Spring Boot.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Spring Security web filter stack",
                "Authentication, authorization, CSRF, and session management are discrete links in one chain.",
                "Interview: contrast with central if/else god method.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Logging pipeline appenders",
                "Some appenders handle only ERROR, others forward to downstream sinks until someone consumes the event.",
                "Same pattern: optional handling with a default sink at the end.");

        ctx.log("--- Java core tie-in ---");
        ClassLoader loader = ChainOfResponsibilityPatternLesson.class.getClassLoader();
        int depth = 0;
        while (loader != null && depth < 4) {
            ctx.log("ClassLoader delegation chain[" + depth + "]: " + loader.getClass().getName());
            loader = loader.getParent();
            depth++;
        }

        PatternLessonHeader.print(
                ctx,
                "Chain of Responsibility",
                "GoF Behavioral",
                "Avoid coupling sender to a single receiver; handlers form a linked list / pipeline.",
                "Support chain tries Tier1, then Tier2, then Tier3 until someone answers.");
        SupportHandler chain = new Tier1Support(new Tier2Support(new Tier3Support(null)));
        chain.handle(ctx, "reset password");
        chain.handle(ctx, "kernel panic");
    }

    private abstract static class SupportHandler {
        private final SupportHandler next;

        SupportHandler(SupportHandler next) {
            this.next = next;
        }

        final void handle(StudyContext ctx, String issue) {
            if (canHandle(issue)) {
                resolve(ctx, issue);
            } else if (next != null) {
                next.handle(ctx, issue);
            } else {
                ctx.log("No handler for: " + issue);
            }
        }

        protected abstract boolean canHandle(String issue);

        protected abstract void resolve(StudyContext ctx, String issue);
    }

    private static final class Tier1Support extends SupportHandler {
        Tier1Support(SupportHandler next) {
            super(next);
        }

        @Override
        protected boolean canHandle(String issue) {
            return issue.contains("password");
        }

        @Override
        protected void resolve(StudyContext ctx, String issue) {
            ctx.log("Tier1 fixed: " + issue);
        }
    }

    private static final class Tier2Support extends SupportHandler {
        Tier2Support(SupportHandler next) {
            super(next);
        }

        @Override
        protected boolean canHandle(String issue) {
            return issue.contains("network");
        }

        @Override
        protected void resolve(StudyContext ctx, String issue) {
            ctx.log("Tier2 fixed: " + issue);
        }
    }

    private static final class Tier3Support extends SupportHandler {
        Tier3Support(SupportHandler next) {
            super(next);
        }

        @Override
        protected boolean canHandle(String issue) {
            return true;
        }

        @Override
        protected void resolve(StudyContext ctx, String issue) {
            ctx.log("Tier3 escalated resolution: " + issue);
        }
    }
}
