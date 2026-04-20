# Study Guide

This module follows `CODE_COMMENT_GUIDE.md`:

- each lesson method includes Purpose/Role/Demonstration Javadoc
- execution-story comments mark setup/action/boundary/observation
- logs show runtime behavior; comments explain why it matters

The module uses grouped functionality:

- one lesson enum (`AnnotationLesson`) containing all lesson entries
- one catalog (`LessonCatalog`) for indexing and coverage assertions
- one CLI (`SpringAnnotationStudyCli`) for `list`, `run`, `run-all`
