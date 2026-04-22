package com.example.springannotation.interview.lesson.fixtures;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;

/**
 * Async, scheduling, cache, ordering, events, and retry types for lessons 45–50.
 */
public final class AsyncSchedulingCacheRetryFixtures {

    private AsyncSchedulingCacheRetryFixtures() {}

    @Configuration
    @org.springframework.scheduling.annotation.EnableAsync
    public static class AsyncConfig {}

    public static class AsyncService {
        @org.springframework.scheduling.annotation.Async
        public void fireAndForget() {}
    }

    @Configuration
    @org.springframework.scheduling.annotation.EnableScheduling
    public static class SchedulingConfig {}

    public static class SchedulingService {
        @org.springframework.scheduling.annotation.Scheduled(fixedRate = 1000)
        public void rate() {}

        @org.springframework.scheduling.annotation.Scheduled(fixedDelay = 1000)
        public void delay() {}

        @org.springframework.scheduling.annotation.Scheduled(cron = "*/5 * * * * *")
        public void cron() {}
    }

    @Configuration
    @org.springframework.cache.annotation.EnableCaching
    public static class CacheConfig {}

    public static class CacheService {
        @org.springframework.cache.annotation.Cacheable(cacheNames = "items")
        public String getItem(String id) {
            return id;
        }

        @org.springframework.cache.annotation.CachePut(cacheNames = "items", key = "#id")
        public String putItem(String id) {
            return id;
        }

        @org.springframework.cache.annotation.CacheEvict(cacheNames = "items", key = "#id")
        public void evictItem(String id) {}
    }

    @org.springframework.core.annotation.Order(1)
    public static class FastFilter {}

    @org.springframework.core.annotation.Order(100)
    public static class SlowFilter {}

    public static class EventHandlers {
        @org.springframework.context.event.EventListener
        public void onAny(String event) {}

        @org.springframework.transaction.event.TransactionalEventListener
        public void afterCommit(String event) {}
    }

    @Configuration
    @EnableRetry
    public static class RetryConfig {}

    public static class RetryService {
        @Retryable(maxAttempts = 3, include = RuntimeException.class)
        public void callRemote() {}
    }
}
