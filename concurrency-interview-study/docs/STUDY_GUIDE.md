# Concurrency interview study guide

This module is a **hands-on** tour of Java concurrency (`java.util.concurrent`, locks, atomics, virtual-thread–aware patterns where relevant) plus a small amount of **Spring scheduling** and **Kafka** at the end. Use this document **before** you run a lesson to know *what* to watch for, and **after** to rehearse how you would explain it in an interview (behavior, guarantees, and failure modes—not just API names).

## How to run

From the repo root:

- List titles: `mvn -pl concurrency-interview-study exec:java -Dexec.args="list"`
- One lesson: `… -Dexec.args="run 17"`
- All lessons: `… -Dexec.args="run-all"` (continues on failure; optional `--errors-log` for stack traces—see root `guide.md`)

Implementation lives mainly in `com.example.concurrency.interview.lesson.ConcurrencyLesson` (enum: number, title, `run`) and `com.example.concurrency.interview.demos.*`. Optional quick notes: `concurrency-study.messages.txt` at repo root if present.

---

## Part 1 — Threads, results, and cancellation (lessons 1–3)

### Lesson 1: Thread lifecycle — start a worker `Thread` with `Runnable`

You learn the baseline model: create a `Thread` with a `Runnable`, call `start()` (not `run()` on the current thread). Interviews often probe whether you know that **starting** schedules work asynchronously and that **joining** observes completion. Watch the console output for ordering: without synchronization, interleaving is normal.

### Lesson 2: `Callable` + `Future` — typed result from a background task

`Callable<V>` returns a value and can throw checked exceptions; `ExecutorService.submit` gives a `Future<V>`. You learn **`get()`** as the blocking completion boundary and the difference from a fire-and-forget `Runnable`.

### Lesson 3: `Future.cancel(true)` and task interruption

You learn that cancellation is **cooperative**: `cancel(true)` sets interrupt status; the task must **poll** `interrupted` or block in interruptible methods. Interview angle: why `cancel` does not always stop work immediately, and how to design long tasks to respect interruption.

---

## Part 2 — `ExecutorService` patterns (lessons 4–12)

### Lesson 4: `newFixedThreadPool` and task throughput

Fixed pool = bounded parallelism and bounded thread count. You learn to relate **pool size** to **work queue** behavior and why unbounded submission to a fixed pool can grow the queue without extra threads.

### Lesson 5: `newCachedThreadPool` for bursty short-lived work

Cached pool creates threads on demand and reuses idle ones. Good for **many short tasks**; risky if tasks are **long-lived** (thread explosion). Contrast with fixed pool in interviews.

### Lesson 6: `newSingleThreadExecutor` — serial execution order

One worker thread: **FIFO**-style ordering of submitted tasks (for the single worker), useful for **non-thread-safe** sequential logic off the caller thread. Not a substitute for locking across multiple threads.

### Lesson 7: `ScheduledExecutorService` — one-shot delayed task

You learn **`schedule(Runnable, delay, unit)`** for delayed execution. Foundation for lesson 8 and later scheduling topics.

### Lesson 8: `scheduleAtFixedRate` — recurring tasks (overlap caveat)

Fixed-rate scheduling can **overlap** if a task runs longer than the period. Interview talking point: **fixed rate vs fixed delay** (`scheduleWithFixedDelay`) and operational implications.

### Lesson 9: `ForkJoinPool` / parallel streams share the common pool

Parallel streams use the **common** `ForkJoinPool`. You learn that CPU-bound work on the common pool can **starve** other library code using the same pool—why dedicated executors matter.

### Lesson 10: Custom `ThreadFactory` — name threads for observability

Named threads show up in logs and thread dumps. You learn to attach **meaningful names** (pool id, task type) for production debugging.

### Lesson 11: `invokeAll` — barrier after a batch of `Callable`s

Submit many tasks and wait for **all** to complete (with timeouts in real code). Structured batch pattern before `CompletableFuture.allOf`.

### Lesson 12: `invokeAny` — first successful result wins

Returns the **first successful** result and cancels the others (behavior depends on executor). Interview: when you want **redundant** providers or **fastest-wins** races.

---

## Part 3 — Locks: `synchronized` and `j.u.c.locks` (lessons 13–18)

### Lesson 13: `synchronized` method — mutual exclusion on monitor

Instance `synchronized` methods use **`this`** as the lock; static methods use the **class** object. You learn **reentrancy** (same thread can re-enter) and that locks are **not** fair by default.

