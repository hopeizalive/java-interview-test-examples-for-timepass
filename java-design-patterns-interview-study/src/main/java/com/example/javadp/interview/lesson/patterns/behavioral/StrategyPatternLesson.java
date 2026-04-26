package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.List;

/**
 * GoF Strategy: encapsulate interchangeable algorithms behind one interface.
 */
public final class StrategyPatternLesson {

    private StrategyPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.util.Comparator",
                "Collections.sort and List.sort accept different Comparator strategies without changing element types.",
                "Strategy object passed into a stable algorithm (sort).");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "java.util.zip Deflater levels",
                "Compression speed vs size policies are swappable strategies around the same byte stream API.",
                "Infrastructure knob as strategy.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Payment capture flows",
                "Authorize now / capture later vs immediate charge are different strategies behind one checkout service.",
                "Business rules injected, not hard-coded branches.");

        ctx.log("--- Java core tie-in ---");
        java.util.ArrayList<String> words = new java.util.ArrayList<>(java.util.List.of("zebra", "apple"));
        words.sort(java.util.Comparator.naturalOrder());
        ctx.log("java.util.List.sort(Comparator): " + words);

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
}
