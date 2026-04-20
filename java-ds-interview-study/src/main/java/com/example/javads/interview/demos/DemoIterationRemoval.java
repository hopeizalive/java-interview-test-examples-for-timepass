package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Safe removal during iteration vs {@link java.util.ConcurrentModificationException}.
 *
 * <p><b>Lessons:</b> 15.
 */
public final class DemoIterationRemoval {

    private DemoIterationRemoval() {}

    /**
     * Removes selected elements with {@link Iterator#remove()} vs invalid structural remove during enhanced for-loop.
     *
     * <p><b>Purpose:</b> demonstrate fail-fast iterators on non-concurrent {@link ArrayList}.
     *
     * <p><b>Role:</b> Path A correct pattern; Path B classic mistake.
     *
     * <p><b>Demonstration:</b> {@link Iterator#remove} keeps iterator state consistent; {@code list.remove} inside
     * for-each throws {@link java.util.ConcurrentModificationException}.
     */
    public static void l15(StudyContext ctx) {
        ctx.log("Interview question: How do you remove items while iterating a non-thread-safe List?");
        List<Integer> safe = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Iterator<Integer> it = safe.iterator();
        while (it.hasNext()) {
            Integer v = it.next();
            if (v % 2 == 0) {
                it.remove();
            }
        }
        ctx.log("After Iterator.remove of evens: " + safe);
        List<Integer> bad = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        try {
            for (Integer v : bad) {
                if (v % 2 == 0) {
                    bad.remove(v);
                }
            }
            ctx.log("Unexpected: structural remove in for-each succeeded");
        } catch (java.util.ConcurrentModificationException ex) {
            ctx.log("for-each + Collection.remove → ConcurrentModificationException (fail-fast iterator).");
        }
    }
}
