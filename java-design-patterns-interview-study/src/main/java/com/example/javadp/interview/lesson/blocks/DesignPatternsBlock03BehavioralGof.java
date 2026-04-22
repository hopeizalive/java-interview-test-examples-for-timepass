package com.example.javadp.interview.lesson.blocks;

import com.example.javadp.interview.lesson.DesignPatternsLesson;
import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <h2>Block 3 — Lessons 13–23: Gang-of-Four <em>behavioral</em> patterns</h2>
 *
 * <p>Behavioral patterns characterize <b>how objects collaborate and distribute responsibility</b>: algorithms,
 * notifications, state transitions, and visitor-style extensibility without endless conditionals.
 */
public final class DesignPatternsBlock03BehavioralGof {

    private DesignPatternsBlock03BehavioralGof() {}

    public static void run(DesignPatternsLesson lesson, StudyContext ctx) {
        switch (lesson) {
            case L13 -> lesson13Chain(ctx);
            case L14 -> lesson14Command(ctx);
            case L15 -> lesson15Interpreter(ctx);
            case L16 -> lesson16Iterator(ctx);
            case L17 -> lesson17Mediator(ctx);
            case L18 -> lesson18Memento(ctx);
            case L19 -> lesson19Observer(ctx);
            case L20 -> lesson20State(ctx);
            case L21 -> lesson21Strategy(ctx);
            case L22 -> lesson22TemplateMethod(ctx);
            case L23 -> lesson23Visitor(ctx);
            default -> throw new IllegalStateException("Block 3 received " + lesson);
        }
    }

    /** Chain of Responsibility: give more than one object a chance to handle a request by chaining receivers. */
    private static void lesson13Chain(StudyContext ctx) {
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

    /** Command: encapsulate a request as an object, letting you parameterize clients with queues, logs, undo. */
    private static void lesson14Command(StudyContext ctx) {
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

    /** Interpreter: given a language, define a representation for its grammar along with an interpreter engine. */
    private static void lesson15Interpreter(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Interpreter",
                "GoF Behavioral",
                "Model simple grammars as composable expression trees; evaluate by recursive interpret().",
                "Expression (add (number 2) (number 3)) evaluates to 5.");
        Expr expr = new AddExpr(new NumberExpr(2), new NumberExpr(3));
        ctx.log("interpret => " + expr.eval());
    }

    private interface Expr {
        int eval();
    }

    private record NumberExpr(int value) implements Expr {
        @Override
        public int eval() {
            return value;
        }
    }

    private record AddExpr(Expr left, Expr right) implements Expr {
        private AddExpr {
            Objects.requireNonNull(left);
            Objects.requireNonNull(right);
        }

        @Override
        public int eval() {
            return left.eval() + right.eval();
        }
    }

    /** Iterator: provide a way to access elements of an aggregate sequentially without exposing representation. */
    private static void lesson16Iterator(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Iterator",
                "GoF Behavioral",
                "Separate traversal from the collection; supports multiple traversal policies.",
                "BookShelf hides array index; foreach uses custom Iterator.");
        BookShelf shelf = new BookShelf();
        shelf.add("Clean Code");
        shelf.add("Effective Java");
        for (String title : shelf) {
            ctx.log("Book: " + title);
        }
    }

    private static final class BookShelf implements Iterable<String> {
        private final List<String> titles = new ArrayList<>();

        void add(String t) {
            titles.add(t);
        }

        @Override
        public Iterator<String> iterator() {
            return titles.iterator();
        }
    }

