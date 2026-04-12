---
name: Concurrency Interview Module
overview: Add a new Maven submodule `concurrency-interview-study` that mirrors your existing interview CLI pattern (picocli, StudyLesson, LessonCatalog, list/run/run-all) while covering multithreading, CompletableFuture, graceful shutdown, backpressure, and Kafka (Embedded Kafka for local runs) across a deliberately expanded lesson count (~60) with a single configurable coverage assertion instead of a hardcoded 50.
todos:
  - id: add-maven-module
    content: Create concurrency-interview-study/pom.xml and register module in parent pom.xml
    status: completed
  - id: study-shell
    content: Add ConcurrencyStudyApplication, ConcurrencyBoot, StudyLesson, StudyContext, LessonCatalog with EXPECTED_LESSON_COUNT
    status: in_progress
  - id: cli
    content: Add ConcurrencyStudyCli (list/run/run-all) mirroring SpringDataStudyCli
    status: pending
  - id: demos-core
    content: "Implement enum-run demos: threads, executors, sync, atomics, volatile, races, shutdown, backpressure, scheduling, chunks"
    status: pending
  - id: demos-cf-batch
    content: Implement CompletableFuture and batch (1000+) lessons with clear ctx.log narratives
    status: pending
  - id: demos-kafka
    content: Add spring-kafka + Embedded Kafka helpers; producer async, consumer concurrency, batch vs record listeners
    status: pending
  - id: optional-test
    content: Add one @SpringBootTest + @EmbeddedKafka smoke test for a Kafka lesson
    status: pending
isProject: false
---

# Concurrency and multithreading interview study module

## Alignment with existing patterns

Your established shape (`[spring-data-interview-study](c:\Users\ahsan\IdeaProjects\untitled\spring-data-interview-study)`) is:

- **Parent** `[pom.xml](c:\Users\ahsan\IdeaProjects\untitled\pom.xml)`: Java 21, Spring Boot 3.4.2 BOM.
- `**StudyLesson`**: `number()`, `title()`, `run(StudyContext)`.
- `**StudyContext`**: simple `log(String)` → stdout.
- `**LessonCatalog`**: ordered list, `byNumber`, `**assertCoverage()`** (today Spring Data hardcodes 50).
- **CLI** (picocli): `list`, `run N`, `run-all`; `exec-maven-plugin` + `maven-jar-plugin` main class.

The new module should follow this **exact CLI and study API**, but **decouple “expected lesson count” from 50**: use a named constant (e.g. `EXPECTED_LESSON_COUNT = 60`) in `LessonCatalog` so this module can be larger without pretending to match JPA/Spring Data’s 50.

**Note:** `[SpringDataLesson](c:\Users\ahsan\IdeaProjects\untitled\spring-data-interview-study\src\main\java\com\example\springdata\interview\lesson\SpringDataLesson.java)` references `LessonRuntime`, which is **not present** in the workspace. For the concurrency module, avoid that gap: either a **single `LessonDispatcher` with a `switch (number)`** or **package-private demo classes** invoked from an enum’s `run` method—everything compilable and runnable without a missing bean.

## Lesson count: 50 vs “more”

- **Fitting everything into 50** is possible only by **combining** multiple concepts per lesson (e.g. one lesson logs three sub-demos). That stays consistent with other modules but is **denser** and weaker for “one topic = one run.”
- **Recommended:** **~60 lessons** (or 55–65) so each major bullet (batch jobs, producer–consumer, Kafka concurrency, backpressure) gets **its own numbered title** and `run-all` remains a reasonable study pass. Use `**assertCoverage()` against `EXPECTED_LESSON_COUNT`**, not the literal `50` in assertions or CLI copy.

If you strongly want **exactly 50** for symmetry, the plan collapses lessons by **merging** the mapping below (same code, fewer enum entries).

## Proposed module layout

- **Artifact:** `concurrency-interview-study` under the parent POM `[pom.xml](c:\Users\ahsan\IdeaProjects\untitled\pom.xml)` `<modules>`.
- **Base package:** `com.example.concurrency.interview`.
- **Entry:** `ConcurrencyStudyApplication` — minimal `@SpringBootApplication` (scan only this module). Most demos can run **without** starting full web stack (`WebApplicationType.NONE`), same idea as `[DataSdBoot](c:\Users\ahsan\IdeaProjects\untitled\spring-data-interview-study\src\main\java\com\example\springdata\interview\support\DataSdBoot.java)`.
- **Boot helper:** `ConcurrencyBoot.startStudy(...)` — sets `banner-mode=off`, `logging.level.root=warn`, optional Kafka bootstrap for Kafka lessons.
- **Packages:**
  - `study` — `StudyLesson`, `StudyContext` (same contracts as Spring Data module).
  - `lesson` — `ConcurrencyLesson` enum (titles + `run`), `LessonCatalog`.
  - `cli` — `ConcurrencyStudyCli` (clone structure from `[SpringDataStudyCli](c:\Users\ahsan\IdeaProjects\untitled\spring-data-interview-study\src\main\java\com\example\springdata\interview\cli\SpringDataStudyCli.java)`).
  - `demos/` — grouped implementation classes (batch, kafka, etc.) to keep enum methods thin.

## Dependencies (Maven)

- `spring-boot-starter` (core lifecycle, `@Scheduled` if using scheduling).
- Optional: `spring-boot-starter-aop` only if you want a tiny aspect demo (can skip to stay minimal).
- **Kafka:** `spring-kafka` + `spring-kafka-test` for `**EmbeddedKafkaBroker`** started **only inside Kafka lessons** (or a small `@Configuration` toggled by profile) so `run 1–20` stays fast without a broker.
- `picocli` (already in parent BOM management).
- No JPA/H2 unless you add a lesson that ties concurrency to DB (optional; not required for your list).

