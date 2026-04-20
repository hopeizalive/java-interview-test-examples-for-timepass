package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * Modern {@link ArrayDeque} stack usage vs legacy {@link Stack}.
 *
 * <p><b>Lessons:</b> 16.
 */
public final class DemoStackLegacy {

    private DemoStackLegacy() {}

    /**
     * Exercises LIFO operations on {@link ArrayDeque} vs {@link Stack}.
     *
     * <p><b>Purpose:</b> answer “what should I use for a stack in Java?”
     *
     * <p><b>Role:</b> same push/pop sequence on both types; comment on design of {@link Stack} extending {@link
     * java.util.Vector}.
     *
     * <p><b>Demonstration:</b> {@link Stack} inherits synchronized {@link java.util.Vector} methods— coarse locking and
     * odd API; {@link ArrayDeque} is the recommended deque/stack replacement.
     */
    public static void l16(StudyContext ctx) {
        ctx.log("Interview question: Stack vs ArrayDeque for a stack?");
        Deque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 5; i++) {
            ad.push(i);
        }
        StringBuilder outAd = new StringBuilder("ArrayDeque pop order: ");
        while (!ad.isEmpty()) {
            outAd.append(ad.pop()).append(' ');
        }
        ctx.log(outAd.toString().trim());
        Stack<Integer> st = new Stack<>();
        for (int i = 0; i < 5; i++) {
            st.push(i);
        }
        StringBuilder outSt = new StringBuilder("java.util.Stack pop order: ");
        while (!st.isEmpty()) {
            outSt.append(st.pop()).append(' ');
        }
        ctx.log(outSt.toString().trim());
        ctx.log("Takeaway: Stack extends Vector (synchronized, legacy); prefer Deque(ArrayDeque) for LIFO.");
    }
}
