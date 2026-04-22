package com.example.springannotation.interview.lesson.blocks;

import com.example.springannotation.interview.lesson.AnnotationLesson;
import com.example.springannotation.interview.lesson.fixtures.AsyncSchedulingCacheRetryFixtures;
import com.example.springannotation.interview.study.StudyContext;
import org.springframework.retry.annotation.Retryable;

import java.lang.reflect.Method;

/**
 * <h2>Block 5 — Lessons 41–50: testing vocabulary recap, async/scheduling/cache, ordering, events, retry</h2>
 *
 * <p><b>Story for the reader:</b> Modern Spring services are asynchronous, cached, scheduled, and resilient.
 * Annotations declare cross-cutting behavior, but tests must deliberately choose how much context to load.
 * This block finishes with metadata-driven proofs so run-all stays deterministic.
 *
 * <p><b>Interview arc:</b> contrast {@code @MockBean} with Mockito, describe {@code @Async} limitations (same-bean
 * calls), and read {@code @Scheduled} attributes precisely.
 */
public final class LessonBlock05AsyncCachingAndRetry {

    private LessonBlock05AsyncCachingAndRetry() {}

    public static void run(AnnotationLesson lesson, StudyContext ctx) throws Exception {
        switch (lesson) {
            case L41 -> lesson41(ctx);
            case L42 -> lesson42(ctx);
            case L43 -> lesson43(ctx);
            case L44 -> lesson44(ctx);
            case L45 -> lesson45(ctx);
            case L46 -> lesson46(ctx);
            case L47 -> lesson47(ctx);
            case L48 -> lesson48(ctx);
            case L49 -> lesson49(ctx);
            case L50 -> lesson50(ctx);
            default -> throw new IllegalStateException("Block 5 received " + lesson);
        }
    }

    private static void lesson41(StudyContext ctx) {
        ctx.log("Story: @MockBean is a Spring Test integration; @Mock is plain unit test wiring.");
        ctx.log("@MockBean replaces bean in Spring context.");
        ctx.log("@Mock/@InjectMocks stays outside Spring container.");
        ctx.log("Takeaway: integration vs unit clarity keeps flaky tests manageable.");
    }

    private static void lesson42(StudyContext ctx) {
        ctx.log("Story: @TestConfiguration is test-scoped wiring without leaking into main @Configuration.");
        ctx.log("@TestConfiguration lets tests override/add beans without polluting prod config.");
        ctx.log("Takeaway: prefer it over component-scan hacks in test packages.");
    }

    private static void lesson43(StudyContext ctx) {
        ctx.log("Story: MockMvc exercises DispatcherServlet without sockets when configured.");
        ctx.log("@AutoConfigureMockMvc gives HTTP-layer assertions without real network port.");
        ctx.log("Takeaway: pair with slice tests for fast controller contracts.");
    }

    private static void lesson44(StudyContext ctx) {
        ctx.log("Story: Spring Test often wraps tests in transactions that roll back by default.");
        ctx.log("Transactional tests default to rollback after each test method.");
        ctx.log("Takeaway: assert side effects via in-memory collaborators when rollback hides DB writes.");
    }

    private static void lesson45(StudyContext ctx) throws Exception {
        ctx.log("Story: @EnableAsync installs async execution infrastructure; @Async marks fire-and-forget entry points.");
        ctx.log("Async config enabled: "
                + AsyncSchedulingCacheRetryFixtures.AsyncConfig.class.isAnnotationPresent(
                org.springframework.scheduling.annotation.EnableAsync.class));
        Method m = AsyncSchedulingCacheRetryFixtures.AsyncService.class.getMethod("fireAndForget");
        ctx.log("method has @Async: " + m.isAnnotationPresent(org.springframework.scheduling.annotation.Async.class));
        ctx.log("Takeaway: self-invocation bypasses async proxy—same lesson as transactions.");
    }

