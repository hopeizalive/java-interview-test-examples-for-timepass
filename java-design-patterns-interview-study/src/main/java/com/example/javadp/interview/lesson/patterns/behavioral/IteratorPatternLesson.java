package com.example.javadp.interview.lesson.patterns.behavioral;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * GoF Iterator: sequential access over an aggregate without exposing internal representation.
 */
public final class IteratorPatternLesson {

    private IteratorPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.util.Collection iterators",
                "ArrayList vs LinkedList hide different pointer arithmetic behind Iterator.hasNext/next.",
                "Fail-fast iterators detect concurrent modification - tradeoff of internal representation.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "java.sql.ResultSet",
                "Cursor traversal abstracts row storage; client code walks forward without knowing page buffers.",
                "External iterator over a database-backed aggregate.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "File tree walks",
                "java.nio.file.FileVisitor / DirectoryStream provide controlled traversal policies over filesystem trees.",
                "Different iterator policies: depth-first vs breadth-first visitors.");

        ctx.log("--- Java core tie-in ---");
        java.util.Iterator<String> it = java.util.List.of("a", "b").iterator();
        ctx.log("java.util.List.iterator hasNext=" + it.hasNext() + " next=" + it.next());

        PatternLessonHeader.print(
                ctx,
                "Iterator",
                "GoF Behavioral",
                "Separate traversal from the collection; supports multiple traversal policies.",
                "BookShelf hides array index; foreach uses custom Iterator.");
        BookShelf shelf = new BookShelf();
        shelf.add("Clean Code");
        shelf.add("Effective Java");
        for (String title : shelf) {
            ctx.log("Book: " + title);
        }
    }

    private static final class BookShelf implements Iterable<String> {
        private final List<String> titles = new ArrayList<>();

        void add(String t) {
            titles.add(t);
        }

        @Override
        public Iterator<String> iterator() {
            return titles.iterator();
        }
    }
}