    /** Mediator: define an object that encapsulates how a set of objects interact; promotes loose coupling. */
    private static void lesson17Mediator(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Mediator",
                "GoF Behavioral",
                "Hub routes messages so colleagues do not reference each other directly.",
                "ChatRoom forwards alice.send to bob without Alice holding a direct Bob field.");
        ChatRoom room = new ChatRoom();
        Chatter alice = new Chatter("alice", room);
        Chatter bob = new Chatter("bob", room);
        alice.send(ctx, "bob", "hello");
        bob.send(ctx, "alice", "hi back");
    }

    private static final class ChatRoom {
        private final java.util.Map<String, Chatter> members = new java.util.HashMap<>();

        void join(Chatter c) {
            members.put(c.name(), c);
        }

        void route(StudyContext ctx, String from, String to, String text) {
            Chatter target = members.get(to);
            if (target == null) {
                ctx.log("No such user: " + to);
                return;
            }
            target.receive(ctx, from, text);
        }
    }

    private static final class Chatter {
        private final String name;
        private final ChatRoom room;

        Chatter(String name, ChatRoom room) {
            this.name = name;
            this.room = room;
            room.join(this);
        }

        String name() {
            return name;
        }

        void send(StudyContext ctx, String to, String text) {
            room.route(ctx, name, to, text);
        }

        void receive(StudyContext ctx, String from, String text) {
            ctx.log(name + " received from " + from + ": " + text);
        }
    }

    /** Memento: capture and externalize an object's internal state for later restoration without violating encapsulation. */
    private static void lesson18Memento(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Memento",
                "GoF Behavioral",
                "Snapshot originator state via a narrow memento; caretaker stores opaque tokens.",
                "Editor types, save(), types more, restore() returns to saved snapshot.");
        Editor editor = new Editor();
        Deque<Editor.Memento> history = new ArrayDeque<>();
        editor.append("hello");
        history.push(editor.save());
        editor.append(" world");
        ctx.log("Before undo: " + editor.text());
        editor.restore(history.pop());
        ctx.log("After undo: " + editor.text());
    }

    private static final class Editor {
        private StringBuilder buffer = new StringBuilder();

        void append(String s) {
            buffer.append(s);
        }

        String text() {
            return buffer.toString();
        }

        Memento save() {
            return new Memento(buffer.toString());
        }

        void restore(Memento m) {
            this.buffer = new StringBuilder(m.state());
        }

        private record Memento(String state) {}
    }

    /** Observer: define a one-to-many dependency so that when one object changes state, dependents are notified. */
    private static void lesson19Observer(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Observer",
                "GoF Behavioral",
                "Subject keeps a list of observers and pushes updates; decouples event source from listeners.",
                "Stock emits price change; two portfolios log reactions.");
        Stock stock = new Stock("JAVA", 100);
        stock.addObserver((s, p) -> ctx.log("ObserverA sees " + s + " at " + p));
        stock.addObserver((s, p) -> ctx.log("ObserverB sees " + s + " at " + p));
        stock.setPrice(110);
    }

    @FunctionalInterface
    private interface PriceObserver {
        void onPrice(String symbol, int price);
    }

    private static final class Stock {
        private final String symbol;
        private int price;
        private final List<PriceObserver> observers = new CopyOnWriteArrayList<>();

        Stock(String symbol, int price) {
            this.symbol = symbol;
            this.price = price;
        }

        void addObserver(PriceObserver o) {
            observers.add(o);
        }

        void setPrice(int newPrice) {
            this.price = newPrice;
            for (PriceObserver o : observers) {
                o.onPrice(symbol, price);
            }
        }
    }

    /** State: allow an object to alter its behavior when its internal state changes; object will appear to change class. */
    private static void lesson20State(StudyContext ctx) {
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

    /** Strategy: define a family of algorithms, encapsulate each one, and make them interchangeable. */
    private static void lesson21Strategy(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Strategy",
                "GoF Behavioral",
                "Inject an algorithm object instead of branching; clients stay stable when pricing rules change.",
                "Checkout totals the cart using PercentDiscount vs FixedDiscount strategies.");
        var cart = List.of(100, 50, 25);
        ctx.log("Percent 10% => " + total(cart, new PercentDiscount(0.10)));
        ctx.log("Fixed 15 off => " + total(cart, new FixedDiscount(15)));
    }

    private interface DiscountStrategy {
        int discountOn(int subtotal);
    }

    private record PercentDiscount(double rate) implements DiscountStrategy {
        @Override
        public int discountOn(int subtotal) {
            return (int) Math.round(subtotal * rate);
        }
    }

    private record FixedDiscount(int amount) implements DiscountStrategy {
        @Override
        public int discountOn(int subtotal) {
            return Math.min(amount, subtotal);
        }
    }

    private static int total(List<Integer> prices, DiscountStrategy strategy) {
        int subtotal = 0;
        for (int p : prices) {
            subtotal += p;
        }
        return subtotal - strategy.discountOn(subtotal);
    }

    /** Template Method: define the skeleton of an algorithm in a base class, letting subclasses refine certain steps. */
    private static void lesson22TemplateMethod(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Template Method",
                "GoF Behavioral",
                "Invariant steps live in a final method; hooks are overridden; the framework controls the flow.",
                "DataImporter.run() always open, parse, persist; CSV vs JSON override parse only.");
        new CsvImporter().run(ctx);
        new JsonImporter().run(ctx);
    }

    private abstract static class DataImporter {
        final void run(StudyContext ctx) {
            ctx.log("open resource");
            parse(ctx);
            ctx.log("persist rows");
        }

        protected abstract void parse(StudyContext ctx);
    }

    private static final class CsvImporter extends DataImporter {
        @Override
        protected void parse(StudyContext ctx) {
            ctx.log("parse CSV rows");
        }
    }

    private static final class JsonImporter extends DataImporter {
        @Override
        protected void parse(StudyContext ctx) {
            ctx.log("parse JSON documents");
        }
    }

    /** Visitor: represent an operation to be performed on elements of an object structure without changing classes. */
    private static void lesson23Visitor(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Visitor",
                "GoF Behavioral",
                "Double dispatch: accept(Visitor) routes to visitCircle/visitRectangle so new operations avoid editing shapes.",
            "AreaVisitor computes total area across a list of Shape elements.");
        List<Shape> shapes = List.of(new Circle(2), new RectangleShape(3, 4));
        AreaVisitor visitor = new AreaVisitor();
        for (Shape s : shapes) {
            s.accept(visitor);
        }
        ctx.log("Total area=" + visitor.total());
    }

    private interface Shape {
        void accept(ShapeVisitor v);
    }

    private interface ShapeVisitor {
        void visit(Circle c);

        void visit(RectangleShape r);
    }

    private static final class Circle implements Shape {
        private final double radius;

        Circle(double radius) {
            this.radius = radius;
        }

        double radius() {
            return radius;
        }

        @Override
        public void accept(ShapeVisitor v) {
            v.visit(this);
        }
    }

    private static final class RectangleShape implements Shape {
        private final double w;
        private final double h;

        RectangleShape(double w, double h) {
            this.w = w;
            this.h = h;
        }

        double w() {
            return w;
        }

        double h() {
            return h;
        }

        @Override
        public void accept(ShapeVisitor v) {
            v.visit(this);
        }
    }

    private static final class AreaVisitor implements ShapeVisitor {
        private double sum;

        double total() {
            return sum;
        }

        @Override
        public void visit(Circle c) {
            sum += Math.PI * c.radius() * c.radius();
        }

        @Override
        public void visit(RectangleShape r) {
            sum += r.w() * r.h();
        }
    }
}
