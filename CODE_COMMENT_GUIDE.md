# Code Comment Guide for Study Modules

This guide defines how to write **teaching-friendly comments** in interview-study modules so readers can understand:

1. what the code does,
2. why this example exists,
3. what to observe when it runs,
4. what interview takeaway to remember.

Use this guide for new modules and when updating existing lessons.

---

## Core Principle

For study code, comments are not decoration. They are part of the lesson content.

- Keep code runnable and concise.
- Add comments where they improve understanding.
- Explain **intent and story**, not trivial syntax.

Bad comment:
- `i++; // increment i`

Good comment:
- `// Increment shared counter under lock so read-modify-write is atomic across threads.`

---

## Required Comment Layers

Every lesson method should have these layers.

### 1) Class-level Javadoc

Each `Demo*` class should state:
- lesson number range,
- topic domain,
- what the reader learns from this class.

Example:

```java
/**
 * Executor and scheduling patterns (lessons 4-12).
 * Focus: pool sizing, scheduling semantics, and batch APIs.
 */
```

### 2) Method-level Javadoc (for every `lXX`)

Each lesson entry method should include:
- **Purpose**: what scenario/API is exercised,
- **Role**: where it fits in the lesson sequence,
- **Demonstration**: what behavior proves the point.

Template:

```java
/**
 * Lesson N: <short title>.
 *
 * <p><b>Purpose:</b> ...
 * <p><b>Role:</b> ...
 * <p><b>Demonstration:</b> ...
 */
```

### 3) Execution-story comments inside method body

Inside the method, use phase comments to narrate runtime flow:
- setup,
- concurrent action,
- sync/wait boundary,
- observed result.

Recommended markers:
- `// Execution story step 1: ...`
- `// Story phase A: ...`

---

## Execution Story Pattern (Recommended)

For most lessons, follow this structure:

1. **Setup**
   - Create inputs, queues, locks, executors, counters.
   - Comment what environment is being modeled.

2. **Action**
   - Submit tasks / start threads / schedule work.
   - Comment what races, contention, or async flow is expected.

3. **Boundary**
   - `join`, `get`, `invokeAll`, `await`, `awaitTermination`, etc.
   - Comment why this boundary is needed for deterministic observation.

4. **Observation**
   - Log measured/observed behavior.
   - Comment expected vs possible outcomes.

5. **Takeaway**
   - Final log or comment mapping behavior to interview talking point.

---

## Where Comments Are Mandatory

Add explicit comments for:
- concurrency boundaries (`join`, `lock`, `unlock`, `volatile`, `await`, `cancel`),
- non-obvious loops (poll loops, spin loops, backpressure behavior),
- any timing-related code (`System.nanoTime`, sleep windows),
- cancellation/error paths,
- differences between two compared approaches in same lesson.

---

## Where Comments Should Be Avoided

Do not add comments for:
- obvious Java syntax,
- simple getters/setters,
- repeating what a method name already says.

If a comment can be removed without losing understanding, remove it.

---

## Logging + Comments Together

Logs show runtime behavior; comments explain why that behavior matters.

- Use logs for **what happened**.
- Use comments for **why we do this step** and **what to notice**.

Example:

```java
// Story step 2: cancel with interruption request so blocking sleep can exit early.
boolean cancelled = f.cancel(true);
ctx.log("  cancel returned: " + cancelled);
```

---

## Checklist Before Finishing a Lesson

- [ ] Class has Javadoc with lesson scope.
- [ ] `lXX` method has Purpose/Role/Demonstration Javadoc.
- [ ] Method body has execution-story comments at key phases.
- [ ] Logs and comments are aligned (no contradiction).
- [ ] No noisy comments that restate trivial code.
- [ ] Reader can explain the lesson by reading code top-to-bottom once.

---

## New Module Authoring Rule

When creating a new study module:

1. Start from this guide.
2. Keep one `STUDY_GUIDE.md` per module.
3. Ensure each lesson method is self-explanatory without external context.
4. Prefer fewer but clearer examples with strong comments over many shallow examples.

This keeps modules readable for students and reviewers who are new to concurrency or data structures.
