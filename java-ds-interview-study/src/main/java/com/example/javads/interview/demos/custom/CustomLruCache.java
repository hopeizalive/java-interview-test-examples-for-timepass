package com.example.javads.interview.demos.custom;

import java.util.ArrayList;
import java.util.List;

/**
 * LRU cache with fixed capacity: O(1) intended via hash map + doubly linked list order.
 *
 * <p><b>Teaching note:</b> this demo maps key → {@link System#identityHashCode(Object)} of list node and keeps a
 * {@link List} of nodes for reverse lookup. Production code uses direct node references in the map or a dedicated
 * entry type—this version keeps dependencies minimal for reading.
 */
public final class CustomLruCache {

    private final int capacity;
    /** Key → identity hash of the DNode instance (demo indirection, not production pattern). */
    private final CustomOpenAddressMap index;
    private final DNode head = new DNode(-1, -1);
    private final DNode tail = new DNode(-1, -1);
    /** Holds node references so we can resolve identityHashCode back to node (linear scan; OK for tiny demos). */
    private final List<DNode> refs = new ArrayList<>();

    public CustomLruCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        this.index = new CustomOpenAddressMap(Math.max(16, capacity * 4));
        head.next = tail;
        tail.prev = head;
    }

    public Integer get(int key) {
        DNode node = nodeFromKey(key);
        if (node == null) {
            return null;
        }
        moveToFront(node);
        return node.value;
    }

    public void put(int key, int value) {
        DNode existing = nodeFromKey(key);
        if (existing != null) {
            existing.value = value;
            moveToFront(existing);
            return;
        }
        // Evict LRU (just before tail sentinel) when at capacity.
        if (index.size() == capacity) {
            DNode victim = tail.prev;
            detach(victim);
            index.remove(victim.key);
            refs.remove(victim);
        }
        DNode n = new DNode(key, value);
        addAfterHead(n);
        index.put(key, System.identityHashCode(n));
        refs.add(n);
    }

    private DNode nodeFromKey(int key) {
        Integer id = index.get(key);
        if (id == null) {
            return null;
        }
        for (DNode n : refs) {
            if (n.key == key && System.identityHashCode(n) == id) {
                return n;
            }
        }
        return null;
    }

    private void moveToFront(DNode n) {
        detach(n);
        addAfterHead(n);
    }

    private void addAfterHead(DNode n) {
        n.next = head.next;
        n.prev = head;
        head.next.prev = n;
        head.next = n;
    }

    private void detach(DNode n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
    }

    private static final class DNode {
        final int key;
        int value;
        DNode prev;
        DNode next;

        DNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
