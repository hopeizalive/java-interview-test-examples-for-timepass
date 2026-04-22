package com.example.javads.interview.demos.custom;

import java.util.ArrayList;
import java.util.List;

/**
 * Singly linked list of ints for teaching insert/delete at ends (not {@link java.util.LinkedList}).
 *
 * <p><b>Tradeoffs:</b> prepend O(1); append O(n) without tail pointer; delete first match O(n).
 */
public final class CustomSinglyLinkedList {

    /** Sentinel not used; classic head-only list. */
    private Node head;

    /** Appends value at tail (walks from head). */
    public void addLast(int value) {
        Node n = new Node(value);
        if (head == null) {
            head = n;
            return;
        }
        Node cur = head;
        while (cur.next != null) {
            cur = cur.next;
        }
        cur.next = n;
    }

    /** Prepends value at head in O(1). */
    public void addFirst(int value) {
        Node n = new Node(value);
        n.next = head;
        head = n;
    }

    /**
     * Removes first node whose {@code value} matches; returns whether a node was removed.
     */
    public boolean deleteFirstMatch(int value) {
        if (head == null) {
            return false;
        }
        if (head.value == value) {
            head = head.next;
            return true;
        }
        Node cur = head;
        while (cur.next != null && cur.next.value != value) {
            cur = cur.next;
        }
        if (cur.next == null) {
            return false;
        }
        // Skip matched node by relinking predecessor.
        cur.next = cur.next.next;
        return true;
    }

    /** Materializes list order for logging (O(n) copy). */
    public List<Integer> toList() {
        List<Integer> out = new ArrayList<>();
        for (Node cur = head; cur != null; cur = cur.next) {
            out.add(cur.value);
        }
        return out;
    }

    private static final class Node {
        final int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }
}
