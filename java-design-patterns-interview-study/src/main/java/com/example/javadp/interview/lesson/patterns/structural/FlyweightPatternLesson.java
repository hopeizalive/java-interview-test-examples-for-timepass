package com.example.javadp.interview.lesson.patterns.structural;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.HashMap;
import java.util.Map;

/**
 * GoF Flyweight: share intrinsic immutable state across many instances; pass extrinsic context per use.
 */
public final class FlyweightPatternLesson {

    private FlyweightPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "java.lang.Integer#valueOf caching",
                "Small integers are cached flyweights; autoboxing reuses instances for common values.",
                "Intrinsic: int value; extrinsic: how you format or compare in a specific locale.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "String interning",
                "Literal strings and intern() can deduplicate identical sequences in the constant pool.",
                "Caution: intern misuse can blow metaspace - still a canonical flyweight story.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "Game rendering glyph atlases",
                "One GPU texture (atlas) stores letter bitmaps; each draw call passes position/color as extrinsic state.",
                "Maps directly to our LetterGlyph + draw(x) demo.");

        ctx.log("--- Java core tie-in ---");
        Integer a = Integer.valueOf(127);
        Integer b = Integer.valueOf(127);
        Integer x = Integer.valueOf(200);
        Integer y = Integer.valueOf(200);
        ctx.log("Integer.valueOf cache in default range: 127 same instance? " + (a == b) + "; 200 same instance? " + (x == y));

        PatternLessonHeader.print(
                ctx,
                "Flyweight",
                "GoF Structural",
                "Reuse immutable intrinsic data (glyph bitmap) while extrinsic position is passed per draw call.",
                "GlyphFactory returns same flyweight instance for 'A'; draw uses different x coordinates.");
        GlyphFactory factory = new GlyphFactory();
        Glyph a1 = factory.glyphFor('A');
        Glyph a2 = factory.glyphFor('A');
        ctx.log("Same flyweight for A? " + (a1 == a2));
        ctx.log(a1.draw(10));
        ctx.log(a1.draw(40));
    }

    private interface Glyph {
        String draw(int x);
    }

    private static final class GlyphFactory {
        private final Map<Character, Glyph> cache = new HashMap<>();

        Glyph glyphFor(char c) {
            return cache.computeIfAbsent(c, LetterGlyph::new);
        }
    }

    private record LetterGlyph(char symbol) implements Glyph {
        @Override
        public String draw(int x) {
            return "draw '" + symbol + "' at x=" + x;
        }
    }
}
