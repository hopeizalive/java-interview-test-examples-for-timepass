# JPA / Hibernate interview study — concepts and code map

This module teaches **JPA and persistence provider behavior** (distinct from the Spring Data module, which focuses on repository APIs and Spring integration).

## How to run

- `mvn -pl jpa-interview-study exec:java -Dexec.args="list"`
- `run <n>` / `run-all` (see repo root `guide.md` for the shared `run-all` contract and `--errors-log`).

## Where the lessons live

| Area | Role |
|------|------|
| `com.example.jpa.interview.lesson.Lesson01` … `Lesson50` | One class per lesson: title + `run(StudyContext)` |
| `com.example.jpa.interview.lesson.LessonCatalog` | Ordered list of all 50 lessons |
| `com.example.jpa.interview.study.StudyContext` | Shared `EntityManager` / factory access for the CLI run |
| `META-INF/persistence.xml` | Persistence unit `studyPU` (in-memory H2) |

## How to use this guide

1. Run `list` and pick a lesson number.
2. Open the matching `LessonNN.java` — that file is the **implementation** and usually logs **talking points**.
3. For interview prep, explain **what** the API does (e.g. flush, detach, cache), **why** it matters, and **what symptom** you see when it is misused.

Optional cross-reference at repo root: `jpa-interview-code-answers.md` (if present) for written Q&A style notes.

> **Single-doc rule:** Prefer extending **this** `STUDY_GUIDE.md` with new sections or a lesson index table rather than scattering many small markdown files per lesson.