    private static void lesson46(StudyContext ctx) throws Exception {
        ctx.log("Story: fixedRate measures from start to start; fixedDelay waits after completion; cron is calendar-driven.");
        Method m1 = AsyncSchedulingCacheRetryFixtures.SchedulingService.class.getMethod("rate");
        Method m2 = AsyncSchedulingCacheRetryFixtures.SchedulingService.class.getMethod("delay");
        Method m3 = AsyncSchedulingCacheRetryFixtures.SchedulingService.class.getMethod("cron");
        var s1 = m1.getAnnotation(org.springframework.scheduling.annotation.Scheduled.class);
        var s2 = m2.getAnnotation(org.springframework.scheduling.annotation.Scheduled.class);
        var s3 = m3.getAnnotation(org.springframework.scheduling.annotation.Scheduled.class);
        ctx.log("fixedRate value: " + s1.fixedRate());
        ctx.log("fixedDelay value: " + s2.fixedDelay());
        ctx.log("cron value: " + s3.cron());
        ctx.log("Takeaway: misconfigured schedules overload pools—always size the executor.");
    }

    private static void lesson47(StudyContext ctx) throws Exception {
        ctx.log("Story: cache annotations map CRUD semantics onto a single backing store.");
        Method get = AsyncSchedulingCacheRetryFixtures.CacheService.class.getMethod("getItem", String.class);
        Method put = AsyncSchedulingCacheRetryFixtures.CacheService.class.getMethod("putItem", String.class);
        Method evict = AsyncSchedulingCacheRetryFixtures.CacheService.class.getMethod("evictItem", String.class);
        ctx.log("has @Cacheable: " + get.isAnnotationPresent(org.springframework.cache.annotation.Cacheable.class));
        ctx.log("has @CachePut: " + put.isAnnotationPresent(org.springframework.cache.annotation.CachePut.class));
        ctx.log("has @CacheEvict: " + evict.isAnnotationPresent(org.springframework.cache.annotation.CacheEvict.class));
        ctx.log("Takeaway: @CachePut still executes the method—use when writes must refresh cache entries.");
    }

    private static void lesson48(StudyContext ctx) {
        ctx.log("Story: @Order sorts competing advices/filters when multiple beans implement the same extension point.");
        ctx.log("FastFilter order: "
                + AsyncSchedulingCacheRetryFixtures.FastFilter.class.getAnnotation(
                org.springframework.core.annotation.Order.class).value());
        ctx.log("SlowFilter order: "
                + AsyncSchedulingCacheRetryFixtures.SlowFilter.class.getAnnotation(
                org.springframework.core.annotation.Order.class).value());
        ctx.log("Takeaway: lower numeric values run earlier in many Spring orderings.");
    }

    private static void lesson49(StudyContext ctx) throws Exception {
        ctx.log("Story: @EventListener runs in the caller thread by default; @TransactionalEventListener respects phases.");
        Method regular = AsyncSchedulingCacheRetryFixtures.EventHandlers.class.getMethod("onAny", String.class);
        Method tx = AsyncSchedulingCacheRetryFixtures.EventHandlers.class.getMethod("afterCommit", String.class);
        ctx.log("regular has @EventListener: " + regular.isAnnotationPresent(
                org.springframework.context.event.EventListener.class));
        ctx.log("tx has @TransactionalEventListener: " + tx.isAnnotationPresent(
                org.springframework.transaction.event.TransactionalEventListener.class));
        ctx.log("Takeaway: AFTER_COMMIT is how you publish integration events that must see committed data.");
    }

    private static void lesson50(StudyContext ctx) throws Exception {
        ctx.log("Story: Spring Retry wraps methods with interceptor-driven replays; metadata encodes backoff policy.");
        Method m = AsyncSchedulingCacheRetryFixtures.RetryService.class.getMethod("callRemote");
        Retryable retryable = m.getAnnotation(Retryable.class);
        ctx.log("maxAttempts from @Retryable: " + retryable.maxAttempts());
        ctx.log("manual retry would require loop + backoff + terminal failure rules.");
        ctx.log("Takeaway: declarative retry is concise but must be combined with idempotency checks.");
    }
}