## Content mapping (~60 lessons, adjustable)

Group in the enum for readability; each line is one `StudyLesson` title + `run` implementation.


| Block     | Topics                                                              | Example lesson titles (representative)                                                                                                                                                      |
| --------- | ------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **1–6**   | Thread lifecycle, `Runnable` / `Callable`, `Future`, thread vs pool | New thread; executor `submit(Callable)`; `Future` cancel                                                                                                                                    |
| **7–12**  | `ExecutorService` types                                             | Fixed, cached, single-thread, scheduled, `ForkJoinPool`, custom `ThreadFactory`                                                                                                             |
| **13–18** | Synchronization                                                     | `synchronized` method/block, `ReentrantLock`, `tryLock` + timeout, `ReadWriteLock` (optional), `StampedLock` (optional)                                                                     |
| **19–22** | Atomics / visibility                                                | `AtomicInteger`/`LongAdder`, `volatile` flag, broken vs fixed visibility demo                                                                                                               |
| **23–26** | Race conditions                                                     | Unsafe counter; fix with sync/atomic; lost update story                                                                                                                                     |
| **27–32** | Batch processing                                                    | Process 1000+ items with fixed pool; chunking; `invokeAll`; timing log for throughput                                                                                                       |
| **33–36** | Producer–consumer                                                   | `ArrayBlockingQueue`, `LinkedBlockingQueue`, poison pill shutdown, multi-consumer                                                                                                           |
| **37–42** | `CompletableFuture`                                                 | `supplyAsync` + custom executor; `thenCompose`/`thenCombine`; `allOf`/`anyOf`; exception propagation (`handle`/`exceptionally`)                                                             |
| **43–46** | Exceptions / shutdown                                               | Uncaught handler; executor after shutdown; `shutdown`/`awaitTermination`; Spring bean `@PreDestroy` closing a pool                                                                          |
| **47–50** | Backpressure / queues                                               | Bounded queue + rejection policy; `Semaphore` limiting in-flight; drop vs block policy                                                                                                      |
| **51–54** | Tuning                                                              | Core vs max pool size, queue choice, `CallerRunsPolicy` as backpressure                                                                                                                     |
| **55–58** | Scheduled / background                                              | `ScheduledExecutorService`; Spring `@Scheduled` + fixed pool bean                                                                                                                           |
| **59–62** | Parallel I/O / chunks                                               | Split list of “file chunks” (in-memory strings) and merge results; parallel stream caveats (one lesson)                                                                                     |
| **63–66** | Kafka                                                               | Async producer `send` + callback; consumer with `concurrency` > 1; **batch listener** (`ContainerProperties` / batch factory) vs **record-by-record**; optional: idempotent processing note |
| **67–60** | Capstones                                                           | End-to-end: “ingest → queue → worker pool → aggregate” without Kafka; with Kafka (Embedded)                                                                                                 |


*(Adjust numbering to hit exactly `EXPECTED_LESSON_COUNT`; the table is a checklist, not final enum order.)*

## Kafka design (practical for CLI)

```mermaid
flowchart LR
  subgraph kafkaLessons [Kafka lessons only]
    EK[EmbeddedKafkaBroker start/stop]
    P[KafkaTemplate async send]
    C[@KafkaListener concurrency N]
    B[Batch listener variant]
  end
  EK --> P
  EK --> C
  EK --> B
```



- Start **Embedded Kafka** in `run()` for lessons **63+** (or dedicated `KafkaLessonSupport` try-with-resources) and **tear down** in `finally` for graceful shutdown demo alignment.
- Use a **dedicated topic name per lesson** (e.g. `study.l63.producer`) to avoid cross-lesson pollution when running `run-all`.
- **Multithreading with consumers:** configure `concurrency` on the listener container factory and log thread names / partition assignment behavior in `ctx.log`.

## Performance / interview talking points

- In batch and pool-tuning lessons, log **pool sizes, queue length, and elapsed time** so `run` output supports interview explanations (not micro-benchmarks; deterministic dummy work with `Thread.sleep` or CPU-light math).

## Testing (lightweight)

- **JUnit 5** + `@SpringBootTest` for **one** Kafka lesson smoke test with `@EmbeddedKafka` (optional but valuable CI signal).
- Other demos remain **CLI-driven** like sibling modules (no need for 60 test classes).

## Files to add / change

1. **New:** `[concurrency-interview-study/pom.xml](c:\Users\ahsan\IdeaProjects\untitled\concurrency-interview-study\pom.xml)` — dependencies above, `exec:java` main = `ConcurrencyStudyCli`.
2. **Edit:** `[pom.xml](c:\Users\ahsan\IdeaProjects\untitled\pom.xml)` — add `<module>concurrency-interview-study</module>`.
3. **New:** Application, `ConcurrencyBoot`, `StudyLesson`, `StudyContext`, `ConcurrencyLesson` enum, `LessonCatalog`, `ConcurrencyStudyCli`, and `demos/`** implementation classes.

## Execution examples (after implementation)

```bash
mvn -pl concurrency-interview-study exec:java -Dexec.args="list"
mvn -pl concurrency-interview-study exec:java -Dexec.args="run 27"
mvn -pl concurrency-interview-study exec:java -Dexec.args="run-all"
```

