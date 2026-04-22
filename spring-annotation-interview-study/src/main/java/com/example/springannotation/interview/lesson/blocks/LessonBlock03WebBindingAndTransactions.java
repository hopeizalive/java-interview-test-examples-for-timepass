package com.example.springannotation.interview.lesson.blocks;

import com.example.springannotation.interview.lesson.AnnotationLesson;
import com.example.springannotation.interview.lesson.fixtures.TransactionAnnotationFixtures;
import com.example.springannotation.interview.lesson.fixtures.WebMvcControllerFixtures;
import com.example.springannotation.interview.study.StudyContext;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * <h2>Block 3 — Lessons 21–30: request binding, HTTP semantics, persistence/transaction metadata</h2>
 *
 * <p><b>Story for the reader:</b> Controllers are the façade; transactions and repositories guard consistency.
 * This block walks binding annotations first, then documents how {@code @Transactional} metadata composes
 * (precedence, rollback rules, propagation, isolation, read-only hints) without needing a live database for every
 * lesson.
 *
 * <p><b>Interview arc:</b> articulate path vs query vs body, when {@code @ResponseStatus} is enough, and how
 * {@code @Transactional} behaves at class vs method scope.
 */
public final class LessonBlock03WebBindingAndTransactions {

    private LessonBlock03WebBindingAndTransactions() {}

    public static void run(AnnotationLesson lesson, StudyContext ctx) throws Exception {
        switch (lesson) {
            case L21 -> lesson21(ctx);
            case L22 -> lesson22(ctx);
            case L23 -> lesson23(ctx);
            case L24 -> lesson24(ctx);
            case L25 -> lesson25(ctx);
            case L26 -> lesson26(ctx);
            case L27 -> lesson27(ctx);
            case L28 -> lesson28(ctx);
            case L29 -> lesson29(ctx);
            case L30 -> lesson30(ctx);
            default -> throw new IllegalStateException("Block 3 received " + lesson);
        }
    }

    private static void lesson21(StudyContext ctx) throws Exception {
        ctx.log("Story: path variables identify the resource; query params refine the read.");
        Method m = WebMvcControllerFixtures.RestApiController.class.getMethod("findBook", String.class, String.class);
        var p = m.getParameters();
        ctx.log("arg0 uses @PathVariable: " + p[0].isAnnotationPresent(
                org.springframework.web.bind.annotation.PathVariable.class));
        ctx.log("arg1 uses @RequestParam: " + p[1].isAnnotationPresent(RequestParam.class));
        ctx.log("Takeaway: REST shape communicates intent to caches, proxies, and humans.");
    }

    private static void lesson22(StudyContext ctx) throws Exception {
        ctx.log("Story: JSON bodies map to objects via @RequestBody and HttpMessageConverters.");
        Method m = WebMvcControllerFixtures.RestApiController.class.getMethod("postBook",
                WebMvcControllerFixtures.BookBody.class);
        ctx.log("method payload uses @RequestBody: " + m.getParameters()[0].isAnnotationPresent(RequestBody.class));
        ctx.log("Takeaway: never confuse query form fields with JSON payloads in API reviews.");
    }

    private static void lesson23(StudyContext ctx) throws Exception {
        ctx.log("Story: @ResponseStatus fixes HTTP code for a handler; ResponseEntity adds branches.");
        Method m = WebMvcControllerFixtures.ClassicController.class.getMethod("created");
        ctx.log("method has @ResponseStatus: " + m.isAnnotationPresent(ResponseStatus.class));
        ctx.log("ResponseEntity style supports dynamic per-branch statuses.");
        ctx.log("Takeaway: choose declarative status when static; choose ResponseEntity when conditional.");
    }

    private static void lesson24(StudyContext ctx) {
        ctx.log("Story: @ControllerAdvice centralizes exception mapping; controllers may still declare local handlers.");
        ctx.log("Global advice present: "
                + WebMvcControllerFixtures.GlobalErrors.class.isAnnotationPresent(
                org.springframework.web.bind.annotation.ControllerAdvice.class));
        ctx.log("Local handler present: "
                + Arrays.stream(WebMvcControllerFixtures.ClassicController.class.getDeclaredMethods())
                .anyMatch(mm -> mm.isAnnotationPresent(
                        org.springframework.web.bind.annotation.ExceptionHandler.class)));
        ctx.log("Takeaway: advice beans keep controllers thin and policies consistent.");
    }

