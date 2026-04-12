# Spring Data JPA — study guide (theory + code map)

This module is **runnable**: every lesson starts a small Spring Boot context, touches real H2 persistence (or JDBC where noted), and prints observable outcomes. Use this guide to **read first** (concepts and tradeoffs), then **run** the lesson and connect output to the ideas below.

**Scope.** These lessons focus on **Spring Data JPA** (repositories on top of Hibernate/JPA), with one **Spring Data JDBC** lesson and topics that show up constantly in backend interviews: transactions, fetching, pagination, locking, and consistency with the database.

---

## Big picture before lesson 1

**JPA** gives you a **unit of work** (`EntityManager`, persistence context): entities attached to a session are tracked; changes flush to SQL at commit or flush boundaries. **Lazy** associations load on access **only** while the persistence context is open—hence the famous `LazyInitializationException` outside a transaction.

**Spring Data JPA** generates **repository implementations** at runtime (JDK proxies delegating to `SimpleJpaRepository` and friends). You declare interfaces; Spring wires `EntityManager`, query parsing, and pagination. You still need to understand **when SQL runs**, **what** SQL runs (N+1, count queries), and **transaction** boundaries—Spring Data does not remove that responsibility.

**Spring Data JDBC** is a different model: **no** lazy navigation, **no** persistence context in the JPA sense—aggregates and SQL are **explicit**. Good for simple domains and when you want predictable queries; less convenient for rich graphs.

---

## How to run

From the repo root:

- List lessons: `mvn -pl spring-data-interview-study exec:java -Dexec.args="list"`
- One lesson: `… -Dexec.args="run 8"` (example)
- All: `… -Dexec.args="run-all"` (failure logging: see root `guide.md`)

**Where things live (quick map).**

- `lesson.SpringDataLesson` — lesson **number**, **title**, and bootstrap (`DataSdBoot` + `LessonRuntime`).
- `support.DataSdBoot` — fresh H2 datasource name per lesson (`sd01`, `sd02`, …).
- `sddata.LessonRuntime` — dispatches to the lesson package.
- `sddata.config.SpringDataLessons01To17Config` / `18To33Config` / `34To50Config` — bean wiring (some config class names are legacy; **lesson numbers are 1–34**).
- `sddata.lNN` — types for lesson *NN* (package naming may not equal lesson number for older splits—follow `LessonRuntime` if confused).

---

## Part A — Repositories and how queries are built (lessons 1–7)

### Lesson 1: `JpaRepository` vs `EntityManager` — less ceremony for CRUD

**Theory.** `EntityManager` is the JPA API: `persist`, `merge`, `find`, queries. It is powerful but verbose for typical “load by id, list, save, delete.” `JpaRepository` exposes that vocabulary as **interface methods** (`findAll`, `save`, `deleteById`, …) so application code stays declarative. Under the hood the repository still uses an `EntityManager` (or `EntityManager` operations wrapped by `SimpleJpaRepository`).

**What to take away.** You are not choosing between “JPA or Spring Data”—Spring Data **is** how most teams access JPA. Interviews: know that **complex workflows** still use `EntityManager` or `@Query` when repositories are not enough.

### Lesson 2: `JpaRepository` layers `CrudRepository` + `PagingAndSortingRepository` + JPA helpers

**Theory.** Spring Data types are layered: `Repository` marker → `CrudRepository` (CRUD) → `PagingAndSortingRepository` (`Pageable`, `Sort`) → `JpaRepository` (batch `saveAll`, `flush`, `deleteAllInBatch`, etc.). Each layer adds **capabilities** without forcing you to implement them.

**What to take away.** Method resolution and return types (`Page`, `Slice`, `List`) depend on which super-interface you extend. Know the **minimal** interface you need for a given port/adapter.

### Lesson 3: Repository proxies — JDK proxy → `SimpleJpaRepository`

**Theory.** Spring registers your interface as a bean backed by a **JDK dynamic proxy** (or similar) that routes calls to `SimpleJpaRepository` (or a custom fragment). That is why you never write `class MyRepoImpl extends …` for basic CRUD—generation + infrastructure do it.

**What to take away.** “Magic” has a **single implementation class** per repository; custom behavior attaches via **`CustomRepo` + `CustomRepoImpl`** (lesson 26). Debugging: think **proxy → target → EntityManager**.

### Lesson 4: Query derivation from method names — and when names become unreadable

**Theory.** Spring parses `findBy…`, `And`, `Or`, `OrderBy`, `LessThan`, etc., and builds a JPA query. It is fast for simple filters; it **degrades** when method names encode half your domain (“findBy…And…Or…OrderBy…”).

