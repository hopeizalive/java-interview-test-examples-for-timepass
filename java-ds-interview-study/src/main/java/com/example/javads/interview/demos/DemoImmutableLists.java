package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Factory-style unmodifiable lists vs defensive copies.
 *
 * <p><b>Lessons:</b> 7.
 */
public final class DemoImmutableLists {

    private DemoImmutableLists() {}

    /**
     * Contrasts {@link List#of}, {@link Collections#unmodifiableList}, and copying into a new {@link ArrayList}.
     *
     * <p><b>Purpose:</b> show aliasing traps interviewers love.
     *
     * <p><b>Role:</b> three paths: true immutable factory list, view wrapper, independent copy.
     *
     * <p><b>Demonstration:</b> mutating the backing list changes the view; {@link List#of} rejects structural changes;
     * copy isolates callers.
     */
    public static void l07(StudyContext ctx) {
        ctx.log("Interview question: List.of vs Collections.unmodifiableList vs new ArrayList<>(copy)?");
        List<String> backing = new ArrayList<>(List.of("a", "b"));
        List<String> view = Collections.unmodifiableList(backing);
        ctx.log("Backed unmodifiable view before backing.add: " + view);
        backing.add("c");
        ctx.log("After mutating backing list, same 'unmodifiable' view sees: " + view);
        try {
            view.add("x");
            ctx.log("Unexpected: view.add succeeded");
        } catch (UnsupportedOperationException ex) {
            ctx.log("view.add throws UnsupportedOperationException (view is unmodifiable from caller side).");
        }
        try {
            List<String> factory = List.of("x", "y");
            factory.add("z");
            ctx.log("Unexpected: List.of allowed add");
        } catch (UnsupportedOperationException ex) {
            ctx.log("List.of list rejects add (immutable factory list).");
        }
        List<String> source = new ArrayList<>(List.of("p", "q"));
        List<String> copy = new ArrayList<>(source);
        source.add("r");
        ctx.log("After source.add, independent copy stays: " + copy);
    }
}
