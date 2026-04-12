package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.ConcurrencyStudyApplication;
import com.example.concurrency.interview.study.StudyContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class DemoShutdown {

    private DemoShutdown() {}

    public static void l43(StudyContext ctx) throws Exception {
        ctx.log("UncaughtExceptionHandler: last resort for thread failures (log + alert).");
        var saw = new boolean[] {false};
        Thread t = new Thread(() -> {
            throw new RuntimeException("worker boom");
        });
        t.setUncaughtExceptionHandler((th, ex) -> saw[0] = true);
        t.start();
        t.join();
        ctx.log("  handler ran: " + saw[0]);
    }

    public static void l44(StudyContext ctx) throws Exception {
        ctx.log("shutdown(): stop accepting new tasks; awaitTermination waits for running tasks.");
        ExecutorService ex = Executors.newFixedThreadPool(2);
        ex.submit(() -> sleep(100));
        ex.shutdown();
        boolean done = ex.awaitTermination(2, TimeUnit.SECONDS);
        ctx.log("  terminated in time: " + done);
    }

    public static void l45(StudyContext ctx) throws Exception {
        ctx.log("shutdownNow(): interrupt workers; returns queued tasks; may not stop all work.");
        ExecutorService ex = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 5; i++) {
            ex.submit(() -> sleep(1000));
        }
        var pending = ex.shutdownNow();
        ctx.log("  cancelled queued tasks: " + pending.size());
        ex.awaitTermination(2, TimeUnit.SECONDS);
    }

    public static void l46(StudyContext ctx) {
        ctx.log("Spring @Bean(destroyMethod = \"shutdown\") on ExecutorService — tied to context lifecycle.");
        try (ConfigurableApplicationContext app = startForL46()) {
            app.getBean("lessonExecutor", ExecutorService.class).submit(() -> ctx.log("  task ran"));
        }
        ctx.log("  context closed → executor shut down");
    }

    @Configuration
    static class ExecutorBeanConfig {
        @Bean(destroyMethod = "shutdown")
        ExecutorService lessonExecutor() {
            return Executors.newFixedThreadPool(2);
        }
    }

    private static ConfigurableApplicationContext startForL46() {
        SpringApplication app = new SpringApplication(ConcurrencyStudyApplication.class, ExecutorBeanConfig.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.setDefaultProperties(Map.of(
                "spring.main.banner-mode", "off",
                "spring.jmx.enabled", "false",
                "logging.level.root", "warn"));
        return app.run();
    }

    private static void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