**What to take away.** Interviews: **when to stop** deriving and switch to `@Query`, Specifications, or Criteria API. Readability and **static analysis** matter in large codebases.

### Lesson 5: `@Query` (JPQL) when derivation is unclear or you need explicit JPQL

**Theory.** **JPQL** is object-oriented: you query **entities and attributes**, not table names (unless native). Named parameters (`:name`) bind safely. Explicit JPQL documents intent and avoids parser ambiguity.

**What to take away.** Prefer JPQL for **non-trivial** joins and when the team needs **grep-friendly** query text. Native SQL (lesson 6) trades portability for control.

### Lesson 6: Native SQL — power vs dialect, pagination, portability

**Theory.** `@Query(nativeQuery = true)` runs **database SQL**. Pagination may still be adapted by Spring/Hibernate, but SQL is **dialect-specific** (functions, limits). Great for reports, vendor features, or tuning; poor for **portable** apps.

**What to take away.** Interview: “We use native when we need **exact** SQL or planner hints; we accept **migration cost** when changing databases.”

### Lesson 7: `Page<T>` runs a count query; `Slice<T>` only checks if a next page exists

**Theory.** A **page** answers “how many total?” → needs **`COUNT(*)`** (or an approximation strategy). A **slice** only needs **one extra row** beyond the limit to know if there is a **next** page—cheaper on huge tables when you do not need total pages.

**What to take away.** UI with “Page 3 of 100” → `Page`. Infinite scroll “load more” → often `Slice` or keyset pagination (not always in this module).

---

## Part B — Web pagination, sort, examples, projections (lessons 8–12)

### Lesson 8: Spring MVC binds `Pageable` from query params (`page`, `size`, `sort`)

**Theory.** `Pageable` is a **web + data** bridge: request parameters map to page index, size, and sort. The controller stays thin; the repository accepts `Pageable` and returns `Page`.

**What to take away.** Security/validation: **limit max page size** in production; unbounded `size` is a DoS vector. Know default sort and **whitelist** sort fields when exposing them to clients.

### Lesson 9: Dynamic `Sort` via `Sort.by` without encoding sort fields in the method name

**Theory.** Sort can be built in **service** or **controller** code (`Sort.by("name").ascending()`) instead of fifty repository methods. Keeps repository interfaces stable.

**What to take away.** Combine with **allowed field lists**—never pass raw client strings straight into `Sort` without validation (SQL injection is not the issue; **unexpected** sorts and missing indexes are).

### Lesson 10: Query by Example — probe entities; weak for OR-heavy searches

**Theory.** **QBE** builds a query from a **probe** entity: non-null fields become equality predicates (with matchers). Good for **admin screens** and simple filters; bad for **OR**, ranges, and full-text.

**What to take away.** Interview: QBE is **not** a search engine; Specifications or query DSLs scale better for dynamic filters.

### Lesson 11: Interface projections — return only selected columns

**Theory.** Projections map query results to **interfaces** (or DTOs) so Hibernate can **avoid** hydrating full entities. Less data, clearer API boundaries for reads.

**What to take away.** Closed projections (explicit getters) vs open projections (SpEL)—lesson 30 touches SpEL in queries; know the **performance** and **type-safety** tradeoff.

### Lesson 12: JPQL constructor expressions — DTO / class-based reads

**Theory.** `select new com.example.MyDto(a.id, a.name) …` builds a **non-entity** type in one round-trip. Immutability and **read models** stay explicit.

**What to take away.** Constructor must match **signature**; refactors break queries at compile time if you use typed JPQL in tests. Contrast with interface projections (lesson 11).

---

## Part C — Writes, fetching, lazy failures, transactions, `save` semantics (lessons 13–17)

### Lesson 13: `@Modifying` + `clearAutomatically` / `flush` for bulk JPQL updates

**Theory.** JPQL `UPDATE`/`DELETE` bypasses the normal “change tracking” path for **every** row in memory. After a bulk update, the persistence context can be **stale**. `clearAutomatically` / explicit `flush` align context and database.

**What to take away.** Interviews: bulk JPQL vs **jdbcTemplate** batch vs **specification delete**; always discuss **caching** (2nd level) and **listeners** not firing row-by-row.

### Lesson 14: `@EntityGraph` reduces N+1 when loading associations

**Theory.** **N+1**: one query for parents, then **N** queries for each child collection. `@EntityGraph` (or `join fetch`) declares **which associations** to fetch in the **same** or **fewer** queries.

**What to take away.** Graphs must match **use case**—over-fetching hurts memory and join cardinality. Lesson 28 combines graphs with **pagination** (duplicates).

### Lesson 15: `LazyInitializationException` — touch lazy fields inside `@Transactional`

