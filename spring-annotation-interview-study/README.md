# Spring Annotation Interview Study

Runnable, code-first Spring Boot annotation comparison lessons for interview preparation.

## Run

- List lessons:
  - `mvn -pl spring-annotation-interview-study exec:java -Dexec.args="list"`
- Run one lesson:
  - `mvn -pl spring-annotation-interview-study exec:java -Dexec.args="run 12"`
- Run all lessons:
  - `mvn -pl spring-annotation-interview-study exec:java -Dexec.args="run-all"`

## Design

- Standalone root module.
- `AnnotationLesson` enum: titles and numbers only; `run` delegates to `AnnotationLessonDispatch`.
- Five **lesson blocks** (`lesson.blocks.LessonBlock01*` … `LessonBlock05*`), ten lessons each, with chapter-level Javadoc plus per-lesson **Story** / **Takeaway** logs.
- Spring fixture types live under `lesson.fixtures` (grouped by concern), kept `public static` where CGLIB must enhance `@Configuration`.
- No per-lesson `LessonXX` classes.
- Every lesson is executable and demonstrates annotation behavior with logs.