### Lesson 14: `synchronized` block — minimize critical section scope

Smaller critical sections reduce contention and deadlock risk. You learn to protect **only shared mutable state**, not I/O or heavy work, inside `synchronized`.

### Lesson 15: `ReentrantLock` — explicit `lock` / `unlock` with `try` / `finally`

Unlike `synchronized`, you **must** unlock in `finally`. You learn **tryLock**, **interruptible** lock acquisition, and **Condition** as a follow-on concept (often asked in senior interviews).

### Lesson 16: `ReentrantLock.tryLock(timeout)` — bounded waits

Avoids indefinite blocking. Interview: **livelock** vs **deadlock**, and using timeouts as a **diagnostic** or **degradation** strategy.

### Lesson 17: `ReadWriteLock` — concurrent reads, exclusive writes

Many readers **or** one writer. Good for **read-heavy** maps or caches. Know that **write** lock is exclusive and that **upgrade** patterns need care (often use `StampedLock` or careful ordering).

### Lesson 18: `StampedLock` optimistic read pattern (validate stamp)

Optimistic read → work → **`validate(stamp)`**; on failure, fall back to read lock. You learn why **false negatives** happen and that `StampedLock` is **not** reentrant in the same way as `ReentrantLock`.

---

## Part 4 — Atomics and visibility (lessons 19–22)

### Lesson 19: `AtomicInteger` — lock-free increments

CAS-based updates: no `synchronized` for simple counters. Interview: **ABA** is usually about references; for `AtomicInteger` focus on **lost update** vs atomic **compareAndSet** mental model.

### Lesson 20: `LongAdder` — high-contention aggregation

Under contention, `LongAdder` spreads counts across cells and sums on `sum()`. Better than `AtomicLong` for **many writers**; slightly lazier / less immediate consistency for reads—know the tradeoff.

### Lesson 21: Visibility — why volatile / happens-before matters

Without a happens-before edge, one thread may **never see** another thread’s writes. You connect **Java Memory Model** vocabulary: visibility vs atomicity.

### Lesson 22: `volatile` stop flag — publish safe visibility

A **volatile** boolean (or other safe publication) lets a worker observe **shutdown**. Interview: `volatile` gives visibility, **not** compound actions—still need locks/atomics for `check-then-act`.

---

## Part 5 — Data races and fixing them (lessons 23–26)

### Lesson 23: Race — lost updates on plain `int++`

Demonstrates **read-modify-write** non-atomicity across threads. Classic interview setup: “two threads increment—why count wrong?”

### Lesson 24: Fix race with `synchronized`

Serialize access to the shared counter. Tradeoff: **contention** and potential **scalability** limits.

### Lesson 25: Fix race with `AtomicInteger`

Lock-free counter path. Compare when you’d still prefer **`synchronized`** (invariants spanning multiple fields).

### Lesson 26: Check-then-act race (interview pattern)

**Test** a condition and **act** without atomicity—another thread can invalidate between check and act. You learn to fix with **synchronized**, **atomic compare-and-set**, or **immutable** designs.

---

## Part 6 — Batch work and throughput (lessons 27–31)

### Lesson 27: Batch — many records through a fixed thread pool

Patterns for slicing work across workers and aggregating results. Relates pool size to **latency** and **throughput**.

### Lesson 28: Chunking tasks to cut submission overhead

Fewer, larger tasks can reduce scheduling overhead vs millions of tiny submissions. Interview: **task granularity** vs **parallelism**.

### Lesson 29: `invokeAll` for structured batch completion

Waits for the **whole batch**—good when all pieces must finish before the next phase.

### Lesson 30: Throughput experiment — pool size vs CPU-bound work

Empirical feel for **too few** vs **too many** threads on CPU-bound work (often near **core count**). Reinforces Amdahl and context-switch cost verbally.

### Lesson 31: Batch + `CompletableFuture.allOf` bridge

Combines **executor batching** with **async composition**. Bridge to the next part.

---

## Part 7 — Producer–consumer queues (lessons 32–35, 61)

### Lesson 32: `ArrayBlockingQueue` (bounded)

Backpressure naturally: producers **block** when full. Interview: bounded queue + fixed pool = **stable** systems under load.

### Lesson 33: `LinkedBlockingQueue` producer–consumer

Often used with thread pools; understand **optional capacity** vs unbounded risk (later lessons revisit).

### Lesson 34: Poison pill — graceful consumer shutdown

Send a **sentinel** message so consumers exit cleanly. Pattern for **draining** work before shutdown.

