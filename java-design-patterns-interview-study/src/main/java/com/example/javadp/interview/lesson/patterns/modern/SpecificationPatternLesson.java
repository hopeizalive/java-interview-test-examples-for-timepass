package com.example.javadp.interview.lesson.patterns.modern;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Specification (DDD / Eric Evans): composable business predicates replacing scattered validation and query if-chains.
 */
public final class SpecificationPatternLesson {

    private SpecificationPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Domain-driven validation",
                "Rules like premium customers must have verified email become reusable specs composed with AND/OR.",
                "Keeps policy in one place instead of duplicating checks across controllers and batch jobs.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Query objects / Criteria APIs",
                "JPA Criteria or QueryDSL build predicates programmatically - structurally similar to composable specs.",
                "Interview: specs can drive both in-memory filters and translated SQL when paired with infrastructure.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Feature flags + eligibility",
                "Product marketing combines specs (in region X AND beta cohort) to decide who sees a feature.",
                "Same compositional pattern outside classic DDD texts.");

        ctx.log("--- Java core tie-in ---");
        java.util.function.Predicate<Integer> positive = n -> n > 0;
        java.util.function.Predicate<Integer> small = n -> n < 10;
        ctx.log("java.util.function.Predicate.and composes rules: test(5)=" + positive.and(small).test(5));

        PatternLessonHeader.print(
                ctx,
                "Specification",
                "Modern / DDD rules",
                "Business rules become small objects; combine them instead of scattering if-chains.",
                "IsInStock AND IsPremium filters a list of Product records.");
        List<Product> catalog =
                List.of(new Product("a", true, true), new Product("b", true, false), new Product("c", false, true));
        Specification<Product> spec = new AndSpec<>(new InStockSpec(), new PremiumSpec());
        List<String> names = new ArrayList<>();
        for (Product p : catalog) {
            if (spec.isSatisfiedBy(p)) {
                names.add(p.name());
            }
        }
        ctx.log("Matching products: " + names);
    }

    private record Product(String name, boolean inStock, boolean premium) {}

    private interface Specification<T> {
        boolean isSatisfiedBy(T candidate);
    }

    private record AndSpec<T>(Specification<T> left, Specification<T> right) implements Specification<T> {
        private AndSpec {
            Objects.requireNonNull(left);
            Objects.requireNonNull(right);
        }

        @Override
        public boolean isSatisfiedBy(T candidate) {
            return left.isSatisfiedBy(candidate) && right.isSatisfiedBy(candidate);
        }
    }

    private static final class InStockSpec implements Specification<Product> {
        @Override
        public boolean isSatisfiedBy(Product candidate) {
            return candidate.inStock();
        }
    }

    private static final class PremiumSpec implements Specification<Product> {
        @Override
        public boolean isSatisfiedBy(Product candidate) {
            return candidate.premium();
        }
    }
}
