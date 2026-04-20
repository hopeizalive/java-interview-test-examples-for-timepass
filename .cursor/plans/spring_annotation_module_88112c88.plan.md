---
name: Spring Annotation Module
overview: Add a new code-only Spring Boot annotations interview module with deep comparison-focused lessons and runnable examples, using grouped single-class lesson organization (no many per-lesson classes) and mandatory teaching comments.
todos:
  - id: scaffold-grouped-module
    content: Create grouped annotation lesson enum/class structure aligned with concurrency style
    status: completed
  - id: implement-group-a-to-c
    content: Implement grouped A+B+C comparison entries with runnable code outputs
    status: completed
  - id: implement-group-d-to-g
    content: Implement grouped D+E+F+G advanced comparisons and testing entries
    status: completed
  - id: catalog-enum-wireup
    content: Register grouped lesson enum in catalog and verify numbering/coverage
    status: completed
  - id: apply-comment-guide
    content: Apply CODE_COMMENT_GUIDE comment layers to grouped lesson implementations
    status: completed
  - id: verify-build
    content: Run tests/build and resolve compilation or lint issues
    status: completed
isProject: false
---

# Spring Boot Annotation Comparisons Module Plan

## Goal
Create a new lesson module dedicated to **Spring Boot annotation comparison interview questions**, where each lesson is implemented as a runnable Java code example (no theory-only lessons).

## Where This Fits In Current Project
- Create a **new independent root module** (separate from existing modules) named:
  - [c:\Users\ahsan\IdeaProjects\untitled\spring-annotation-interview-study](c:\Users\ahsan\IdeaProjects\untitled\spring-annotation-interview-study)
- Do **not** reuse classes/files from `spring-microservices-questions`; keep implementation, catalog, and study contracts local to the new module.
- Follow the grouped organization style inspired by concurrency module patterns:
  - [c:\Users\ahsan\IdeaProjects\untitled\concurrency-interview-study\src\main\java\com\example\concurrency\interview\lesson\LessonCatalog.java](c:\Users\ahsan\IdeaProjects\untitled\concurrency-interview-study\src\main\java\com\example\concurrency\interview\lesson\LessonCatalog.java)
  - Use enum/grouped lesson entries as source of truth instead of creating many separate `LessonXX` classes.

## Module Design
- Add a dedicated annotation-focused module namespace in the new root module with grouped implementation units (single enum-driven lesson container + one catalog), avoiding many individual lesson classes.
- Each lesson will include:
  - interview-style comparison question in title/comment
  - minimal runnable demo code proving behavior
  - logged output that shows “why this annotation over that one”
  - brief “talking point” line tied to interviewer follow-up traps
- Prefer lightweight, module-local bootstraps/utilities for speed (defined inside this new module, no cross-module dependency).
- Group entries by topic sections (A–G) inside one lesson container so functionality stays centralized and easier to navigate.

## New Module Root Files (Planned)
- Create root-level module files under `spring-annotation-interview-study`:
  - `pom.xml` or `build.gradle` (aligned with workspace build approach)
  - `README.md`
  - `STUDY_GUIDE.md` (module-specific guidance + comment rules)
  - `src/main/java/.../study/StudyLesson.java`
  - `src/main/java/.../study/StudyContext.java`
  - `src/main/java/.../study/StudyRunner.java`
  - `src/main/java/.../lesson/AnnotationLesson.java` (grouped enum entries)
  - `src/main/java/.../lesson/LessonCatalog.java`

## Commenting Standard (Mandatory)
- Apply [c:\Users\ahsan\IdeaProjects\untitled\CODE_COMMENT_GUIDE.md](c:\Users\ahsan\IdeaProjects\untitled\CODE_COMMENT_GUIDE.md) to this module.
- Every grouped lesson entry/method will include:
  - Purpose/Role/Demonstration Javadoc
  - execution-story inline comments for setup/action/boundary/observation
  - interview takeaway log/comment
- Avoid noisy comments and keep explanation focused on behavioral differences between compared annotations.

## Web-Researched Question Backlog (Code-First)

### A) Core stereotype and bean registration comparisons
1. `@Component` vs `@Service` vs `@Repository`: what changes at runtime, especially exception translation.
2. `@Component` vs `@Bean`: scanned class vs explicit factory method.
3. `@Configuration` vs `@Component`: inter-bean calls and singleton guarantees.
4. `@Configuration(proxyBeanMethods=true)` vs `false`: duplicate instance trap.
5. `@SpringBootApplication` vs explicit `@SpringBootConfiguration + @EnableAutoConfiguration + @ComponentScan`.
6. `@ComponentScan` default package behavior vs explicit `basePackages`.
7. `@Import` vs component scan for wiring feature modules.
8. `@Primary` vs `@Qualifier`: default selection vs explicit bean pick.
9. `@Autowired` field injection vs constructor injection in tests and immutability.
10. `@Lazy` on bean vs on injection point (startup impact and cycle avoidance).