### Lesson 35: Multiple consumers on one queue

Competing consumers **share** load; relates to **work stealing** mentally and to Kafka consumer groups later at a high level.

### Lesson 61: Producer–consumer **problem** — `wait` / `notifyAll` **and** `BlockingQueue`

**Theory.** The classical **producer–consumer problem** coordinates two (or more) threads through a **bounded buffer**: the producer must not overwrite unread slots, and the consumer must not read an empty buffer. On the JVM this is usually expressed with a **monitor** (`synchronized` on a lock object): while the buffer is **full**, the producer calls **`wait()`** to release the monitor and sleep until space exists; while **empty**, the consumer waits. After each **`put`** or **`take`**, call **`notifyAll()`** so every waiter re-checks the condition (mandatory pattern when multiple producers/consumers exist—`notify()` can wake the wrong thread).

**Same condition via `BlockingQueue`.** `BlockingQueue.put` blocks when the queue is **full**; `take` blocks when it is **empty**. `ArrayBlockingQueue` (bounded) applies the **same logical condition** as a hand-rolled ring buffer, using `ReentrantLock` + `Condition` internally instead of `Object.wait`/`notifyAll`. Lesson 61 runs **both** with the same capacity and item count so you can compare logs.

**Relation to lessons 32–35.** Those lessons already used `BlockingQueue`; lesson 61 ties them back to **explicit** monitor code. **Always wait in a loop** (`while` condition), never `if`, because **spurious wakeups** and **batched** notifications mean the condition may still be false after wake.

**What to take away.** In production you normally use **`BlockingQueue`** or a reactive pipeline; you still explain the **monitor + wait/notify** version to show you understand **why** blocking queues behave as they do and how **lost signals** happen if you forget `notify` or use `if` instead of `while`.

---

## Part 8 — `CompletableFuture` and structured concurrency (lessons 36–42, 62)

### Lesson 36: `CompletableFuture.supplyAsync` and default executor

Default async executor is **`ForkJoinPool.commonPool()`**—know the implication for blocking tasks.

### Lesson 37: `CompletableFuture` with a dedicated executor

**Always** use a custom executor for **blocking** or **mixed** workloads to avoid starving common pool.

### Lesson 38: `thenCompose` — async chaining without nesting

Flattens **`CompletionStage<CompletionStage<T>>`** into one pipeline. Core for **dependent** async steps.

### Lesson 39: `thenCombine` — merge two independent async results

**Parallel** branches joined when both complete. Contrast with `thenCompose` (sequential dependency).

### Lesson 40: `allOf` vs `anyOf` coordination

**All** complete vs **first** complete. Interview: error handling—`allOf` does not fail-fast on first exception unless you add logic.

### Lesson 41: `exceptionally` / `handle` — recover from failed stages

You learn **pipeline-level** recovery vs letting exceptions propagate. `handle` sees both success and failure.

### Lesson 42: `get()` vs `join()` exception shapes

`get()` wraps in **`ExecutionException`**/`InterruptedException`; `join()` throws **`CompletionException`**. Know what your API consumers must catch.

### Lesson 62: Structured concurrency — `StructuredTaskScope.ShutdownOnFailure` (JDK 21+)

**Theory.** **Structured concurrency** means subtasks have a **parent scope**: they are started **for** a bounded piece of work and must finish (or be **cancelled**) when that work ends or fails. That avoids **orphan threads**—a classic problem with raw `new Thread` or fire-and-forget `CompletableFuture.runAsync` where a failure in one branch leaves unrelated work running.

**API.** `StructuredTaskScope` (JDK 21) lets you **`fork(Callable)`** subtasks, **`join()`** until the scope’s policy is satisfied, then **`throwIfFailed()`** to surface the first failure. **`ShutdownOnFailure`** means: if **any** fork completes exceptionally, the scope **shuts down** and **cancels** the others (typically **interrupt**), so you do not keep calling a dead downstream after one leg failed.

**Contrast with `CompletableFuture`.** `allOf` composes stages but does not by itself define a **single cancellation tree**; you often add manual `exceptionally` / `cancel chains`. Structured scopes make **“all succeed or we unwind together”** the default story—closer to **try/finally** for threads.

**What to take away.** Interviews: name **structured concurrency**, **failure propagation**, and **peer cancellation**. In production, also know **Project Loom** / **virtual threads** pair well with scopes (many blocked forks are cheap). Requires **JDK 21+** (this module targets Java 21).

---

