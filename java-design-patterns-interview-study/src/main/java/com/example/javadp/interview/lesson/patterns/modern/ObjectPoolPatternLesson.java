package com.example.javadp.interview.lesson.patterns.modern;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Object pool: reuse expensive objects (connections, parsers) under a cap instead of allocating per request.
 */
public final class ObjectPoolPatternLesson {

    private ObjectPoolPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "JDBC connection pools (HikariCP, Tomcat JDBC)",
                "Borrowed connections amortize TCP+auth setup; pools bound concurrency to protect the database.",
                "Most Java services never create raw connections per SQL statement.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Apache Commons Pool for parsers",
                "Expensive PDF or media parsers are pooled because construction dominates small workloads.",
                "Pool = reuse + lifecycle (validate on borrow/return).");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Thread pools (ExecutorService)",
                "Worker threads themselves are pooled heavyweight resources; tasks are lightweight runnables.",
                "Analogous pattern at the concurrency layer.");

        ctx.log("--- Java core tie-in ---");
        java.util.concurrent.ForkJoinPool common = java.util.concurrent.ForkJoinPool.commonPool();
        ctx.log("java.util.concurrent.ForkJoinPool.commonPool() reuses worker threads (JDK pool): parallelism="
                + common.getParallelism());

        PatternLessonHeader.print(
                ctx,
                "Object Pool",
                "Modern / performance",
                "Bound concurrency and reuse heavyweight instances; callers borrow and return instead of new().",
                "ConnectionPool hands out at most two Connection stubs; third borrow blocks until a return.");
        ConnectionPool pool = new ConnectionPool(2);
        Connection c1;
        Connection c2;
        try {
            c1 = pool.borrowConnection();
            c2 = pool.borrowConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ctx.log("Interrupted borrowing from pool");
            return;
        }
        ctx.log("Borrowed two connections");
        Thread t = new Thread(
                () -> {
                    try {
                        Connection c3 = pool.borrowConnection();
                        ctx.log("Late borrower got " + c3.id());
                        pool.returnConnection(c3);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
        t.start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ctx.log("Interrupted while demoing pool wait");
            return;
        }
        pool.returnConnection(c1);
        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        pool.returnConnection(c2);
        ctx.log("Pool settled; all returned.");
    }

    private record Connection(String id) {}

    private static final class ConnectionPool {
        private final ArrayBlockingQueue<Connection> free;

        ConnectionPool(int size) {
            this.free = new ArrayBlockingQueue<>(size);
            for (int i = 0; i < size; i++) {
                free.offer(new Connection("conn-" + i));
            }
        }

        Connection borrowConnection() throws InterruptedException {
            return free.take();
        }

        void returnConnection(Connection c) {
            free.offer(c);
        }
    }
}
