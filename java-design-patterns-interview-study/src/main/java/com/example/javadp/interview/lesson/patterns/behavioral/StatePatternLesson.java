package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF State: delegate behavior to state objects so transitions replace large conditional logic.
 */
public final class StatePatternLesson {

    private StatePatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "TCP connection state machines",
                "RFC-defined states (CLOSED, ESTABLISHED, ...) change which operations are legal - same object, different behavior.",
                "Maps directly to this lesson's TcpConnection demo.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Order / ticket workflows",
                "E-commerce orders move Draft -> Paid -> Shipped; each state exposes different allowed commands.",
                "State pattern keeps transition rules near the state classes.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Media players",
                "Play/pause/stop semantics depend on current playback state; UI buttons delegate to the active state.",
                "Avoids giant switch on an enum scattered through the UI.");

        ctx.log("--- Java core tie-in ---");
        ctx.log("java.lang.Thread.State models thread lifecycle: " + Thread.currentThread().getState());

        PatternLessonHeader.print(
                ctx,
                "State",
                "GoF Behavioral",
                "Replace large switch statements with polymorphic state objects; transitions are explicit.",
                "TcpConnection delegates to ClosedState vs EstablishedState; open() transitions.");
        TcpConnection tcp = new TcpConnection();
        tcp.describe(ctx);
        tcp.open(ctx);
        tcp.describe(ctx);
        tcp.close(ctx);
        tcp.describe(ctx);
    }

    private interface TcpState {
        void open(TcpConnection ctx, StudyContext log);

        void close(TcpConnection ctx, StudyContext log);
    }

    private static final class TcpConnection {
        private TcpState state = new ClosedState();

        void open(StudyContext log) {
            state.open(this, log);
        }

        void close(StudyContext log) {
            state.close(this, log);
        }

        void transitionTo(TcpState next) {
            this.state = next;
        }

        void describe(StudyContext log) {
            log.log("TCP state=" + state.getClass().getSimpleName());
        }
    }

    private static final class ClosedState implements TcpState {
        @Override
        public void open(TcpConnection ctx, StudyContext log) {
            log.log("Opening from CLOSED");
            ctx.transitionTo(new EstablishedState());
        }

        @Override
        public void close(TcpConnection ctx, StudyContext log) {
            log.log("Already closed");
        }
    }

    private static final class EstablishedState implements TcpState {
        @Override
        public void open(TcpConnection ctx, StudyContext log) {
            log.log("Already open");
        }

        @Override
        public void close(TcpConnection ctx, StudyContext log) {
            log.log("Closing connection");
            ctx.transitionTo(new ClosedState());
        }
    }
}
