package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Command: encapsulate a request as an object so you can queue, log, undo, or compose operations.
 */
public final class CommandPatternLesson {

    private CommandPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.lang.Runnable / Callable",
                "Thread pools execute opaque command objects without knowing concrete work - parameterization by behavior.",
                "Command as first-class value passed across threads.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "GUI undo stacks",
                "Text editors store reversible Command objects so Ctrl+Z pops and inverts prior mutations.",
                "Contrast macro recording: list of commands is a script.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Event sourcing append-only logs",
                "Domain events are serialized commands applied to aggregates - replay rebuilds state.",
                "Connect to CQRS lesson: commands are the write-side vocabulary.");

        ctx.log("--- Java core tie-in ---");
        java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newSingleThreadExecutor();
        try {
            java.util.concurrent.Future<?> f = pool.submit(() -> ctx.log("  Runnable command executed on thread pool"));
            f.get();
        } catch (Exception e) {
            ctx.log("Executor command demo skipped: " + e.getMessage());
        } finally {
            pool.shutdown();
        }

        PatternLessonHeader.print(
                ctx,
                "Command",
                "GoF Behavioral",
                "Turn an action into an object with execute()/undo(); decouple invoker from receiver.",
                "RemoteButton stores a Command; press() executes LightOnCommand against a Lamp.");
        Lamp lamp = new Lamp();
        RemoteButton on = new RemoteButton(new LightOnCommand(lamp));
        RemoteButton off = new RemoteButton(new LightOffCommand(lamp));
        on.press(ctx);
        off.press(ctx);
    }

    private static final class Lamp {
        private boolean on;

        void turnOn() {
            on = true;
        }

        void turnOff() {
            on = false;
        }

        boolean isOn() {
            return on;
        }
    }

    private interface Command {
        void execute(StudyContext ctx);
    }

    private static final class LightOnCommand implements Command {
        private final Lamp lamp;

        LightOnCommand(Lamp lamp) {
            this.lamp = lamp;
        }

        @Override
        public void execute(StudyContext ctx) {
            lamp.turnOn();
            ctx.log("Light ON, lamp.isOn=" + lamp.isOn());
        }
    }

    private static final class LightOffCommand implements Command {
        private final Lamp lamp;

        LightOffCommand(Lamp lamp) {
            this.lamp = lamp;
        }

        @Override
        public void execute(StudyContext ctx) {
            lamp.turnOff();
            ctx.log("Light OFF, lamp.isOn=" + lamp.isOn());
        }
    }

    private static final class RemoteButton {
        private final Command command;

        RemoteButton(Command command) {
            this.command = command;
        }

        void press(StudyContext ctx) {
            command.execute(ctx);
        }
    }
}
