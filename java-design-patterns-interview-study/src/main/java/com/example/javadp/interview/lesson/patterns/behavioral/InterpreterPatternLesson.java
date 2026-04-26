package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.Objects;

/**
 * GoF Interpreter: represent a grammar as composable expression objects and evaluate via interpret().
 */
public final class InterpreterPatternLesson {

    private InterpreterPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Spring Expression Language (SpEL)",
                "Expressions parse into an AST; the evaluator walks nodes to resolve properties and method calls.",
                "Same interpreter shape: composite expressions + recursive eval.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "SQL optimizers and rule engines",
                "Predicates become trees (AND/OR/NOT) that planners rewrite and cost - interpreter over a grammar.",
                "Note: heavy languages usually move to parser generators, but the idea persists.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Regular expressions engines",
                "Pattern.compile builds internal automaton/AST; matcher interprets input against that structure.",
                "Interview: interpreter shines for small, composable DSLs.");

        ctx.log("--- Java core tie-in ---");
        boolean matches = java.util.regex.Pattern.compile("a+b").matcher("aab").matches();
        ctx.log("java.util.regex.Pattern + Matcher interpret a grammar over input: " + matches);

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
}
