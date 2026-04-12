# Concurrency interview study — concepts and code map

Lessons mix **JDK concurrency** (`java.util.concurrent`, locks, virtual threads where relevant) with **optional Spring / Kafka** demos where the catalog says so.

## How to run

- `mvn -pl concurrency-interview-study exec:java -Dexec.args="list"`
- `run <n>` / `run-all` — continues on failure; stack trace log via `--errors-log` (see repo root `guide.md`).

## Where the lessons live

| Area | Role |
|------|------|
| `com.example.concurrency.interview.lesson.ConcurrencyLesson` | Enum: lesson **number**, **title**, and `run` implementation |
| `com.example.concurrency.interview.lesson.LessonCatalog` | Catalog + coverage assert |
| `com.example.concurrency.interview.demos` | Supporting demo types some lessons delegate to |
| `concurrency-study.messages.txt` (repo root, if present) | Quick reference for operators / APIs |

## How to use this guide

1. Run `list` — titles are the **interview topics**.
2. Open `ConcurrencyLesson` and jump to the enum constant for that number.
3. Rehearse: **behavior** (ordering, visibility, cancellation), **pitfalls** (deadlock, starvation), and **when** you would use the primitive in production.

> **Single-doc rule:** Add new narrative or a lesson index **here** rather than adding one markdown file per lesson.