## Part 9 — Thread errors and executor shutdown (lessons 43–46)

### Lesson 43: `UncaughtExceptionHandler` on threads

Captures **crashes** on threads without try/catch. Operational importance for **logging** and **alerting**.

### Lesson 44: `ExecutorService.shutdown` + `awaitTermination`

**Graceful** shutdown: stop accepting new work, finish queued tasks (for shutdown), wait with timeout. Production shutdown sequence.

### Lesson 45: `shutdownNow` and cancelling queued work

**Best-effort** interrupt of workers and **drain** of queued `Runnable`s. Interview: difference from **graceful** shutdown and why tasks must be interruptible.

### Lesson 46: Spring-managed `ExecutorService` `destroyMethod` shutdown

Wiring **lifecycle**: Spring destroys the bean → executor shuts down. Relates lesson 44 to **application context** teardown.

---

## Part 10 — Saturation, backpressure, and handoff (lessons 47–50)

### Lesson 47: `AbortPolicy` when the queue is saturated

**Reject** new tasks with `RejectedExecutionException`. Caller must handle failure—often **drop** or **retry** with policy.

### Lesson 48: `CallerRunsPolicy` as backpressure

Submitter thread runs the task—**slows producers** naturally. Interview: pros (self-throttling) and cons (risk blocking **unexpected** threads).

### Lesson 49: `Semaphore` limiting concurrent in-flight work

Caps **parallelism** independent of pool size, or guards a **resource** (DB, socket). Classic **throttling** primitive.

### Lesson 50: `SynchronousQueue` direct handoff

No capacity: producer waits for consumer. Used in **`Executors.newCachedThreadPool`**-style handoff. Understand **zero buffering**.

---

## Part 11 — Pool tuning and interview recap (lessons 51–54)

### Lesson 51: `corePoolSize` vs `maximumPoolSize`

How a `ThreadPoolExecutor` **ramps** threads and when the queue is used vs new threads. Core interview material for **`ThreadPoolExecutor`** internals.

### Lesson 52: Unbounded `LinkedBlockingQueue` risk

**Never grows** the pool beyond core if queue is unbounded—can hide **overload** until OOM. Critical production pitfall.

### Lesson 53: `RejectedExecutionHandler` strategies (interview)

Name **Abort**, **CallerRuns**, **Discard**, **DiscardOldest** and when each is appropriate or dangerous.

### Lesson 54: Pool tuning recap for interviews

Consolidates vocabulary: **queue choice**, **pool bounds**, **rejection**, **monitoring** (queue depth, active count).

---

## Part 12 — Scheduling: plain JDK and Spring (lessons 55–57)

### Lesson 55: `ScheduledExecutorService` without Spring

Standalone periodic/ delayed tasks outside a container. Compare with Spring’s lifecycle in the next lessons.

### Lesson 56: Spring `@Scheduled` with extra configuration

Declarative scheduling in a Spring context. You learn **fixedRate** / **fixedDelay** semantics and that **multiple threads** depend on scheduler configuration.

### Lesson 57: `ThreadPoolTaskScheduler` programmatic ticks

Programmatic control of a **Spring** scheduler bean—useful for tests and dynamic schedules.

---

## Part 13 — Parallelism and streams (lesson 58)

### Lesson 58: Parallel chunks + `parallelStream` / common pool cautions

Splits work with **parallel streams** or chunking; reinforces **lesson 9**: common pool **contention** and **blocking** inside parallel pipelines. Interview: “when not to use `parallelStream`.”

---

## Part 14 — Kafka and capstone (lessons 59–60)

### Lesson 59: Kafka async producer `send(callback)` and consumer drain

**Asynchronous** produce with callbacks; consumer **poll** loop basics. Messaging angle: **at-least-once** mindset, **offsets**, and **backpressure** via `poll` batching (high level).

### Lesson 60: Kafka consumer group concurrency; batch vs per-record; capstone recap

Ties **multiple consumers** to **partitions**, contrasts **batch** vs **per-record** processing, and serves as a **verbal recap** linking queues, pools, and async patterns from earlier lessons.

---

## How to review for interviews

After each run, close the loop in three sentences:

1. **What** did the demo prove (one concrete behavior)?
2. **What breaks** if you misuse the primitive (wrong pool, unbounded queue, blocking in common pool, ignoring interrupt)?
3. **Where** would you use this in a real service (and what would you monitor)?

Keep this guide as the **single narrative index** for the module; prefer extending these sections over scattering new markdown files per lesson.
