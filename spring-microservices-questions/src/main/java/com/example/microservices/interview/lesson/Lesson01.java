package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

/** Monolith vs microservices: trade-offs illustrated via Spring context startup. */
public final class Lesson01 extends AbstractMicroLesson {

    public Lesson01() {
        super(1, "Monolith vs microservices: compare sequential small contexts vs one combined context (StopWatch).");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        StopWatch sw = new StopWatch();
        sw.start("one-combined-context");
        try (var c = new AnnotationConfigApplicationContext(MonoConfig.class)) {
            c.getBean(String.class);
        }
        sw.stop();
        sw.start("two-isolated-contexts");
        try (var a = new AnnotationConfigApplicationContext(OrderCtx.class)) {
            a.getBean(String.class);
        }
        try (var b = new AnnotationConfigApplicationContext(BillingCtx.class)) {
            b.getBean(String.class);
        }
        sw.stop();
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
