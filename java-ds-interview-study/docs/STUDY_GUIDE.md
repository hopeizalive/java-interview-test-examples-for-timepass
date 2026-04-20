# Java data structures interview study guide

This module is **JDK-only**: runnable **comparison** lessons for `java.util`, `java.util.concurrent` (one blocking-queue lesson), and `java.util.Arrays`. Each lesson prints one **interview-style question**, then runs code with **Javadoc on every entry method** (purpose, role, demonstration) and **detailed comments** on non-trivial blocks. Open the cited **class and method** in the IDE—that is the primary study material.

## How to run

From the repo root:

- List titles: `mvn -pl java-ds-interview-study exec:java -Dexec.args="list"`
- One lesson: `mvn -pl java-ds-interview-study exec:java -Dexec.args="run 1"`
- All lessons: `mvn -pl java-ds-interview-study exec:java -Dexec.args="run-all"` (continues on failure; optional `--errors-log` / `-e` for stack traces—see root `guide.md`)

Catalog: `com.example.javads.interview.lesson.DsLesson` (enum). CLI: `com.example.javads.interview.cli.DsStudyCli`.

---

## Lessons 1–10 (lists, maps, sets, deques)

| # | Title (short) | Code |
|---|-----------------|------|
| 1 | ArrayList vs LinkedList | `DemoListsAndDeques.l01` |
| 2 | HashMap vs LinkedHashMap vs TreeMap | `DemoMapsCore.l02` |
| 3 | Hash / Linked / Tree set + equals/hashCode | `DemoSetsPriority.l03` |
| 4 | ArrayDeque vs LinkedList as Deque | `DemoListsAndDeques.l04` |
| 5 | PriorityQueue vs TreeSet | `DemoSetsPriority.l05` |
| 6 | ConcurrentHashMap vs synchronized HashMap | `DemoConcurrencyMaps.l06` |
| 7 | List.of vs unmodifiableList vs copy | `DemoImmutableLists.l07` |
| 8 | HashMap presizing | `DemoMapsCore.l08` |
| 9 | IdentityHashMap vs HashMap | `DemoMapsCore.l09` |
| 10 | EnumMap vs HashMap | `DemoMapsEnum.l10` |

---

## Lessons 11–20 (arrays, strings, iteration, concurrency collections)

| # | Title (short) | Code |
|---|-----------------|------|
| 11 | Arrays.binarySearch vs linear | `DemoArraysAlgorithms.l11` |
| 12 | Arrays.sort vs parallelSort | `DemoArraysAlgorithms.l12` |
| 13 | String += vs StringBuilder | `DemoArraysAlgorithms.l13` |
| 14 | Collections.binarySearch ArrayList vs LinkedList | `DemoSearchLists.l14` |
| 15 | Iterator.remove vs for-each remove | `DemoIterationRemoval.l15` |
| 16 | ArrayDeque stack vs Stack | `DemoStackLegacy.l16` |
| 17 | TreeSet ceiling vs scan sorted ArrayList | `DemoNavigableTree.l17` |
| 18 | CopyOnWriteArrayList vs synchronized list | `DemoCopyOnWriteCompare.l18` |
| 19 | ArrayBlockingQueue vs LinkedBlockingQueue | `DemoBlockingQueuesCompare.l19` |
| 20 | BitSet vs boolean[] | `DemoBitSetCompare.l20` |

Timing helpers: `DemoSupport` (nanosecond timing is **directional** only, not JMH).

---

## Conference / interview prep tracks

Use these when time is limited; expand to `run-all` on both modules when you can.

**Minimal (half-day style)** — run individual lessons in this order:

1. **Concurrency** (queues, pools, saturation): `32, 33, 34, 35, 47, 48, 49, 50, 51, 52, 53, 54`  
   (`mvn -pl concurrency-interview-study exec:java -Dexec.args="run N"`)

2. **Java DS (this module)**: `1, 2, 3, 4, 5, 6, 11, 12, 15, 16, 18, 19`

**Full DS pass** — all lessons in catalog order:

`mvn -pl java-ds-interview-study exec:java -Dexec.args="run-all"`

---

## How to review

After each run, rehearse in three sentences: (1) what behavior the numbers or logs proved, (2) which API rule or complexity argument backs it, (3) when you would pick each type in production and what you would monitor.
