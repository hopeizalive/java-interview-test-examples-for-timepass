package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.List;

/**
 * GoF Visitor: add operations over a stable object structure without editing each element class (double dispatch).
 */
public final class VisitorPatternLesson {

    private VisitorPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Java compiler annotation processors",
                "Visitors walk AST nodes (classes, methods) to generate code or validate constraints.",
                "Visitor keeps traversal logic separate from node types.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Jackson JsonSerializer graph walks",
                "Serialization visitors traverse object graphs applying rules per concrete type.",
                "Same pattern: new output format = new visitor, not N new methods on every model class.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "PDF rendering pipelines",
                "Document visitors emit drawing commands for text vs vector paths without bloating element classes.",
                "Cross-cutting rendering concerns isolated.");

        ctx.log("--- Java core tie-in ---");
        java.util.List.of(1, 2, 3).forEach(n -> ctx.log("java.lang.Iterable.forEach applies Consumer (internal iterator visitor): " + n));

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
