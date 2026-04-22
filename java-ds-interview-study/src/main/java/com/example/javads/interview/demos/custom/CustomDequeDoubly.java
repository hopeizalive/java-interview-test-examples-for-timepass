package com.example.javads.interview.demos.custom;

import java.util.ArrayList;
import java.util.List;

/**
 * Doubly linked deque: O(1) add/remove at both ends (conceptually like {@link java.util.LinkedList} deque use).
 */
public final class CustomDequeDoubly {

    private DNode head;
    private DNode tail;

    public void addFirst(int v) {
        DNode n = new DNode(v);
        if (head == null) {
            head = tail = n;
            return;
        }
        n.next = head;
        head.prev = n;
        head = n;
    }

    public void addLast(int v) {
        DNode n = new DNode(v);
        if (tail == null) {
            head = tail = n;
            return;
        }
        tail.next = n;
        n.prev = tail;
        tail = n;
    }

    /** Removes tail value or null if empty. */
    public Integer removeLast() {
        if (tail == null) {
            return null;
        }
        int v = tail.value;
        tail = tail.prev;
        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        return v;
    }

    public List<Integer> snapshot() {
        List<Integer> out = new ArrayList<>();
        for (DNode cur = head; cur != null; cur = cur.next) {
            out.add(cur.value);
        }
        return out;
    }

    private static final class DNode {
        final int value;
        DNode prev;
        DNode next;

        DNode(int value) {
            this.value = value;
        }
    }
}
