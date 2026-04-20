package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.ConcurrencyStudyApplication;
import com.example.concurrency.interview.study.StudyContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Scheduling examples for plain JDK and Spring schedulers (lessons 55-57).
 */
public final class DemoScheduled {

    /** Set before starting Spring context for lesson 56 (scheduled bean reads this). */
    static final AtomicReference<Lesson56State> L56_STATE = new AtomicReference<>();

    private DemoScheduled() {}

    /** Shared state container so lesson-56 scheduled bean can log into the active lesson context. */
    record Lesson56State(StudyContext ctx, CountDownLatch latch) {}

    /** Lesson 55: periodic ticks with {@code ScheduledExecutorService} outside Spring. */
    public static void l55(StudyContext ctx) throws Exception {
        ctx.log("ScheduledExecutorService: fixed-rate ticks without Spring.");
        try (var ex = Executors.newScheduledThreadPool(2)) {
            var latch = new CountDownLatch(3);
            // Execution story: schedule periodic task, capture first three ticks, then stop waiting.
            ex.scheduleAtFixedRate(() -> {
                ctx.log("  tick");
                latch.countDown();
            }, 0, 60, TimeUnit.MILLISECONDS);
            latch.await(2, TimeUnit.SECONDS);
        }
    }

    /** Lesson 56: Spring {@code @Scheduled} method lifecycle inside a temporary application context. */
    public static void l56(StudyContext ctx) throws Exception {
        ctx.log("@Scheduled on a Spring bean (extra @Configuration only for this lesson).");
        CountDownLatch latch = new CountDownLatch(3);
        // Execution story step 1: publish current lesson context so scheduled bean can log into this run.
        L56_STATE.set(new Lesson56State(ctx, latch));
        SpringApplication app = new SpringApplication(ConcurrencyStudyApplication.class, Lesson56Config.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.setDefaultProperties(java.util.Map.of(
                "spring.main.banner-mode", "off",
                "spring.jmx.enabled", "false",
                "logging.level.root", "warn"));
        try (ConfigurableApplicationContext c = app.run()) {
            // Execution story step 2: wait until scheduled method fires enough times.
            latch.await(5, TimeUnit.SECONDS);
        } finally {
            // Cleanup matters so later lessons do not accidentally reuse stale state.
            L56_STATE.set(null);
        }
    }

    @Configuration
    @EnableScheduling
    static class Lesson56Config {
        @Bean
        Lesson56Runner lesson56Runner() {
            return new Lesson56Runner();
        }
    }

    static class Lesson56Runner {
        @Scheduled(initialDelay = 400, fixedDelay = 120)
        void tick() {
            Lesson56State s = L56_STATE.get();
            if (s != null && s.latch().getCount() > 0) {
                s.ctx().log("  @Scheduled tick");
                s.latch().countDown();
            }
        }
    }

    /** Lesson 57: programmatic scheduling with {@link ThreadPoolTaskScheduler}. */
    public static void l57(StudyContext ctx) throws Exception {
        ctx.log("ThreadPoolTaskScheduler: explicit Spring scheduler bean for programmatic scheduling.");
        var ts = new ThreadPoolTaskScheduler();
        ts.setPoolSize(2);
        ts.setThreadNamePrefix("lesson57-");
        ts.initialize();
        var latch = new CountDownLatch(2);
        // Execution story: start fixed-rate schedule, observe two ticks, then shut scheduler down explicitly.
        ts.scheduleAtFixedRate(
                () -> {
                    ctx.log("  TaskScheduler tick");
                    latch.countDown();
                },
                Duration.ofMillis(120));
        latch.await(3, TimeUnit.SECONDS);
        ts.shutdown();
    }
}
