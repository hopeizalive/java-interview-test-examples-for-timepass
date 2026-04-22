package com.example.springannotation.interview.lesson.blocks;

import com.example.springannotation.interview.lesson.AnnotationLesson;
import com.example.springannotation.interview.lesson.fixtures.StereotypeAndRegistryFixtures;
import com.example.springannotation.interview.study.StudyContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * <h2>Block 1 — Lessons 1–10: stereotypes, bean registration, configuration proxy, composition, scan, import, wiring,
 * injection style, laziness</h2>
 *
 * <p><b>Story for the reader:</b> Spring’s annotation model is not random decoration. Each stereotype and
 * configuration choice changes <em>how the container discovers beans</em>, <em>how it wires them</em>, and
 * <em>when it creates them</em>. This block builds a mental model bottom-up: markers → scanned beans vs explicit
 * {@code @Bean} → {@code @Configuration} singleton semantics → bootstrapping composition → classpath scanning
 * contracts → explicit imports → disambiguation → injection shape → lazy creation.
 *
 * <p><b>Interview arc:</b> after this block you should explain, without slides, why {@code @Configuration} method
 * inter-calls return shared singletons, why {@code @Qualifier} beats {@code @Primary}, and why field injection is
 * discouraged in modern Spring codebases.
 */
public final class LessonBlock01StereotypesAndContext {

    private LessonBlock01StereotypesAndContext() {}

    public static void run(AnnotationLesson lesson, StudyContext ctx) throws Exception {
        switch (lesson) {
            case L01 -> lesson01(ctx);
            case L02 -> lesson02(ctx);
            case L03 -> lesson03(ctx);
            case L04 -> lesson04(ctx);
            case L05 -> lesson05(ctx);
            case L06 -> lesson06(ctx);
            case L07 -> lesson07(ctx);
            case L08 -> lesson08(ctx);
            case L09 -> lesson09(ctx);
            case L10 -> lesson10(ctx);
            default -> throw new IllegalStateException("Block 1 received " + lesson);
        }
    }

    /**
     * Lesson 1 — Stereotype markers only reflect intent; most behavior is shared unless an extension kicks in
     * (e.g. {@code @Repository} exception translation when used with persistence infrastructure).
     */
    private static void lesson01(StudyContext ctx) {
        ctx.log("Story: stereotypes tag layers for humans and some subsystems; verify markers exist on types.");
        ctx.log("Service has @Service: "
                + StereotypeAndRegistryFixtures.LayeredService.class.isAnnotationPresent(Service.class));
        ctx.log("Repo has @Repository: "
                + StereotypeAndRegistryFixtures.LayeredRepository.class.isAnnotationPresent(Repository.class));
        ctx.log("Comp has @Component: "
                + StereotypeAndRegistryFixtures.GenericComponent.class.isAnnotationPresent(Component.class));
        ctx.log("Takeaway: prefer the narrowest stereotype that documents your layer.");
    }

    /**
     * Lesson 2 — {@code @Bean} registers return values; {@code @Component} on a class registers the type itself.
     * Explicit {@code register} shows both paths without component-scan noise.
     */
    private static void lesson02(StudyContext ctx) {
        ctx.log("Story: compare explicit registration of a @Configuration + @Bean vs a @Component type.");
        try (var c = new AnnotationConfigApplicationContext()) {
            c.register(
                    StereotypeAndRegistryFixtures.BeanConfig.class,
                    StereotypeAndRegistryFixtures.GenericComponent.class);
            c.refresh();
            ctx.log("genericComponent bean exists: " + c.containsBean("genericComponent"));
            ctx.log("factoryCreated bean exists: " + c.containsBean("factoryCreated"));
        }
        ctx.log("Takeaway: @Bean is for types you do not own or for factory wiring; @Component is for your types.");
    }

    /** Lesson 3 — Full {@code @Configuration} proxies {@code @Bean} methods so inter-calls hit the container. */
    private static void lesson03(StudyContext ctx) {
        ctx.log("Story: two @Bean methods call the same dep() helper; proxy must return one instance.");
        try (var c = new AnnotationConfigApplicationContext(StereotypeAndRegistryFixtures.ConfigInterCall.class)) {
            StereotypeAndRegistryFixtures.PairHolder holder = c.getBean(StereotypeAndRegistryFixtures.PairHolder.class);
            ctx.log("same dependency instance through @Configuration: " + (holder.left() == holder.right()));
        }
        ctx.log("Takeaway: this is the canonical interview proof of @Configuration method interception.");
    }

    /** Lesson 4 — {@code proxyBeanMethods=false} turns @Bean methods into plain Java calls unless you inject deps. */
    private static void lesson04(StudyContext ctx) {
        ctx.log("Story: same PairHolder pattern with proxy true vs false; false breaks singleton on direct calls.");
        try (var c1 = new AnnotationConfigApplicationContext(StereotypeAndRegistryFixtures.ProxyTrueConfig.class);
                var c2 = new AnnotationConfigApplicationContext(StereotypeAndRegistryFixtures.ProxyFalseConfig.class)) {
            StereotypeAndRegistryFixtures.PairHolder p1 = c1.getBean(StereotypeAndRegistryFixtures.PairHolder.class);
            StereotypeAndRegistryFixtures.PairHolder p2 = c2.getBean(StereotypeAndRegistryFixtures.PairHolder.class);
            ctx.log("proxyBeanMethods=true returns same dep: " + (p1.left() == p1.right()));
            ctx.log("proxyBeanMethods=false creates new dep: " + (p2.left() == p2.right()));
        }
        ctx.log("Takeaway: Boot auto-config often sets proxyBeanMethods=false for startup; prefer @Bean params then.");
    }

