package com.example.springannotation.interview.lesson.blocks;

import com.example.springannotation.interview.lesson.AnnotationLesson;
import com.example.springannotation.interview.lesson.fixtures.TransactionAnnotationFixtures;
import com.example.springannotation.interview.lesson.fixtures.ValidationAnnotationFixtures;
import com.example.springannotation.interview.study.StudyContext;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * <h2>Block 4 — Lessons 31–40: isolation/read-only metadata, proxy story, validation, testing slices</h2>
 *
 * <p><b>Story for the reader:</b> Transactions and validation are cross-cutting concerns expressed through
 * annotations. Testing annotations describe <em>how much Spring you load</em> in a test. This block keeps most
 * stories reflective (metadata + logs) so run-all stays fast while still teaching the vocabulary interviewers use.
 *
 * <p><b>Interview arc:</b> explain isolation trade-offs, the self-invocation trap, Bean Validation groups, and why
 * {@code @WebMvcTest} is narrower than {@code @SpringBootTest}.
 */
public final class LessonBlock04ValidationAndTestingNarrative {

    private LessonBlock04ValidationAndTestingNarrative() {}

    public static void run(AnnotationLesson lesson, StudyContext ctx) throws Exception {
        switch (lesson) {
            case L31 -> lesson31(ctx);
            case L32 -> lesson32(ctx);
            case L33 -> lesson33(ctx);
            case L34 -> lesson34(ctx);
            case L35 -> lesson35(ctx);
            case L36 -> lesson36(ctx);
            case L37 -> lesson37(ctx);
            case L38 -> lesson38(ctx);
            case L39 -> lesson39(ctx);
            case L40 -> lesson40(ctx);
            default -> throw new IllegalStateException("Block 4 received " + lesson);
        }
    }

    private static void lesson31(StudyContext ctx) throws Exception {
        ctx.log("Story: isolation levels trade anomalies vs concurrency; read metadata from methods.");
        var readCommitted =
                TransactionAnnotationFixtures.TxService.class.getMethod("readCommitted").getAnnotation(Transactional.class).isolation();
        var repeatableRead =
                TransactionAnnotationFixtures.TxService.class.getMethod("repeatableRead").getAnnotation(Transactional.class).isolation();
        ctx.log("readCommitted isolation: " + readCommitted);
        ctx.log("repeatableRead isolation: " + repeatableRead);
        ctx.log("Takeaway: defaults follow the datasource; explicit isolation is rare but interview-heavy.");
    }

    private static void lesson32(StudyContext ctx) throws Exception {
        ctx.log("Story: readOnly=true hints optimization to the persistence provider.");
        var tx = TransactionAnnotationFixtures.TxService.class.getMethod("fastRead").getAnnotation(Transactional.class);
        ctx.log("fastRead readOnly: " + tx.readOnly());
        ctx.log("Takeaway: combine readOnly with repository slice for read models.");
    }

    private static void lesson33(StudyContext ctx) {
        ctx.log("Story: internal this.method() calls skip Spring’s proxy; @Transactional on inner callee may never start.");
        ctx.log("this.innerTxCall() bypasses proxy interception path.");
        ctx.log("Move method to another bean or self-inject proxied bean.");
        ctx.log("Takeaway: interviewers love this trap—know the fix patterns.");
    }

    private static void lesson34(StudyContext ctx) {
        ctx.log("Story: @EnableTransactionManagement switches on annotation-driven tx for imperative stacks.");
        ctx.log("Tx config has @EnableTransactionManagement: "
                + TransactionAnnotationFixtures.TxConfig.class.isAnnotationPresent(
                org.springframework.transaction.annotation.EnableTransactionManagement.class));
        ctx.log("Takeaway: Boot auto-config often hides this, but custom tests still expose it.");
    }

    private static void lesson35(StudyContext ctx) {
        ctx.log("Story: Hibernate Validator evaluates constraints; @Valid triggers cascades on graphs.");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        int v1 = validator.validate(new ValidationAnnotationFixtures.InputDto("")).size();
        int v2 = validator.validate(new ValidationAnnotationFixtures.InputDto("ok")).size();
        ctx.log("violations for blank payload: " + v1);
        ctx.log("violations for valid payload: " + v2);
        ctx.log("Takeaway: @Validated enables SpEL-aware validation on @RequestParam/@PathVariable when needed.");
    }

    private static void lesson36(StudyContext ctx) {
        ctx.log("Story: null vs blank vs empty differ for strings—constraints encode precise semantics.");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        int nullCount = validator.validate(new ValidationAnnotationFixtures.TripleConstraints(null, null, null)).size();
        int emptyCount = validator.validate(new ValidationAnnotationFixtures.TripleConstraints("", "", "")).size();
        ctx.log("violations with null fields: " + nullCount);
        ctx.log("violations with empty fields: " + emptyCount);
        ctx.log("Takeaway: @NotBlank implies @NotNull for strings—do not stack redundantly in APIs.");
    }

    private static void lesson37(StudyContext ctx) {
        ctx.log("Story: @InitBinder customizes WebDataBinder per controller.");
        boolean hasInitBinder = java.util.Arrays.stream(ValidationAnnotationFixtures.BinderController.class.getDeclaredMethods())
                .anyMatch(m -> m.getName().equals("bind"));
        ctx.log("custom binder method available: " + hasInitBinder);
        ctx.log("Takeaway: use for field whitelist/blacklist and custom editors.");
    }

    private static void lesson38(StudyContext ctx) {
        ctx.log("Story: global Validator vs annotation-only is an architecture trade-off.");
        ctx.log("Global validator bean allows one place for conversion/validation rules.");
        ctx.log("Annotation-only setup is simpler but less centralized.");
        ctx.log("Takeaway: large teams often centralize cross-field rules in a Validator bean.");
    }

    private static void lesson39(StudyContext ctx) {
        ctx.log("Story: slice tests trade fidelity for speed—describe the slices without booting each here.");
        ctx.log("@SpringBootTest: loads full context (slowest, widest).");
        ctx.log("@WebMvcTest: controller/web slice only (faster, narrower).");
        ctx.log("Takeaway: pick the narrowest slice that still proves the behavior under test.");
    }

    private static void lesson40(StudyContext ctx) {
        ctx.log("Story: @DataJpaTest focuses on repositories + TestEntityManager defaults.");
        ctx.log("@DataJpaTest: repository + JPA slice.");
        ctx.log("@SpringBootTest: all configured layers.");
        ctx.log("Takeaway: slice failures often mean missing @Import of a small config.");
    }
}
