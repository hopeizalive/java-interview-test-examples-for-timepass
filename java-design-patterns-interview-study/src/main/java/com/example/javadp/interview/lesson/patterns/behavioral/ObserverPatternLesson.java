package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GoF Observer: one-to-many notification when a subject's state changes.
 */
public final class ObserverPatternLesson {

    private ObserverPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.beans.PropertyChangeSupport",
                "Swing and JavaBeans fire property change events to registered listeners on model updates.",
                "JDK's structured observer for bound properties.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Jakarta Messaging (JMS) topics",
                "Publishers broadcast messages; subscribers receive decoupled notifications without direct references.",
                "Distributed observer with reliability semantics.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Reactive Streams publishers",
                "Subscribers react to asynchronous emissions - push-based observer with backpressure rules.",
                "Contrast pull iterator vs push observer.");

        ctx.log("--- Java core tie-in ---");
        Object bean = new Object();
        java.beans.PropertyChangeSupport pcs = new java.beans.PropertyChangeSupport(bean);
        pcs.addPropertyChangeListener(
                evt -> ctx.log("PropertyChangeListener fired: " + evt.getPropertyName() + " " + evt.getOldValue() + "->" + evt.getNewValue()));
        pcs.firePropertyChange("score", 0, 1);

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
}
