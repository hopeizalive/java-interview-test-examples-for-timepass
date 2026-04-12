---
name: ""
overview: ""
todos: []
isProject: false
---

---

name: Spring Data study module
overview: Add a dedicated Maven module with 50 **technical interview** lessons on Spring Data and DB access, each backed by **runnable code** plus a **theory markdown doc** for study; expose runs via Maven and a **root `.cmd` launcher** (same ergonomics as [microservices-study.cmd](c:\Users\ahsan\IdeaProjects\untitled\microservices-study.cmd)). Use the [Spring Data project page](https://spring.io/projects/spring-data) as **reading** for the umbrella—not the primary artifact.
todos:

- id: parent-module
content: Add `spring-data-interview-study` to parent `pom.xml` modules list
status: pending
- id: new-pom
content: Create `spring-data-interview-study/pom.xml` (Boot + spring-boot-starter-data-jpa + H2 + picocli; exec + jar manifest)
status: pending
- id: study-core
content: Add `StudyLesson`, `StudyContext`, `LessonCatalog` (50 entries + assertCoverage), and `SpringDataStudyCli` (list / run N / run-all)
status: pending
- id: boot-harness
content: Add small Boot launch helper (pattern like `MicroBoot`) for lessons—discrete `sddata.lNN` apps or scoped configs
status: pending
- id: theory-docs
content: Add `spring-data-interview-study/docs/theory/lesson-01.md` … `lesson-50.md` (template below); keep in sync with lesson titles and interview question
status: pending
- id: root-batch
content: Add repo-root `spring-data-study.cmd` mirroring `microservices-study.cmd` (`mvnw -pl spring-data-interview-study compile exec:java` + passthrough args)
status: pending
- id: lessons-01-25
content: Implement lessons 1–25 with entities/repos/services and `run()` logs + talking points
status: pending
- id: lessons-26-50
content: Implement lessons 26–50 (same pattern; include specs, JDBC slice, test slice, multi-store awareness where feasible)
status: pending
- id: verify-build
content: `mvn -pl spring-data-interview-study -am package`, smoke CLI, and smoke `spring-data-study.cmd list`
status: pending
isProject: false

---

# Spring Data interview module (runnable lessons)

## What this module is for

- **Primary goal:** Prepare for **technical interviews** where candidates must discuss **database access in Spring**: repositories, queries, transactions, lazy loading, pagination, projections, testing, and when to use which Spring Data technology.
- **Shape:** Same idea as [Lesson11](c:\Users\ahsan\IdeaProjects\untitled\spring-microservices-questions\src\main\java\com\example\microservices\interview\lesson\Lesson11.java) (Boot context + `JpaRepository` + service + `ctx.log` talking points) and [jpa-interview-study](c:\Users\ahsan\IdeaProjects\untitled\jpa-interview-study): **50 numbered lessons**, **CLI** (`list`, `run <n>`, `run-all`), **H2** (or embedded DB) so every lesson runs without external infra unless a lesson explicitly adds Testcontainers later.
- **Spring.io link:** Read [Spring Data](https://spring.io/projects/spring-data) to understand the **umbrella** (Commons, JPA, JDBC, R2DBC, MongoDB, Redis, REST, etc.) and **release train / BOM**. The **code in this repo** is what you rehearse—not a reprint of that page.

## Non-goals

- Not a second copy of every [jpa-interview-study](c:\Users\ahsan\IdeaProjects\untitled\jpa-interview-study) lesson: that module is **JPA/Hibernate mechanics**; this one is **Spring Data APIs**, repository patterns, and **interview angles** (N+1, `Page` vs `Slice`, `@Query`, `@EntityGraph`, `@DataJpaTest`, etc.).
- No changes to [spring-microservices-questions](c:\Users\ahsan\IdeaProjects\untitled\spring-microservices-questions) lessons unless you explicitly want cross-links later.

## Maven layout

1. **Parent** [pom.xml](c:\Users\ahsan\IdeaProjects\untitled\pom.xml): add `<module>spring-data-interview-study</module>`.
2. `**spring-data-interview-study/pom.xml`**:
  - `spring-boot-starter-data-jpa`, H2 (via parent BOM), `spring-boot-starter-validation` if lessons use bean validation on entities.
  - `info.picocli:picocli` for CLI.
  - `exec-maven-plugin` + `maven-jar-plugin` with `mainClass` → `SpringDataStudyCli` (names TBD; mirror [JpaStudyCli](c:\Users\ahsan\IdeaProjects\untitled\jpa-interview-study\src\main\java\com\example\jpa\interview\cli\JpaStudyCli.java)).

## Code layout (convention)

- Package root: e.g. `com.example.springdata.interview`.
- `**lesson`**: `Lesson01` … `Lesson50`, `AbstractLesson`, `LessonCatalog` (list of 50, `assertCoverage()`).
- `**study`**: `StudyLesson` (`number`, `title`, `run(StudyContext)`), `StudyContext` (logger / `log(String)`).
- `**sddata.lNN`**: Per-lesson (or small groups) **Spring Boot** slice—`Application`, `application.properties` with unique `spring.application.name` / DB name if needed, entities, `JpaRepository` interfaces, services, optional `@Controller` for `Pageable` demos.
- Reuse the **“start Boot app, get bean, invoke, log, close”** pattern from microservices lessons so interviews can point at **concrete** repository method names and SQL/Hibernate behavior in logs.

## CLI behavior

- `list` — print `1..50` with **short interview-oriented titles**.
- `run <n>` — run one lesson (Boot context lifecycle inside `run`).
- `run-all` — sequential run for CI smoke (may be slower; acceptable for study module).

## Interview question list (50) — each lesson answers this in prose + code

For each row, **Lesson N** implements the answer with **working Spring Data / persistence code** and a **talking point** in logs (like existing modules).


| #   | Interview question (technical)                                                                                                 | What the codebase should demonstrate                                                                                                                                                                                                                                                                              |
| --- | ------------------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | What problem does Spring Data solve vs raw JDBC/JPA in a Spring app?                                                           | Minimal Boot + `JpaRepository`; show less boilerplate than `EntityManager`-only.                                                                                                                                                                                                                                  |
| 2   | Explain the repository interface hierarchy (`Repository` → `CrudRepository` → `PagingAndSortingRepository` → `JpaRepository`). | One repo extending `JpaRepository`; call `save`, `findById`, `findAll`.                                                                                                                                                                                                                                           |
| 3   | How does Spring Data implement your repository interface at runtime?                                                           | Optional `@EnableJpaRepositories`; mention JDK dynamic proxy / `SimpleJpaRepository`.                                                                                                                                                                                                                             |
| 4   | What is query derivation from method names? Give an example and a limitation.                                                  | `findByLastNameIgnoreCaseAndActiveTrue` + log generated behavior; note unreadable names / complex queries.                                                                                                                                                                                                        |
| 5   | When do you switch from derived queries to `@Query` (JPQL)?                                                                    | Same entity: JPQL `@Query` with `@Param`.                                                                                                                                                                                                                                                                         |
| 6   | How do native queries fit in, and what breaks if you misuse them?                                                              | `@Query(nativeQuery = true)` + caution on pagination/SQL dialect.                                                                                                                                                                                                                                                 |
| 7   | What is the difference between `Page<T>` and `Slice<T>` for DB-backed APIs?                                                    | Two methods: `Page` (count query) vs `Slice` (next flag only).                                                                                                                                                                                                                                                    |
| 8   | How does `Pageable` work with Spring MVC?                                                                                      | Tiny web slice or standalone service taking `Pageable`; log page size/sort.                                                                                                                                                                                                                                       |
| 9   | How do you apply **dynamic sorting** (`Sort`) without hard-coding in the method name?                                          | `findAll(Sort)` or `Sort.by(...).ascending()`.                                                                                                                                                                                                                                                                    |
| 10  | What is Query by Example (`Example` / `ExampleMatcher`)? When is it inappropriate?                                             | `Example.of(probe)`; note null-handling and lack of OR-heavy queries.                                                                                                                                                                                                                                             |
| 11  | What are **interface projections** and why use them in read paths?                                                             | Closed projection interface; only selected columns fetched.                                                                                                                                                                                                                                                       |
| 12  | What are **class-based projections** (DTOs) in Spring Data JPA?                                                                | JPQL `select new ...` or interface; compare to entity graph for reads.                                                                                                                                                                                                                                            |
| 13  | How does `@Modifying` work with `@Query`, and what must you do for JPA?                                                        | Update/delete query + `@Modifying` + clear automatically / flush.                                                                                                                                                                                                                                                 |
| 14  | What is `@EntityGraph` used for, and how does it relate to N+1?                                                                | Two queries: without vs with entity graph; contrast result shape.                                                                                                                                                                                                                                                 |
| 15  | Explain **lazy loading** and `LazyInitializationException` in a typical Spring service.                                        | Transaction boundary: access outside `@Transactional` fails (demonstrate + fix).                                                                                                                                                                                                                                  |
| 16  | Where should `@Transactional` live—repository or service—and why?                                                              | Service orchestrates; repo stays thin.                                                                                                                                                                                                                                                                            |
| 17  | What is the difference between `save()` behavior for new vs detached entities?                                                 | `persist` vs `merge` mental model; log IDs before/after.                                                                                                                                                                                                                                                          |
| 18  | How do `saveAll` / batch inserts interact with flush and performance?                                                          | Multiple entities; mention batch size property where relevant.                                                                                                                                                                                                                                                    |
| 19  | What are **Specifications** (`JpaSpecificationExecutor`) for?                                                                  | Dynamic predicates (e.g. optional filters) without stringly JPQL.                                                                                                                                                                                                                                                 |
| 20  | How do you handle **optimistic locking** with Spring Data JPA?                                                                 | `@Version` field; show conflict scenario or explain in code + log.                                                                                                                                                                                                                                                |
| 21  | What is `flush()` vs `commit()` visibility in tests and services?                                                              | `EntityManager` or repo + `@Transactional` flush demo.                                                                                                                                                                                                                                                            |
| 22  | How do you run **read-only** transactions for query-heavy paths?                                                               | `@Transactional(readOnly = true)` on service method calling repo.                                                                                                                                                                                                                                                 |
| 23  | What is `JpaRepository`’s `getReferenceById` / `getById` vs `findById`?                                                        | Lazy proxy vs `Optional`; when each is appropriate.                                                                                                                                                                                                                                                               |
| 24  | How do **cascade** settings affect `remove` / orphan removal in JPA + repositories?                                            | Parent-child aggregate; `delete` behavior.                                                                                                                                                                                                                                                                        |
| 25  | What is `AuditingEntityListener` / `@CreatedDate` in Spring Data JPA?                                                          | Enable JPA auditing; save entity and log timestamps.                                                                                                                                                                                                                                                              |
| 26  | How do you add **custom repository** code (fragment + impl)?                                                                   | `CustomRepo` + `CustomRepoImpl` + `JpaRepository` composition.                                                                                                                                                                                                                                                    |
| 27  | What is `EntityManager` still used for when you already have repositories?                                                     | Bulk operation or explicit `detach`/`clear` lesson.                                                                                                                                                                                                                                                               |
| 28  | How does `@DataJpaTest` slice the context vs `@SpringBootTest`?                                                                | Optional lesson using test slice (if tests added) or document + minimal app showing only JPA beans.                                                                                                                                                                                                               |
| 29  | How do you test repositories against **real SQL** vs H2?                                                                       | Testcontainers optional lesson or “pattern” lesson with notes + stub.                                                                                                                                                                                                                                             |
| 30  | What pitfalls appear with **pagination + join fetch**?                                                                         | Demonstrate duplicate rows or need for `countQuery` / distinct patterns (concept + simplified query).                                                                                                                                                                                                             |
| 31  | What is Spring Data **JDBC** and how is the programming model different from JPA?                                              | If dependency added: one aggregate + `JdbcAggregateTemplate` / repository; else conceptual mini-config + reading notes.                                                                                                                                                                                           |
| 32  | When would you choose JDBC over JPA in a microservice?                                                                         | Tradeoffs lesson: complexity vs ORM, team skill, reporting.                                                                                                                                                                                                                                                       |
| 33  | What is Spring Data **R2DBC** and when is it used?                                                                             | Concept + dependency-light explanation; optional reactive repo if scope allows.                                                                                                                                                                                                                                   |
| 34  | How does Spring Data **Redis** map to repositories vs `RedisTemplate`?                                                         | High-level: if only JPA in module, use **concept lesson** + link; or add `spring-boot-starter-data-redis` + embedded/embedded not available—use Testcontainers or pure conceptual with interface sketch. **Prefer:** conceptual + interface code without requiring Redis for v1, or single Testcontainers lesson. |
| 35  | What serialization issues hit you when storing objects in Redis?                                                               | Interview answer + pseudo-config in comments or minimal module.                                                                                                                                                                                                                                                   |
| 36  | How does Spring Data **MongoDB** differ mentally from JPA repositories?                                                        | Optional dep + one `MongoRepository` or conceptual lesson comparing document vs relational.                                                                                                                                                                                                                       |
| 37  | What is Spring Data **REST** and why is exposing repositories directly risky?                                                  | Small app or config snippet + security/over-exposure talking points.                                                                                                                                                                                                                                              |
| 38  | What is Spring Data **Elasticsearch** used for in architecture?                                                                | Search vs primary store; interview framing.                                                                                                                                                                                                                                                                       |
| 39  | What problem does **Cassandra** module solve?                                                                                  | Write-heavy / partition key thinking at high level.                                                                                                                                                                                                                                                               |
| 40  | What is Spring Data **LDAP** for in enterprise SSO directories?                                                                | Repository over LDAP entries; conceptual + `LdapRepository` mention.                                                                                                                                                                                                                                              |
| 41  | What is **Neo4j** Spring Data for?                                                                                             | Graph relationships vs relational joins; high level.                                                                                                                                                                                                                                                              |
| 42  | How does the Spring Data **release train / BOM** affect your `pom`?                                                            | Parent import / alignment with Spring Boot BOM (no version drift).                                                                                                                                                                                                                                                |
| 43  | What is **CalVer** in Spring Data and why should candidates care?                                                              | Upgrade cadence vs feature backports.                                                                                                                                                                                                                                                                             |
| 44  | How do **SpEL** expressions in `@Query` help or hurt maintainability?                                                          | `@Query` with `:#{#...}` example or caution.                                                                                                                                                                                                                                                                      |
| 45  | What are **domain events** (`AbstractAggregateRoot` / `@DomainEvents`) in Spring Data JPA?                                     | Publish on save; interview DDD angle.                                                                                                                                                                                                                                                                             |
| 46  | How do you avoid **N+1** in a REST API returning nested DTOs?                                                                  | Fetch join / entity graph / DTO projection lesson.                                                                                                                                                                                                                                                                |
| 47  | What is the **Open Session In View** debate in Spring Boot?                                                                    | `spring.jpa.open-in-view` default; why turn off in APIs.                                                                                                                                                                                                                                                          |
| 48  | How do **database indexes** relate to Spring Data method names?                                                                | Entity `@Table(indexes=...)` + query that should use index; explain `EXPLAIN` mindset.                                                                                                                                                                                                                            |
| 49  | How do you design **idempotency** or duplicate handling at the persistence layer?                                              | Unique constraint + `DataIntegrityViolationException` handling pattern.                                                                                                                                                                                                                                           |
| 50  | Summarize how you pick **JPA vs JDBC vs document vs cache** for a new service.                                                 | Capstone lesson: short narrative + references to earlier lesson packages.                                                                                                                                                                                                                                         |


**Note:** Lessons 31–41 that touch Redis/Mongo/ES/Cassandra/LDAP/Neo4j may be **concept + minimal code** or **optional extra dependency** where embedded servers are impractical; the **default spine** (lessons 1–30, 42–50) stays **Boot + Data JPA + H2** so `run-all` is reliable offline.

## Verification

- `mvn -pl spring-data-interview-study -am package` passes.
- `mvn -pl spring-data-interview-study exec:java -Dexec.args="list"` shows 50 lines.
- `mvn -pl spring-data-interview-study exec:java -Dexec.args="run 1"` runs without external Docker (for the H2-only spine).

## Optional later enhancements

- Add Testcontainers profile for Postgres + one lesson on dialect-specific SQL.
- Add `spring-boot-starter-data-mongodb` + Testcontainers for lesson 36 as a second profile.

