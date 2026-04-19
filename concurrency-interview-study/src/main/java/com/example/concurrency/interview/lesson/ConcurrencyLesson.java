package com.example.concurrency.interview.lesson;

import com.example.concurrency.interview.demos.DemoAtomic;
import com.example.concurrency.interview.demos.DemoBackpressure;
import com.example.concurrency.interview.demos.DemoBatch;
import com.example.concurrency.interview.demos.DemoCf;
import com.example.concurrency.interview.demos.DemoChunks;
import com.example.concurrency.interview.demos.DemoExecutors;
import com.example.concurrency.interview.demos.DemoKafka;
import com.example.concurrency.interview.demos.DemoPc;
import com.example.concurrency.interview.demos.DemoRace;
import com.example.concurrency.interview.demos.DemoScheduled;
import com.example.concurrency.interview.demos.DemoShutdown;
import com.example.concurrency.interview.demos.DemoStructuredConcurrency;
import com.example.concurrency.interview.demos.DemoSync;
import com.example.concurrency.interview.demos.DemoThreads;
import com.example.concurrency.interview.demos.DemoTuning;
import com.example.concurrency.interview.study.StudyContext;
import com.example.concurrency.interview.study.StudyLesson;

/** Sixty-two interview lessons; runnable from {@link com.example.concurrency.interview.cli.ConcurrencyStudyCli}. */
public enum ConcurrencyLesson implements StudyLesson {

    L01(1, "Thread lifecycle: start a worker Thread with Runnable."),
    L02(2, "Callable + Future: typed result from a background task."),
    L03(3, "Future.cancel(true) and task interruption."),
    L04(4, "ExecutorService: newFixedThreadPool and task throughput."),
    L05(5, "newCachedThreadPool for bursty short-lived work."),
    L06(6, "newSingleThreadExecutor: serial execution order."),
    L07(7, "ScheduledExecutorService: schedule one-shot delayed task."),
    L08(8, "scheduleAtFixedRate: recurring tasks (overlap caveat)."),
    L09(9, "ForkJoinPool / parallel streams share the common pool."),
    L10(10, "Custom ThreadFactory: name threads for observability."),
    L11(11, "invokeAll: barrier after a batch of Callables."),
    L12(12, "invokeAny: first successful result wins."),
    L13(13, "synchronized method: mutual exclusion on class monitor."),
    L14(14, "synchronized block: minimize critical section scope."),
    L15(15, "ReentrantLock: explicit lock/unlock with try/finally."),
    L16(16, "ReentrantLock.tryLock(timeout): bounded waits."),
    L17(17, "ReadWriteLock: concurrent reads, exclusive writes."),
    L18(18, "StampedLock optimistic read pattern (validate stamp)."),
    L19(19, "AtomicInteger: lock-free increments."),
    L20(20, "LongAdder: high-contention aggregation."),
    L21(21, "Visibility: why volatile / happens-before matters."),
    L22(22, "volatile stop flag: publish safe visibility."),
    L23(23, "Race: lost updates on plain int++."),
    L24(24, "Fix race with synchronized."),
    L25(25, "Fix race with AtomicInteger."),
    L26(26, "Check-then-act race (interview pattern)."),
    L27(27, "Batch: 1200 records through a fixed thread pool."),
    L28(28, "Chunking tasks to cut submission overhead."),
    L29(29, "invokeAll for structured batch completion."),
    L30(30, "Throughput experiment: pool size vs CPU-bound work."),
    L31(31, "Batch + CompletableFuture.allOf bridge."),
    L32(32, "Producer–consumer: ArrayBlockingQueue (bounded)."),
    L33(33, "LinkedBlockingQueue producer–consumer."),
    L34(34, "Poison pill graceful consumer shutdown."),
    L35(35, "Multiple consumers on one queue."),
    L36(36, "CompletableFuture.supplyAsync and default executor."),
    L37(37, "CompletableFuture with a dedicated executor."),
    L38(38, "thenCompose: async chaining without nesting."),
    L39(39, "thenCombine: merge two independent async results."),
    L40(40, "allOf vs anyOf coordination."),
    L41(41, "exceptionally / handle: recover from failed stages."),
    L42(42, "get() vs join() exception shapes."),
    L43(43, "UncaughtExceptionHandler on threads."),
    L44(44, "ExecutorService.shutdown + awaitTermination."),
    L45(45, "shutdownNow and cancelling queued work."),
    L46(46, "Spring-managed ExecutorService destroyMethod shutdown."),
    L47(47, "AbortPolicy when the queue is saturated."),
    L48(48, "CallerRunsPolicy as backpressure."),
    L49(49, "Semaphore limiting concurrent in-flight work."),
    L50(50, "SynchronousQueue direct handoff."),
    L51(51, "Thread pool tuning: corePoolSize vs maximumPoolSize."),
    L52(52, "Unbounded LinkedBlockingQueue risk."),
    L53(53, "RejectedExecutionHandler strategies (interview)."),
    L54(54, "Pool tuning recap for interviews."),
    L55(55, "ScheduledExecutorService without Spring."),
    L56(56, "Spring @Scheduled with extra configuration."),
    L57(57, "ThreadPoolTaskScheduler programmatic ticks."),
    L58(58, "Parallel chunks + parallelStream / common pool cautions."),
    L59(59, "Kafka async producer send(callback) and consumer drain."),
    L60(60, "Kafka consumer group concurrency; batch vs per-record; capstone recap."),
    L61(61, "Producer–consumer problem: wait/notify buffer vs BlockingQueue (same bounded condition)."),
    L62(62, "Structured concurrency: StructuredTaskScope.ShutdownOnFailure — fork, join, cancel peers on failure.");