    private static void lesson25(StudyContext ctx) throws Exception {
        ctx.log("Story: @ModelAttribute binds form fields; @RequestBody expects a serialized representation.");
        Method m1 = WebMvcControllerFixtures.ClassicController.class.getMethod("formSave",
                WebMvcControllerFixtures.BookBody.class);
        Method m2 = WebMvcControllerFixtures.RestApiController.class.getMethod("postBook",
                WebMvcControllerFixtures.BookBody.class);
        ctx.log("formSave uses @ModelAttribute: " + m1.getParameters()[0].isAnnotationPresent(ModelAttribute.class));
        ctx.log("postBook uses @RequestBody: " + m2.getParameters()[0].isAnnotationPresent(RequestBody.class));
        ctx.log("Takeaway: mixing these on the same DTO shape is a common integration bug.");
    }

    private static void lesson26(StudyContext ctx) {
        ctx.log("Story: @CrossOrigin advertises browser permissions on the controller type.");
        ctx.log("Controller has local @CrossOrigin: "
                + WebMvcControllerFixtures.CorsController.class.isAnnotationPresent(
                org.springframework.web.bind.annotation.CrossOrigin.class));
        ctx.log("Takeaway: prefer gateway/global CORS in large systems; local is fine for demos.");
    }

    private static void lesson27(StudyContext ctx) {
        ctx.log("Story: @Repository participates in exception translation when persistence stack is present.");
        SQLExceptionTranslator translator = new org.springframework.jdbc.support.SQLStateSQLExceptionTranslator();
        var translated = translator.translate("insert", "INSERT INTO x", new SQLException("boom", "23505"));
        ctx.log("translated exception type: " + translated.getClass().getSimpleName());
        ctx.log("Takeaway: interviewers tie @Repository to DataAccessException hierarchy.");
    }

    private static void lesson28(StudyContext ctx) throws Exception {
        ctx.log("Story: method-level @Transactional metadata overrides class-level defaults.");
        Transactional classTx = TransactionAnnotationFixtures.TxService.class.getAnnotation(Transactional.class);
        Transactional methodTx =
                TransactionAnnotationFixtures.TxService.class.getMethod("fastRead").getAnnotation(Transactional.class);
        ctx.log("class readOnly: " + classTx.readOnly());
        ctx.log("method readOnly override: " + methodTx.readOnly());
        ctx.log("Takeaway: most-specific wins - same rule as many Spring annotation models.");
    }

    private static void lesson29(StudyContext ctx) throws Exception {
        ctx.log("Story: default rollback listens to RuntimeException; checked exceptions need rollbackFor.");
        Transactional defaultTx =
                TransactionAnnotationFixtures.TxService.class.getMethod("defaultRollback").getAnnotation(Transactional.class);
        Transactional checkedTx =
                TransactionAnnotationFixtures.TxService.class.getMethod("rollbackForChecked").getAnnotation(Transactional.class);
        ctx.log("default rollbackFor count: " + defaultTx.rollbackFor().length);
        ctx.log("custom rollbackFor count: " + checkedTx.rollbackFor().length);
        ctx.log("Takeaway: never assume IOException rolls back without declaring it.");
    }

    private static void lesson30(StudyContext ctx) throws Exception {
        ctx.log("Story: propagation metadata answers join-or-suspend when nested calls occur.");
        var req = TransactionAnnotationFixtures.TxService.class.getMethod("required").getAnnotation(Transactional.class).propagation();
        var reqNew =
                TransactionAnnotationFixtures.TxService.class.getMethod("requiresNew").getAnnotation(Transactional.class).propagation();
        ctx.log("required propagation: " + req);
        ctx.log("requiresNew propagation: " + reqNew);
        ctx.log("Takeaway: REQUIRES_NEW is the audit/logging escape hatch from the outer transaction.");
    }
}