**Theory.** Lazy proxies load data through the **session**. No open session (transaction ended) → **no load** → exception. This is correct JPA behavior, not a bug.

**What to take away.** Fixes: **transactional** service method, **fetch join** / graph, **DTO projection** at query time, or `OpenEntityManagerInView` (web) with awareness of **performance** cost.

### Lesson 16: Keep `@Transactional` on services; repositories stay persistence-focused

**Theory.** **Transaction boundaries** belong at **use-case** level: one service method = one business operation = one transactional unit. Repositories are **data access**; nesting or splitting transactions wrongly causes **partial commits** and inconsistent aggregates.

**What to take away.** `readOnly`, propagation, and rollback rules are **service** concerns. Interviews: `@Transactional` on **controller** is usually a smell.

### Lesson 17: `save()` — `persist` vs `merge` depending on new vs detached

**Theory.** **New** entity (no id / not managed) → typically **persist**. **Detached** (has id, not in context) → **merge** (copy state into managed instance). Wrong assumption causes **extra SELECT**, **duplicate** rows, or **unexpected** updates.

**What to take away.** For assigned IDs or DTO rehydration, be explicit about **lifecycle**. `merge` is heavier; batching and **reference** by id (lesson 23) interact with this.

---

## Part D — Batching, dynamic predicates, locking, flush, reads, references, aggregates (lessons 18–27)

### Lesson 18: `saveAll` + JDBC batch properties for bulk inserts

**Theory.** JPA can **batch** INSERT/UPDATE statements if the persistence provider and datasource support **batching** and you tune `hibernate.jdbc.batch_size` (and related) appropriately. Without batching, `saveAll` can still mean **many round-trips**.

**What to take away.** Interview: **order of inserts**, **ID generation** strategy (identity vs sequence) affects batching. Validate with SQL logging in dev.

### Lesson 19: Specifications — compose dynamic predicates without stringly JPQL

**Theory.** **JPA Criteria** wrapped in Spring’s `Specification<T>` lets you **combine** predicates (`and`/`or`) in code—good for **optional filters** on one repository method.

**What to take away.** Criteria is verbose but **type-safe** at compile time compared to concatenating JPQL strings. Contrast with Querydsl if the team uses it.

### Lesson 20: Optimistic locking with `@Version` and `ObjectOptimisticLockingFailureException`

**Theory.** A **version** column increments on each successful write. If two transactions read the same version and both commit, the **second** fails—**no** DB row lock held for the whole think-time. Scales better than pessimistic locks for **contention-light** domains.

**What to take away.** UX: **retry** or **show conflict**. Not a substitute for **invariant** enforcement at the DB (unique constraints, checks).

### Lesson 21: `flush()` — sends SQL early; still same transaction

**Theory.** `flush` pushes pending changes to the DB **without** committing—needed before **native** queries that must see pending state, or before **constraints** trigger in the right order.

**What to take away.** Overusing `flush` hurts **batching**; use **purposefully**.

### Lesson 22: `@Transactional(readOnly = true)` for read-heavy service methods

**Theory.** Hints the provider to **optimize** (no dirty checking, sometimes read-only connection). Does **not** replace **authorization** or **validation**.

**What to take away.** Spring Data’s `find` methods are read-only by nature at the **DB** level, but **service** boundaries still need the hint for **whole** use cases.

### Lesson 23: `getReferenceById` — lazy proxy; `findById` — `Optional` with round-trip when absent

**Theory.** **Reference** returns a **proxy** without hitting DB until accessed—good for **foreign keys** when you only need an id-shaped handle. **findById** **selects** and returns empty if missing—clear for **validation**.

**What to take away.** Using `getReferenceById` then accessing fields without checking existence → **EntityNotFoundException** at access time. Know **when** that is acceptable.

### Lesson 24: `CascadeType` + `orphanRemoval` — aggregate deletes

**Theory.** **Cascade** propagates operations (persist/remove) to associations. **orphanRemoval** deletes **child** rows when removed from the **parent** collection—models **aggregate** consistency (DDD bounded context).

**What to take away.** Dangerous on **large** graphs or **shared** entities; prefer explicit delete or **soft delete** for audit trails.

### Lesson 25: JPA auditing — `@CreatedDate` / `@LastModifiedDate` via `EntityListeners`

**Theory.** **Auditing** fills timestamps (and optionally users) from Spring Data JPA’s `AuditingEntityListener` and an `AuditorAware` bean. Keeps **consistent** metadata without manual `setCreatedAt` in every service.

**What to take away.** Requires **enabled JPA auditing** in config; clock skew and **time zones** matter for “last modified” semantics.

### Lesson 26: Custom repository fragment + `*Impl` registered by naming convention