    public static final int EXPECTED_LESSON_COUNT = 62;

    private final int number;
    private final String title;

    ConcurrencyLesson(int number, String title) {
        this.number = number;
        this.title = title;
    }

    @Override
    public int number() {
        return number;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public void run(StudyContext ctx) throws Exception {
        switch (this) {
            case L01 -> DemoThreads.l01(ctx);
            case L02 -> DemoThreads.l02(ctx);
            case L03 -> DemoThreads.l03(ctx);
            case L04 -> DemoExecutors.l04(ctx);
            case L05 -> DemoExecutors.l05(ctx);
            case L06 -> DemoExecutors.l06(ctx);
            case L07 -> DemoExecutors.l07(ctx);
            case L08 -> DemoExecutors.l08(ctx);
            case L09 -> DemoExecutors.l09(ctx);
            case L10 -> DemoExecutors.l10(ctx);
            case L11 -> DemoExecutors.l11(ctx);
            case L12 -> DemoExecutors.l12(ctx);
            case L13 -> DemoSync.l13(ctx);
            case L14 -> DemoSync.l14(ctx);
            case L15 -> DemoSync.l15(ctx);
            case L16 -> DemoSync.l16(ctx);
            case L17 -> DemoSync.l17(ctx);
            case L18 -> DemoSync.l18(ctx);
            case L19 -> DemoAtomic.l19(ctx);
            case L20 -> DemoAtomic.l20(ctx);
            case L21 -> DemoAtomic.l21(ctx);
            case L22 -> DemoAtomic.l22(ctx);
            case L23 -> DemoRace.l23(ctx);
            case L24 -> DemoRace.l24(ctx);
            case L25 -> DemoRace.l25(ctx);
            case L26 -> DemoRace.l26(ctx);
            case L27 -> DemoBatch.l27(ctx);
            case L28 -> DemoBatch.l28(ctx);
            case L29 -> DemoBatch.l29(ctx);
            case L30 -> DemoBatch.l30(ctx);
            case L31 -> DemoBatch.l31(ctx);
            case L32 -> DemoPc.l32(ctx);
            case L33 -> DemoPc.l33(ctx);
            case L34 -> DemoPc.l34(ctx);
            case L35 -> DemoPc.l35(ctx);
            case L36 -> DemoCf.l36(ctx);
            case L37 -> DemoCf.l37(ctx);
            case L38 -> DemoCf.l38(ctx);
            case L39 -> DemoCf.l39(ctx);
            case L40 -> DemoCf.l40(ctx);
            case L41 -> DemoCf.l41(ctx);
            case L42 -> DemoCf.l42(ctx);
            case L43 -> DemoShutdown.l43(ctx);
            case L44 -> DemoShutdown.l44(ctx);
            case L45 -> DemoShutdown.l45(ctx);
            case L46 -> DemoShutdown.l46(ctx);
            case L47 -> DemoBackpressure.l47(ctx);
            case L48 -> DemoBackpressure.l48(ctx);
            case L49 -> DemoBackpressure.l49(ctx);
            case L50 -> DemoBackpressure.l50(ctx);
            case L51 -> DemoTuning.l51(ctx);
            case L52 -> DemoTuning.l52(ctx);
            case L53 -> DemoTuning.l53(ctx);
            case L54 -> DemoTuning.l54(ctx);
            case L55 -> DemoScheduled.l55(ctx);
            case L56 -> DemoScheduled.l56(ctx);
            case L57 -> DemoScheduled.l57(ctx);
            case L58 -> DemoChunks.l58(ctx);
            case L59 -> DemoKafka.l59(ctx);
            case L60 -> DemoKafka.l60(ctx);
            case L61 -> DemoPc.l61(ctx);
            case L62 -> DemoStructuredConcurrency.l62(ctx);
        }
    }
}
