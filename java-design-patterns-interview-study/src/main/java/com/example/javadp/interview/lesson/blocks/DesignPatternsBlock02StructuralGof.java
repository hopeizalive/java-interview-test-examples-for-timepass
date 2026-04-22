package com.example.javadp.interview.lesson.blocks;

import com.example.javadp.interview.lesson.DesignPatternsLesson;
import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <h2>Block 2 — Lessons 6–12: Gang-of-Four <em>structural</em> patterns</h2>
 *
 * <p>Structural patterns explain <b>how classes and objects compose</b> to form larger structures while keeping
 * relationships flexible (adapter for legacy, bridge for dimensions, composite for trees, decorator for layering,
 * facade for simplicity, flyweight for sharing, proxy for controlled access).
 */
public final class DesignPatternsBlock02StructuralGof {

    private DesignPatternsBlock02StructuralGof() {}

    public static void run(DesignPatternsLesson lesson, StudyContext ctx) {
        switch (lesson) {
            case L06 -> lesson06Adapter(ctx);
            case L07 -> lesson07Bridge(ctx);
            case L08 -> lesson08Composite(ctx);
            case L09 -> lesson09Decorator(ctx);
            case L10 -> lesson10Facade(ctx);
            case L11 -> lesson11Flyweight(ctx);
            case L12 -> lesson12Proxy(ctx);
            default -> throw new IllegalStateException("Block 2 received " + lesson);
        }
    }

    /** Adapter: convert the interface of a class into another interface clients expect. */
    private static void lesson06Adapter(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Adapter",
                "GoF Structural",
                "Wrap an existing type so it satisfies a target interface without modifying legacy code.",
                "MetricWeatherService (Celsius) is adapted to ImperialReporter expecting Fahrenheit.");
        ImperialReporter reporter = new ImperialReporter(new CelsiusToFahrenheitAdapter(new MetricWeatherService()));
        ctx.log(reporter.describe());
    }

    private interface ImperialTemperature {
        double fahrenheit();
    }

    private static final class MetricWeatherService {
        double celsius() {
            return 20.0;
        }
    }

    /** Adapter holds adaptee and translates. */
    private static final class CelsiusToFahrenheitAdapter implements ImperialTemperature {
        private final MetricWeatherService metric;

        CelsiusToFahrenheitAdapter(MetricWeatherService metric) {
            this.metric = metric;
        }

        @Override
        public double fahrenheit() {
            return metric.celsius() * 9.0 / 5.0 + 32.0;
        }
    }

    private static final class ImperialReporter {
        private final ImperialTemperature sensor;

        ImperialReporter(ImperialTemperature sensor) {
            this.sensor = sensor;
        }

        String describe() {
            return "Temperature (F): " + sensor.fahrenheit();
        }
    }

    /**
     * Bridge: decouple an abstraction from its implementation so both can vary independently (two orthogonal
     * dimensions: device vs remote).
     */
    private static void lesson07Bridge(StudyContext ctx) {
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

    /** Composite: compose objects into tree structures to represent part-whole hierarchies uniformly. */
    private static void lesson08Composite(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Composite",
                "GoF Structural",
                "Treat individual objects and compositions of objects uniformly through a shared component API.",
                "GraphicGroup and Rectangle both implement Graphic; totalArea sums children recursively.");
        Graphic root = new GraphicGroup(
                "root", List.of(new Rectangle(2, 3), new GraphicGroup("nested", List.of(new Rectangle(1, 1)))));
        ctx.log("Total area: " + root.area());
    }

    private interface Graphic {
        double area();
    }

    private record Rectangle(double w, double h) implements Graphic {
        @Override
        public double area() {
            return w * h;
        }
    }

    private static final class GraphicGroup implements Graphic {
        private final String name;
        private final List<Graphic> children;

        GraphicGroup(String name, List<Graphic> children) {
            this.name = name;
            this.children = List.copyOf(children);
        }

        @Override
        public double area() {
            double sum = 0;
            for (Graphic g : children) {
                sum += g.area();
            }
            return sum;
        }

        @Override
        public String toString() {
            return "GraphicGroup(" + name + ")";
        }
    }

    /** Decorator: attach additional responsibilities to an object dynamically. */
    private static void lesson09Decorator(StudyContext ctx) {
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

    /** Facade: provide a unified higher-level interface to a subsystem of classes. */
    private static void lesson10Facade(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Facade",
                "GoF Structural",
                "Hide complexity behind one coherent operation; callers do not juggle low-level steps.",
                "ComputerFacade.boot() sequences CPU, memory, disk without exposing internals.");
        new ComputerFacade().boot(ctx);
    }

    private static final class ComputerFacade {
        private final Cpu cpu = new Cpu();
        private final Memory mem = new Memory();
        private final Disk disk = new Disk();

        void boot(StudyContext ctx) {
            ctx.log(cpu.freeze());
            ctx.log(mem.selfTest());
            ctx.log(disk.spinUp());
            ctx.log("Boot sequence complete.");
        }
    }

    private static final class Cpu {
        String freeze() {
            return "CPU microcode loaded";
        }
    }

    private static final class Memory {
        String selfTest() {
            return "Memory OK";
        }
    }

    private static final class Disk {
        String spinUp() {
            return "Disk ready";
        }
    }

    /**
     * Flyweight: use sharing to support large numbers of fine-grained objects efficiently. Separate intrinsic
     * (shared) state from extrinsic (context) state.
     */
    private static void lesson11Flyweight(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Flyweight",
                "GoF Structural",
                "Reuse immutable intrinsic data (glyph bitmap) while extrinsic position is passed per draw call.",
                "GlyphFactory returns same flyweight instance for 'A'; draw uses different x coordinates.");
        GlyphFactory factory = new GlyphFactory();
        Glyph a1 = factory.glyphFor('A');
        Glyph a2 = factory.glyphFor('A');
        ctx.log("Same flyweight for A? " + (a1 == a2));
        ctx.log(a1.draw(10));
        ctx.log(a1.draw(40));
    }

    private interface Glyph {
        String draw(int x);
    }

    private static final class GlyphFactory {
        private final Map<Character, Glyph> cache = new HashMap<>();

        Glyph glyphFor(char c) {
            return cache.computeIfAbsent(c, LetterGlyph::new);
        }
    }

    /** Intrinsic: the letter shape; extrinsic: x position passed into draw. */
    private record LetterGlyph(char symbol) implements Glyph {
        private LetterGlyph {
            Objects.requireNonNull(symbol);
        }

        @Override
        public String draw(int x) {
            return "draw '" + symbol + "' at x=" + x;
        }
    }

    /** Proxy: provide a surrogate to control access (lazy creation, caching, permission checks). */
    private static void lesson12Proxy(StudyContext ctx) {
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
