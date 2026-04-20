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

/**
 * Failure handling and executor shutdown lifecycle demos (lessons 43-46).
 */
public final class DemoShutdown {

    private DemoShutdown() {}

    /** Lesson 43: uncaught exception handler as last-chance crash hook. */
    public static void l43(StudyContext ctx) throws Exception {
        ctx.log("UncaughtExceptionHandler: last resort for thread failures (log + alert).");
        // Execution story: intentionally crash a thread to prove handler hook receives uncaught failure.
        var saw = new boolean[] {false};
        Thread t = new Thread(() -> {
            throw new RuntimeException("worker boom");
        });
        t.setUncaughtExceptionHandler((th, ex) -> saw[0] = true);
        t.start();
        t.join();
        ctx.log("  handler ran: " + saw[0]);
    }

    /** Lesson 44: graceful shutdown with awaitTermination. */
    public static void l44(StudyContext ctx) throws Exception {
        ctx.log("shutdown(): stop accepting new tasks; awaitTermination waits for running tasks.");
        // Execution story: submit one task, initiate graceful shutdown, then wait for clean completion.
        ExecutorService ex = Executors.newFixedThreadPool(2);
        ex.submit(() -> sleep(100));
        ex.shutdown();
        boolean done = ex.awaitTermination(2, TimeUnit.SECONDS);
        ctx.log("  terminated in time: " + done);
    }

    /** Lesson 45: forceful shutdownNow and queued-task cancellation. */
    public static void l45(StudyContext ctx) throws Exception {
        ctx.log("shutdownNow(): interrupt workers; returns queued tasks; may not stop all work.");
        // Execution story: queue several long tasks, force shutdown, inspect how many never started.
        ExecutorService ex = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 5; i++) {
            ex.submit(() -> sleep(1000));
        }
        var pending = ex.shutdownNow();
        ctx.log("  cancelled queued tasks: " + pending.size());
        ex.awaitTermination(2, TimeUnit.SECONDS);
    }

    /** Lesson 46: Spring-managed executor bean shutdown on context close. */
    public static void l46(StudyContext ctx) {
        ctx.log("Spring @Bean(destroyMethod = \"shutdown\") on ExecutorService — tied to context lifecycle.");
        // Execution story: open temporary Spring context, run one task, close context, observe managed shutdown.
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