### B) Configuration/property annotation comparisons
11. `@Value` vs `@ConfigurationProperties` for grouped/nested config.
12. `@ConfigurationProperties` mutable bean vs constructor-bound immutable record.
13. `@EnableConfigurationProperties` vs `@ConfigurationPropertiesScan`.
14. `@Profile` vs `@ConditionalOnProperty` for environment vs feature toggle.
15. `@ConditionalOnBean` vs `@ConditionalOnMissingBean` customization patterns.
16. `@ConditionalOnClass` vs `@ConditionalOnProperty` in auto-config behavior.
17. `matchIfMissing=true` vs explicit default property behavior.
18. `@PropertySource` vs Boot-native externalized config loading.

### C) Web/controller annotation comparisons
19. `@Controller` vs `@RestController` and implicit `@ResponseBody` behavior.
20. `@RequestMapping` vs composed mappings (`@GetMapping`, `@PostMapping`).
21. `@PathVariable` vs `@RequestParam` for resource identity vs filtering.
22. `@RequestParam` vs `@RequestBody` in GET/POST API design.
23. `@ResponseStatus` vs `ResponseEntity` for status control.
24. `@ControllerAdvice` vs local `@ExceptionHandler`.
25. `@ModelAttribute` vs `@RequestBody` binding behavior.
26. `@CrossOrigin` local controller vs global CORS config.

### D) Persistence and transaction annotation comparisons
27. `@Repository` exception translation vs plain DAO class behavior.
28. `@Transactional` on class vs method precedence.
29. `@Transactional` default rollback (runtime only) vs `rollbackFor` checked exceptions.
30. `Propagation.REQUIRED` vs `REQUIRES_NEW` with nested service calls.
31. `Isolation.READ_COMMITTED` vs `REPEATABLE_READ` (demonstration scaffold).
32. `readOnly=true` vs default transaction (flush behavior demonstration).
33. `@Transactional` self-invocation proxy trap vs external proxy call.
34. `@EnableTransactionManagement` proxy mode considerations.

### E) Validation and binding annotation comparisons
35. `@Valid` vs `@Validated` (method-level groups).
36. `@NotNull` vs `@NotBlank` vs `@NotEmpty` using request DTOs.
37. `@InitBinder` custom binding vs default conversion.
38. Global validator bean vs annotation-only validation setup.

### F) Testing annotation comparisons (frequent interview focus)
39. `@SpringBootTest` vs `@WebMvcTest` context size and startup time.
40. `@DataJpaTest` vs `@SpringBootTest` for repository tests.
41. `@MockBean` vs Mockito `@Mock`/`@InjectMocks` scope differences.
42. `@TestConfiguration` vs production `@Configuration` override usage.
43. `@AutoConfigureMockMvc` vs full random-port HTTP tests.
44. Transactional test rollback defaults vs explicit commit behavior.

### G) Async, scheduling, and cross-cutting comparisons
45. `@Async` + `@EnableAsync` vs synchronous call path.
46. `@Scheduled(fixedRate)` vs `fixedDelay` vs `cron` behavior.
47. `@Cacheable` vs `@CachePut` vs `@CacheEvict` behavioral comparison.
48. `@Order` vs explicit bean chaining for interceptor/filter ordering.
49. `@EventListener` vs `@TransactionalEventListener` transaction phase timing.
50. `@Retryable` vs manual retry loop (if Spring Retry is enabled in module).

## Implementation Sequence
1. Create new root module `spring-annotation-interview-study` with independent build file and base study contracts.
2. Implement A→C comparisons as grouped entries with deterministic logs and code-only examples.
3. Implement D→G comparisons in the same grouped container, keeping question order contiguous.
4. Apply CODE_COMMENT_GUIDE requirements across all grouped entries (Javadocs + execution-story comments).
5. Register grouped entries in module-local catalog/runner and verify coverage/uniqueness assertions.
6. Run module tests/bootstraps and fix any lint/build issues.

## Source Anchors Used For Question Selection
- Spring Boot reference (`@SpringBootApplication`, auto-config composition): [https://docs.spring.io/spring-boot/reference/using/using-the-springbootapplication-annotation.html](https://docs.spring.io/spring-boot/reference/using/using-the-springbootapplication-annotation.html)
- Spring Framework reference (`@Transactional`, proxy interception constraints): [https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html)
- High-frequency community interview patterns aggregated from Baeldung, StackOverflow, JavaGuides/Java67, and current 2025–2026 interview prep discussions.

## Deliverable After Approval
- A fully coded, runnable annotation-comparison lesson module (code examples only), starting with the highest-yield interview questions first and extending across the full backlog above.