**Theory.** Extend a **custom interface**; Spring picks up `YourRepositoryCustomImpl` in the **same package** as the repository fragment. Lets you inject `EntityManager` for **complex** queries while keeping the main interface clean.

**What to take away.** Naming and **package** rules are easy to break—follow Spring Data’s **suffix** conventions.

### Lesson 27: `EntityManager.executeUpdate` for bulk operations beside repositories

**Theory.** Same family as lesson 13: **bulk** JPQL from code when it does not belong on the repository surface. Useful for **maintenance** tasks and **migration** scripts wrapped in services.

**What to take away.** Always consider **cache** invalidation and **audit** events—bulk bypasses entity lifecycle callbacks unless you configure them carefully.

---

## Part E — Pagination with joins and Spring Data JDBC (lessons 28–29)

### Lesson 28: Pagination + join fetch — duplicates, `distinct`, separate `countQuery`

**Theory.** Joining collections **multiplies rows**; `Page`’s **content** can duplicate entities unless you **`distinct`** or use a **DTO** query. **Count** queries must not use the same join graph blindly—Spring Data allows **`countQuery`** on `@Query` to supply a **simpler** count.

**What to take away.** Classic interview failure mode: “pagination + fetch join” → **wrong totals** or **wrong page size**. Sometimes **two queries** (ids then fetch) is cleaner than one giant join.

### Lesson 29: Spring Data JDBC — small aggregates, explicit SQL, no lazy navigation

**Theory.** **No** lazy loading: what you load is what you queried. **Aggregates** map cleanly to **repositories** per aggregate root; fewer **surprises** than ORM for read-heavy simple models.

**What to take away.** Contrast with JPA for **complex graphs** and **migrations** from existing Hibernate-centric code. Schema: see `schema-spring-data-study.sql` where used.

---

## Part F — SpEL, domain events, N+1 recap, indexes, uniqueness (lessons 30–34)

### Lesson 30: SpEL in `@Query` (`:#{…}`) — flexibility vs static analysis

**Theory.** SpEL binds **method parameters** or **beans** into queries dynamically. Powerful for **tenant** ids, **security** filters, or shared snippets; **harder** to validate at compile time and easier to misuse.

**What to take away.** Security: never let **raw user input** become SpEL—**parameterize** like normal JPQL. Prefer plain parameters when possible.

### Lesson 31: Domain events — `AbstractAggregateRoot` publishes after successful persist

**Theory.** **Domain events** decouple side effects (email, integrations) from **aggregate** mutation. Spring Data can **publish** after flush/commit depending on setup—understand **transaction** boundaries so listeners see **consistent** state.

**What to take away.** Compare with **transactional outbox** for **at-least-once** to external systems (higher-level microservices pattern).

### Lesson 32: Avoid N+1 — fetch join, `@EntityGraph`, or projections for API payloads

**Theory.** Recap and extension: **API** layers should not trigger **per-row** lazy loads. Choose **one** strategy per endpoint: **eager graph**, **batch fetching** (Hibernate setting), or **projection query**.

**What to take away.** Observability: enable **statistics** or SQL logging in dev to **prove** query count.

### Lesson 33: Indexes + selective predicates align repository methods with planner use

**Theory.** ORMs do not remove **index design**. Filters (`where status = ? and created_at > ?`) need **composite indexes** that match **selectivity** and **sort** order. Wrong index → **seq scan** at scale.

**What to take away.** Interview: tie **repository method names** to **explain plans** in staging; co-locate **pagination sort** with index order when possible.

### Lesson 34: Unique constraints + `DataIntegrityViolationException` for idempotent writes

**Theory.** **Database uniqueness** is the **final** authority for “only one.” Application checks race with concurrent requests; **constraint violation** maps to Spring’s `DataIntegrityViolationException` for **clean** handling (retry, map to 409).

**What to take away.** Contrast **optimistic lock** (lesson 20) vs **unique index**—different failure modes and UX.

---

## Optional: `@DataJpaTest` slice

Run:

`mvn -pl spring-data-interview-study test -Dtest=Lesson28DataJpaSliceTest`

This is **not** a CLI lesson; it shows a **narrow** Spring test context (repositories + JPA) without full web stack—useful to rehearse how slices differ from `@SpringBootTest`.

---

## Review drill (after each lesson)

1. **SQL story:** What queries ran (roughly), and **when** did the transaction commit?  
2. **Failure mode:** What breaks if you remove `@Transactional`, the wrong fetch graph, or pagination `count`?  
3. **Interview pitch:** One sentence on **why** this feature exists and **one** production pitfall.

Keep extending **this** document for new lessons so the module stays a **single** narrative index.
