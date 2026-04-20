package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

/**
 * Lesson 1 introduces monolith versus microservices trade-offs with a startup-time toy benchmark.
 *
 * <p>The lesson uses one combined Spring context versus two isolated contexts so readers can discuss
 * deployability and operational cost in concrete terms.
 */
public final class Lesson01 extends AbstractMicroLesson {

    public Lesson01() {
        super(1, "Monolith vs microservices: compare sequential small contexts vs one combined context (StopWatch).");
    }

    /**
     * Lesson 1: monolith and microservice boundary comparison.
     *
     * <p><b>Purpose:</b> Illustrate how decomposition changes runtime shape and team responsibilities.
     * <p><b>Role:</b> Opens the module with architectural framing before communication patterns.
     * <p><b>Demonstration:</b> Times one combined context against two isolated contexts and logs implications.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: measure one-process monolith-style startup first.
        StopWatch sw = new StopWatch();
        sw.start("one-combined-context");
        try (var c = new AnnotationConfigApplicationContext(MonoConfig.class)) {
            c.getBean(String.class);
        }
        sw.stop();
        // Story action: measure two independently booted bounded contexts.
        sw.start("two-isolated-contexts");
        try (var a = new AnnotationConfigApplicationContext(OrderCtx.class)) {
            a.getBean(String.class);
        }
        try (var b = new AnnotationConfigApplicationContext(BillingCtx.class)) {
            b.getBean(String.class);
        }
        sw.stop();
        // Story observation: runtime numbers are less important than architecture trade-off discussion.
        ctx.log(sw.prettyPrint());
        ctx.log("Talking point: microservices improve independent deployability; cost is network, consistency, and ops complexity.");
    }

    @Configuration
    static class MonoConfig {
        @Bean
        String monolithCore() {
            return "orders+billing";
        }
    }

    @Configuration
    static class OrderCtx {
        @Bean
        String orderSvc() {
            return "orders";
        }
    }

    @Configuration
    static class BillingCtx {
        @Bean
        String billingSvc() {
            return "billing";
        }
    }
}
