# Guide for Using in Linux Environment

## Prerequisites
- Java 21 must be installed and set as JAVA_HOME
- The scripts automatically set JAVA_HOME to `/usr/lib/jvm/java-21-openjdk-amd64`

## Building the Project

To build all modules, run:
```
./mvnw clean compile
```

(On Windows you can use `mvn-idea.cmd compile` from the repo root if that is your IDE-aligned wrapper.)

## Learning guides (concepts + where the code is)

Each interview study module keeps **one** markdown guide so you can read **what** is being taught, **where** it is implemented, and **why** it matters—without turning every Java class into a long essay.

| Module | Guide |
|--------|--------|
| Spring Data (repositories, Spring integration; 34 runnable lessons) | [spring-data-interview-study/docs/STUDY_GUIDE.md](spring-data-interview-study/docs/STUDY_GUIDE.md) |
| JPA / Hibernate mechanics | [jpa-interview-study/docs/STUDY_GUIDE.md](jpa-interview-study/docs/STUDY_GUIDE.md) |
| Concurrency | [concurrency-interview-study/docs/STUDY_GUIDE.md](concurrency-interview-study/docs/STUDY_GUIDE.md) |
| Spring Security | [spring-security-questions/LESSONS.md](spring-security-questions/LESSONS.md) (lesson list + topics) |
| Spring Microservices (45 runnable lessons; pure narrative entries removed) | `spring-microservices-questions` CLI + catalog; optional plan under `.cursor/plans/` |

**Convention for new study modules:** add `your-module/docs/STUDY_GUIDE.md` and link it from this section.

## Interview study CLI contract (`run-all`)

All **interview study** modules (JPA, Spring Data, Concurrency, Security, Microservices) share the same expectations for `run-all` so CI and local smoke runs behave consistently.

### Shared library

- **Artifact:** `interview-study-cli-support` (module `interview-study-cli-support/`)
- **Package:** `com.example.interview.studycli.runall`
- **Use this** instead of copying `failureMessage` helpers or ad-hoc try/catch loops into each new study module.

### What `run-all` must do

1. **Continue on failure** — run every lesson in catalog order; one failing lesson must not stop the rest.
2. **Exit code** — `0` if all passed, `1` if any lesson failed (unless the module has an additional fatal error path, e.g. report directory creation, which may use `2`).
3. **Stderr line per failure** — `[FAILED] Lesson N: <root-cause one-liner>` using `RunAllThrowableFormatter.rootCauseMessage(Throwable)`.
4. **Stdout summary** — final line `========== run-all complete: X passed, Y failed ==========` plus a numbered list of failed lessons (number + same one-liner). Use `StudyRunAllExecutor.printStandardSummary`.
5. **Optional stack trace log** — Picocli option `--errors-log` / `-e` with a **default file name in the current working directory** (per module). On the **first failure of that run**, truncate and write a header, then **append** a delimited section per failure with the **full** stack trace. Implementation: `RunAllStackTraceLogWriter` (synchronized methods for a clear concurrency story if the runner is ever extended). After the summary, if there were failures, print the absolute path to that log (`StudyRunAllExecutor.printStackTraceLogPointer`).

### Wiring a new study module

1. Add a Maven dependency on `org.example:interview-study-cli-support` (same `${project.version}` as the parent).
2. Build `List<RunAllTask>`: each task has `number`, `title`, and a `StudyLessonAction` that closes over your shared context (e.g. `StudyContext`, `EntityManagerFactory`, etc.).
3. Call `StudyRunAllExecutor.execute("<Module name> interview study", errorsLogPath, tasks)` then print summary + stack-trace pointer.
4. Reuse the **same Picocli option names** (`--errors-log`, `-e`) and the same **semantic** (default log file name pattern `<cli-name>-run-all-errors.log`).

### Microservices module note

Microservices **also** writes a timestamped PASS/FAIL **report** under `target/microservices-reports/` via `RunAllReportWriter`. That is **in addition to** the shared stack-trace log contract above, not a replacement.

## Running the Studies

### Microservices Study
- Run with Maven: `./microservices-study.sh`
- Run with JAR: First build `./mvnw -pl spring-microservices-questions package`, then `./microservices-study-jar.sh`

### Security Study
- Run with Maven: `./security-study.sh`
- Run with JAR: First build `./mvnw -pl spring-security-questions package`, then `./security-study-jar.sh`

### Spring Data Study
- Run with Maven: `./spring-data-study.sh`
- Run with JAR: First build `./mvnw -pl spring-data-interview-study package`, then `./spring-data-study-jar.sh`

### Concurrency Study
- Operator / search reference: `concurrency-study.messages.txt`
- Run with Maven: `./concurrency-study.sh` (pass args after the script, e.g. `./concurrency-study.sh list`)
- Run with JAR: First build `./mvnw -pl concurrency-interview-study package`, then `./concurrency-study-jar.sh`

### JPA Study
- `run-all` follows the same contract; default stack trace log: `jpa-study-run-all-errors.log` in the working directory.