    /** Lesson 5 — {@code @SpringBootApplication} is a composed shortcut; introspect metadata instead of booting. */
    private static void lesson05(StudyContext ctx) {
        ctx.log("Story: show the three concerns folded into one convenience annotation.");
        var md = org.springframework.core.type.AnnotationMetadata.introspect(
                StereotypeAndRegistryFixtures.ExplicitBootLike.class);
        ctx.log("Has @EnableAutoConfiguration: " + md.hasAnnotation(
                org.springframework.boot.autoconfigure.EnableAutoConfiguration.class.getName()));
        ctx.log("Has @ComponentScan: " + md.hasAnnotation(
                org.springframework.context.annotation.ComponentScan.class.getName()));
        ctx.log("Has @SpringBootConfiguration: " + md.hasAnnotation(
                org.springframework.boot.SpringBootConfiguration.class.getName()));
        ctx.log("Takeaway: you can replace @SpringBootApplication with explicit pieces when you need tight control.");
    }

    /** Lesson 6 — Default {@code @ComponentScan} has no explicit basePackages; adding them narrows the search cone. */
    private static void lesson06(StudyContext ctx) {
        ctx.log("Story: read ComponentScan metadata from two marker types.");
        var defaultScan =
                StereotypeAndRegistryFixtures.DefaultScanRoot.class.getAnnotation(
                        org.springframework.context.annotation.ComponentScan.class);
        var explicitScan =
                StereotypeAndRegistryFixtures.ExplicitScanRoot.class.getAnnotation(
                        org.springframework.context.annotation.ComponentScan.class);
        ctx.log("default scan has explicit basePackages: " + (defaultScan.basePackages().length > 0));
        ctx.log("explicit scan packages: " + java.util.Arrays.toString(explicitScan.basePackages()));
        ctx.log("Takeaway: misplaced main class loses beans; explicit basePackages fixes accidental empty scans.");
    }

    /** Lesson 7 — {@code @Import} wires types deterministically without scanning the whole classpath. */
    private static void lesson07(StudyContext ctx) {
        ctx.log("Story: ImportedRoot imports a nested config that declares a single @Bean.");
        try (var c = new AnnotationConfigApplicationContext(StereotypeAndRegistryFixtures.ImportedRoot.class)) {
            ctx.log("Imported bean present: " + c.containsBean("importedOnlyBean"));
        }
        ctx.log("Takeaway: feature modules and tests often use @Import for explicit graphs.");
    }

    /** Lesson 8 — {@code @Qualifier} at injection point beats {@code @Primary} when both candidates exist. */
    private static void lesson08(StudyContext ctx) {
        ctx.log("Story: two Sender beans; @Primary picks email, but constructor asks for smsSender by name.");
        try (var c = new AnnotationConfigApplicationContext(StereotypeAndRegistryFixtures.QualifierConfig.class)) {
            StereotypeAndRegistryFixtures.MessageClient client =
                    c.getBean(StereotypeAndRegistryFixtures.MessageClient.class);
            ctx.log("Injected sender name: " + client.sender().name());
        }
        ctx.log("Takeaway: @Primary is a default; @Qualifier is the surgical override.");
    }

    /** Lesson 9 — Field injection hides dependencies; constructor injection pairs with final fields. */
    private static void lesson09(StudyContext ctx) throws Exception {
        ctx.log("Story: reflect on FieldInjected vs ConstructorInjected field modifiers.");
        var fField = StereotypeAndRegistryFixtures.FieldInjected.class.getDeclaredFields()[0];
        var cField = StereotypeAndRegistryFixtures.ConstructorInjected.class.getDeclaredFields()[0];
        ctx.log("Field-injected dependency mutable flag: " + !java.lang.reflect.Modifier.isFinal(fField.getModifiers()));
        ctx.log("Constructor-injected dependency immutable flag: " + java.lang.reflect.Modifier.isFinal(cField.getModifiers()));
        ctx.log("Takeaway: interviews favor constructor injection + final fields for clarity and tests.");
    }

    /** Lesson 10 — {@code @Lazy} delays creation until first use; counter proves single materialization. */
    private static void lesson10(StudyContext ctx) {
        ctx.log("Story: ExpensiveService increments counter in ctor; lazy proxy avoids ctor until touch().");
        try (var c = new AnnotationConfigApplicationContext(StereotypeAndRegistryFixtures.LazyConfig.class)) {
            var counter = c.getBean(java.util.concurrent.atomic.AtomicInteger.class);
            ctx.log("Before lazy access, constructor calls: " + counter.get());
            c.getBean(StereotypeAndRegistryFixtures.LazyConsumer.class).touch();
            ctx.log("After lazy access, constructor calls: " + counter.get());
        }
        ctx.log("Takeaway: @Lazy breaks eager cycles and defers heavy beans; misuse can hide startup failures.");
    }
}
