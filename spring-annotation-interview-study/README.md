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
- Grouped lesson implementation in a single enum (`AnnotationLesson`).
- No per-lesson `LessonXX` classes.
- Every lesson is executable and demonstrates annotation behavior with logs.
