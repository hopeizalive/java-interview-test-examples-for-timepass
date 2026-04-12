# Spring Data interview study — concepts, code, and why it matters

This module keeps **only lessons that execute persistence or web code** (no pure “talking point only” CLI slots). Removed topics (multi-store overviews, BOM/CalVer narrative, R2DBC/Mongo/Redis prose-only, etc.) belong in interview prep books or external docs—not in this runnable repo.

## How to run

- List: `mvn -pl spring-data-interview-study exec:java -Dexec.args="list"`
- One lesson: `-Dexec.args="run 8"`
- All: `run-all` (see repo root `guide.md` for failure logging).

## Layout

| Area | Role |
|------|------|
| `lesson.SpringDataLesson` | Titles + `LESSON_COUNT` (34) |
| `support.DataSdBoot` | Fresh H2 per run |
| `sddata.LessonRuntime` | Dispatch |
| `sddata.config.SpringDataLessons01To17Config` | Lessons 1–17 |
| `sddata.config.SpringDataLessons18To33Config` | Lessons 18–29 |
| `sddata.config.SpringDataLessons34To50Config` | Lessons 30–34 (file name is legacy) |
| `sddata.lNN` | Types for lesson *NN* (package numbers are legacy; lesson **numbers** are 1–34) |

## Lessons 1–17

| # | Concept | Code / config |
|---|---------|----------------|
| 1 | JpaRepository vs `EntityManager` | `l01`, `Sd01DemoService` |
| 2 | Repository hierarchy | `l02`, `Sd02TagRepository` |
| 3 | JDK proxy / `SimpleJpaRepository` | `lesson03` |
| 4 | Derived queries | `l04` |
| 5 | JPQL `@Query` | `l05` |
| 6 | Native `Page` | `l06` |
| 7 | `Page` vs `Slice` | `l07` |
| 8 | MVC `Pageable` | `l08`, `Sd08ItemController` |
| 9 | `Sort.by` | `l09` |
| 10 | Query by Example | `l10` |
| 11 | Interface projection | `l11` |
| 12 | `select new` DTO | `l12` |
| 13 | `@Modifying` | `l13`, `Sd13InventoryService` |
| 14 | `@EntityGraph` vs lazy | `l14`, `Sd14BlogPostDemoService` |
| 15 | `LazyInitializationException` | `l15`, `Sd15LazyDemoService` |
| 16 | `@Transactional` on service | `l16`, `Sd16TransferService` |
| 17 | persist vs merge | `l17` + `TransactionTemplate` |

## Lessons 18–29

| # | Concept | Code |
|---|---------|------|
| 18 | `saveAll` / batching | `l18` |
| 19 | Specifications | `l19` |
| 20 | `@Version` | `l20` |
| 21 | `flush()` | `l21` |
| 22 | `readOnly` tx | `l22` |
| 23 | `getReferenceById` | `l23` |
| 24 | Cascade / orphanRemoval | `l24` |
| 25 | Auditing | `l25` |
| 26 | Custom repository | `l26` |
| 27 | Bulk JPQL | `l27` |
| 28 | Join fetch + duplicate rows | `l30` package, `Sd30AuthorRepository` |
| 29 | Spring Data JDBC | `l31`, `schema-spring-data-study.sql` |

## Lessons 30–34

| # | Concept | Code |
|---|---------|------|
| 30 | SpEL `@Query` | `l44`, `Sd44SnipRepository` |
| 31 | Domain events | `l45` |
| 32 | N+1 vs `@EntityGraph` | `l46`, `Sd46BrowseService` |
| 33 | Indexes | `l48`, `Sd48Sku` |
| 34 | Unique + `DataIntegrityViolationException` | `l49` |

## Optional: `@DataJpaTest` slice

Run `mvn -pl spring-data-interview-study test -Dtest=Lesson28DataJpaSliceTest` for a **test-only** JPA slice example (not a CLI lesson).

## Weekly drill

1. `run N` and read output.  
2. Open the table row’s types.  
3. Explain **failure modes** if you drop transactions, fetch graphs, or pagination rules.
