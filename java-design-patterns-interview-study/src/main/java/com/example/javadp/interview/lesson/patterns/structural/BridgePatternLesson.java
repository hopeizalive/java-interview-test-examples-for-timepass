package com.example.javadp.interview.lesson.patterns.structural;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

/**
 * GoF Bridge: split an abstraction hierarchy from implementation so each dimension can vary independently.
 */
public final class BridgePatternLesson {

    private BridgePatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "JDBC Driver / Connection split",
                "java.sql.Connection is the abstraction; vendor-specific drivers supply implementations for different DBs.",
                "Avoids N-by-M class explosion like OracleThinPreparedStatement vs PostgresThinPreparedStatement stacks.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "AWT peer architecture (historical)",
                "Lightweight AWT components delegated to native peers per OS - abstraction vs platform bridge.",
                "Good historical anecdote even if most UI moved to JavaFX/Swing layers.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Payment orchestration",
                "Checkout flow (authorize/capture/refund) stays stable while PSP implementations (Stripe/Adyen) plug in.",
                "Same bridge idea: process abstraction + payment implementation.");

        ctx.log("--- Java core tie-in ---");
        java.util.concurrent.ExecutorService exec = java.util.concurrent.Executors.newSingleThreadExecutor();
        try {
            ctx.log("java.util.concurrent.ExecutorService is the abstraction; pool impl runs Runnable: "
                    + exec.submit(() -> "thread-pool").get());
        } catch (Exception e) {
            ctx.log("Executor demo skipped: " + e.getMessage());
        } finally {
            exec.shutdown();
        }

        PatternLessonHeader.print(
                ctx,
                "Bridge",
                "GoF Structural",
                "Split 'what clients see' from 'how it works underneath' with composition instead of inheritance stacks.",
                "Remote abstraction works with any Device implementation; swap TV vs Radio without subclass explosion.");
        Device tv = new Television();
        Device radio = new Radio();
        Remote basic = new BasicRemote(tv);
        Remote deluxe = new DeluxeRemote(radio);
        basic.togglePower();
        deluxe.togglePower();
        ctx.log("TV on? " + tv.isOn());
        ctx.log("Radio on? " + radio.isOn());
    }

    private interface Device {
        void powerOn();

        void powerOff();

        boolean isOn();
    }

    private static class Television implements Device {
        private boolean on;

        @Override
        public void powerOn() {
            on = true;
        }

        @Override
        public void powerOff() {
            on = false;
        }

        @Override
        public boolean isOn() {
            return on;
        }
    }

    private static class Radio implements Device {
        private boolean on;

        @Override
        public void powerOn() {
            on = true;
        }

        @Override
        public void powerOff() {
            on = false;
        }

        @Override
        public boolean isOn() {
            return on;
        }
    }

    private abstract static class Remote {
        protected final Device device;

        Remote(Device device) {
            this.device = device;
        }

        void togglePower() {
            if (device.isOn()) {
                device.powerOff();
            } else {
                device.powerOn();
            }
        }
    }

    private static final class BasicRemote extends Remote {
        BasicRemote(Device device) {
            super(device);
        }
    }

    private static final class DeluxeRemote extends Remote {
        DeluxeRemote(Device device) {
            super(device);
        }
    }
}
