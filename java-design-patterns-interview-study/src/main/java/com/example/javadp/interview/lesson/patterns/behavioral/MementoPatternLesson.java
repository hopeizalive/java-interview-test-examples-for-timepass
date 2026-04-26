package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * GoF Memento: capture internal state for later restore without breaking encapsulation of the originator.
 */
public final class MementoPatternLesson {

    private MementoPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Text editor undo stacks",
                "Each command pushes a memento (snapshot or inverse op) so users revert arbitrary edits safely.",
                "Caretaker holds stack; originator knows how to apply/restore mementos.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Database transaction savepoints",
                "SAVEPOINT captures a recoverable marker; ROLLBACK TO restores prior state without exposing internals.",
                "Infrastructure memento with transactional semantics.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Game checkpoints",
                "Serialized world state (opaque blob) lets players resume; game engine internals stay hidden.",
                "Wide vs narrow memento interface tradeoff.");

        ctx.log("--- Java core tie-in ---");
        java.util.Calendar cal = java.util.Calendar.getInstance(java.util.Locale.ROOT);
        cal.set(java.util.Calendar.YEAR, 2000);
        java.util.Calendar snapshot = (java.util.Calendar) cal.clone();
        cal.set(java.util.Calendar.YEAR, 2024);
        ctx.log("java.util.Calendar.clone() snapshot year=" + snapshot.get(java.util.Calendar.YEAR)
                + " while live cal year=" + cal.get(java.util.Calendar.YEAR));

        PatternLessonHeader.print(
                ctx,
                "Memento",
                "GoF Behavioral",
                "Snapshot originator state via a narrow memento; caretaker stores opaque tokens.",
                "Editor types, save(), types more, restore() returns to saved snapshot.");
        Editor editor = new Editor();
        Deque<Editor.Memento> history = new ArrayDeque<>();
        editor.append("hello");
        history.push(editor.save());
        editor.append(" world");
        ctx.log("Before undo: " + editor.text());
        editor.restore(history.pop());
        ctx.log("After undo: " + editor.text());
    }

    private static final class Editor {
        private StringBuilder buffer = new StringBuilder();

        void append(String s) {
            buffer.append(s);
        }

        String text() {
            return buffer.toString();
        }

        Memento save() {
            return new Memento(buffer.toString());
        }

        void restore(Memento m) {
            this.buffer = new StringBuilder(m.state());
        }

        private record Memento(String state) {}
    }
}
