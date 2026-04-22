package com.example.springannotation.interview.lesson.blocks;

import com.example.springannotation.interview.lesson.AnnotationLesson;
import com.example.springannotation.interview.lesson.fixtures.ConfigurationPropertiesFixtures;
import com.example.springannotation.interview.lesson.fixtures.WebMvcControllerFixtures;
import com.example.springannotation.interview.study.StudyContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Method;

/**
 * <h2>Block 2 — Lessons 11–20: configuration binding, conditionals narrative, MVC surface annotations</h2>
 *
 * <p><b>Story for the reader:</b> After beans exist, applications must bind configuration and expose HTTP APIs.
 * This block contrasts <em>structured binding</em> with one-off values, documents conditional <em>intent</em> even
 * when we do not boot every permutation, and inspects MVC handler metadata so you can articulate differences between
 * {@code @Controller} and {@code @RestController}, composed mappings, and binding annotations.
 *
 * <p><b>Interview arc:</b> defend {@code @ConfigurationProperties} for nested keys, explain profile vs property
 * toggles, and walk through how {@code @GetMapping} specializes {@code @RequestMapping}.
 */
public final class LessonBlock02ConfigurationAndWeb {

    private LessonBlock02ConfigurationAndWeb() {}

    public static void run(AnnotationLesson lesson, StudyContext ctx) throws Exception {
        switch (lesson) {
            case L11 -> lesson11(ctx);
            case L12 -> lesson12(ctx);
            case L13 -> lesson13(ctx);
            case L14 -> lesson14(ctx);
            case L15 -> lesson15(ctx);
            case L16 -> lesson16(ctx);
            case L17 -> lesson17(ctx);
            case L18 -> lesson18(ctx);
            case L19 -> lesson19(ctx);
            case L20 -> lesson20(ctx);
            default -> throw new IllegalStateException("Block 2 received " + lesson);
        }
    }

    /** Lesson 11 — Grouped properties objects model related keys; {@code @Value} is per-field and SpEL-capable. */
    private static void lesson11(StudyContext ctx) {
        ctx.log("Story: build a DbProps object manually to show the shape @ConfigurationProperties would bind.");
        var props = new ConfigurationPropertiesFixtures.DbProps();
        props.setUrl("jdbc:h2:mem:test");
        props.setPoolSize(10);
        ctx.log("Grouped properties object: " + props.getUrl() + " / " + props.getPoolSize());
        ctx.log("Scalar style would map each key one by one.");
        ctx.log("Takeaway: grouped binding scales; @Value is fine for a handful of primitives.");
    }

    /** Lesson 12 — Mutable setters vs immutable constructor-bound config records. */
    private static void lesson12(StudyContext ctx) {
        ctx.log("Story: same logical host/port as mutable POJO vs record.");
        var mutable = new ConfigurationPropertiesFixtures.MutableProps();
        mutable.setHost("localhost");
        var immutable = new ConfigurationPropertiesFixtures.ImmutableProps("localhost", 6379);
        ctx.log("Mutable props host set after create: " + mutable.getHost());
        ctx.log("Immutable props fixed at create: " + immutable.host() + ":" + immutable.port());
        ctx.log("Takeaway: immutable binding + validation is a strong production pattern.");
    }

    /** Lesson 13 — {@code @EnableConfigurationProperties} registers the properties bean with the context. */
    private static void lesson13(StudyContext ctx) {
        ctx.log("Story: refresh a tiny context that only exists to host @ConfigurationProperties beans.");
        try (var c = new AnnotationConfigApplicationContext(ConfigurationPropertiesFixtures.EnablePropsConfig.class)) {
            ctx.log("properties bean present: " + c.getBeanNamesForType(ConfigurationPropertiesFixtures.DbProps.class).length);
        }
        ctx.log("Takeaway: Boot 2.2+ often uses @ConfigurationPropertiesScan; explicit enable still shows the contract.");
    }

    private static void lesson14(StudyContext ctx) {
        ctx.log("Story: profiles partition environments; properties toggle features.");
        ctx.log("@Profile targets env partitioning (dev/test/prod).");
        ctx.log("@ConditionalOnProperty targets feature flags (on/off).");
        ctx.log("Takeaway: do not overload profiles for feature flags—property switches stay finer-grained.");
    }

    private static void lesson15(StudyContext ctx) {
        ctx.log("Story: narrate bean presence gates vs default bean fallback.");
        ctx.log("@ConditionalOnBean: add bean only when dependency exists.");
        ctx.log("@ConditionalOnMissingBean: provide default when user bean absent.");
        ctx.log("Takeaway: auto-configuration uses these to stay replaceable.");
    }

    private static void lesson16(StudyContext ctx) {
        ctx.log("Story: classpath gates vs property gates answer different questions.");
        boolean hasMap = java.util.Map.class != null;
        boolean featureOn = Boolean.parseBoolean("true");
        ctx.log("@ConditionalOnClass equivalent result: " + hasMap);
        ctx.log("@ConditionalOnProperty equivalent result: " + featureOn);
        ctx.log("Takeaway: @ConditionalOnClass avoids hard deps; property checks runtime intent.");
    }

    private static void lesson17(StudyContext ctx) {
        ctx.log("Story: matchIfMissing flips default enablement when keys are absent.");
        ctx.log("matchIfMissing=true -> feature enabled when key absent.");
        ctx.log("matchIfMissing=false -> feature disabled when key absent.");
        ctx.log("Takeaway: document defaults in metadata for operators.");
    }

    private static void lesson18(StudyContext ctx) {
        ctx.log("Story: contrast classic @PropertySource with Boot’s layered Environment.");
        ctx.log("@PropertySource attaches one file manually.");
        ctx.log("Boot model merges env vars, command args, profiles, and files.");
        ctx.log("Takeaway: prefer externalized config + relaxed binding for services.");
    }

    private static void lesson19(StudyContext ctx) throws Exception {
        ctx.log("Story: @RestController stacks @Controller + @ResponseBody semantics.");
        Method json = WebMvcControllerFixtures.ClassicController.class.getMethod("json");
        ctx.log("Controller has @ResponseBody directly: " + json.isAnnotationPresent(
                org.springframework.web.bind.annotation.ResponseBody.class));
        ctx.log("RestController class-level composed: "
                + WebMvcControllerFixtures.RestApiController.class.isAnnotationPresent(
                org.springframework.web.bind.annotation.RestController.class));
        ctx.log("Takeaway: APIs return bodies; MVC pages return view names—do not mix accidentally.");
    }

    private static void lesson20(StudyContext ctx) throws Exception {
        ctx.log("Story: composed mappings are specialized @RequestMapping shortcuts.");
        Method mGet = WebMvcControllerFixtures.RestApiController.class.getMethod("getBook");
        Method mPost = WebMvcControllerFixtures.RestApiController.class.getMethod("postBook",
                WebMvcControllerFixtures.BookBody.class);
        ctx.log("getBook has @GetMapping: " + mGet.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class));
        ctx.log("postBook has @PostMapping: " + mPost.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class));
        ctx.log("Takeaway: prefer composed annotations for clarity and static analysis.");
    }
}
