package com.example.javadp.interview.lesson.patterns.structural;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.List;

/**
 * GoF Composite: treat individual objects and compositions uniformly through a shared component interface.
 */
public final class CompositePatternLesson {

    private CompositePatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.awt.Container / Component",
                "Swing containers hold child components; layout and painting recurse through the tree uniformly.",
                "Classic UI tree: leaf widgets and panels share the same base type.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Kubernetes API objects",
                "Higher-level resources (Deployment) compose ReplicaSet and Pod templates as nested specs.",
                "Operations APIs often expose recursive JSON - composite thinking keeps clients simple.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Budget roll-ups",
                "Finance systems sum cost centers; departments and regions are composites over leaf accounts.",
                "Same totalArea() idea: one operation walks the whole structure.");

        ctx.log("--- Java core tie-in ---");
        javax.swing.tree.DefaultMutableTreeNode uiRoot = new javax.swing.tree.DefaultMutableTreeNode("root");
        uiRoot.add(new javax.swing.tree.DefaultMutableTreeNode("leaf"));
        ctx.log("javax.swing.tree.DefaultMutableTreeNode getChildCount (composite tree API): " + uiRoot.getChildCount());

        PatternLessonHeader.print(
                ctx,
                "Composite",
                "GoF Structural",
                "Treat individual objects and compositions of objects uniformly through a shared component API.",
                "GraphicGroup and Rectangle both implement Graphic; totalArea sums children recursively.");
        Graphic root = new GraphicGroup(
                "root", List.of(new Rect(2, 3), new GraphicGroup("nested", List.of(new Rect(1, 1)))));
        ctx.log("Total area: " + root.area());
    }

    private interface Graphic {
        double area();
    }

    private record Rect(double w, double h) implements Graphic {
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
}
