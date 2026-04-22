package com.example.javads.interview.demos;

import com.example.javads.interview.demos.custom.CustomBinarySearchTree;
import com.example.javads.interview.demos.custom.CustomBloomFilter;
import com.example.javads.interview.demos.custom.CustomCircularQueue;
import com.example.javads.interview.demos.custom.CustomDequeDoubly;
import com.example.javads.interview.demos.custom.CustomDynamicArray;
import com.example.javads.interview.demos.custom.CustomFenwickTree;
import com.example.javads.interview.demos.custom.CustomGraphAdjList;
import com.example.javads.interview.demos.custom.CustomHashMapChaining;
import com.example.javads.interview.demos.custom.CustomHashSet;
import com.example.javads.interview.demos.custom.CustomLruCache;
import com.example.javads.interview.demos.custom.CustomMergeSort;
import com.example.javads.interview.demos.custom.CustomMinHeap;
import com.example.javads.interview.demos.custom.CustomOpenAddressMap;
import com.example.javads.interview.demos.custom.CustomPriorityQueueMaxHeap;
import com.example.javads.interview.demos.custom.CustomQuickSort;
import com.example.javads.interview.demos.custom.CustomSegmentTree;
import com.example.javads.interview.demos.custom.CustomSinglyLinkedList;
import com.example.javads.interview.demos.custom.CustomStackArray;
import com.example.javads.interview.demos.custom.CustomTrie;
import com.example.javads.interview.demos.custom.CustomUnionFind;
import com.example.javads.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Lessons 21–40: runnable demos that exercise {@code Custom*} structures in {@link com.example.javads.interview.demos.custom}.
 *
 * <p>Implementations live in separate types so you can open one file per data structure while reading runs here.
 */
public final class DemoCustomStructures {

    private DemoCustomStructures() {}

    /**
     * Lesson 21: {@link CustomQuickSort} partition behavior.
     *
     * <p><b>Purpose:</b> show in-place reordering around a pivot.
     * <p><b>Role:</b> contrasts with stable {@link CustomMergeSort} in lesson 22.
     * <p><b>Demonstration:</b> logs array before/after {@link CustomQuickSort#sort(int[])}.
     */
    public static void l21(StudyContext ctx) {
        int[] arr = {9, 3, 7, 1, 5, 8, 2, 6, 4};
        ctx.log("Before CustomQuickSort: " + Arrays.toString(arr));
        CustomQuickSort.sort(arr);
        ctx.log("After  CustomQuickSort: " + Arrays.toString(arr));
    }

    /**
     * Lesson 22: {@link CustomMergeSort} stable merge passes.
     *
     * <p><b>Purpose:</b> show divide-and-conquer with scratch buffer.
     * <p><b>Role:</b> predictable O(n log n) vs quicksort worst case.
     * <p><b>Demonstration:</b> sorted output on a fixed input.
     */
    public static void l22(StudyContext ctx) {
        int[] arr = {10, 4, 2, 8, 6, 3, 1, 9, 7, 5};
        ctx.log("Before CustomMergeSort: " + Arrays.toString(arr));
        CustomMergeSort.sort(arr);
        ctx.log("After  CustomMergeSort: " + Arrays.toString(arr));
    }

    /**
     * Lesson 23: {@link CustomSinglyLinkedList} mutations.
     *
     * <p><b>Purpose:</b> prepend, append, delete first match.
     * <p><b>Demonstration:</b> materialized list via {@link CustomSinglyLinkedList#toList()}.
     */
    public static void l23(StudyContext ctx) {
        CustomSinglyLinkedList list = new CustomSinglyLinkedList();
        list.addLast(4);
        list.addLast(9);
        list.addFirst(1);
        list.deleteFirstMatch(9);
        ctx.log("CustomSinglyLinkedList state: " + list.toList());
    }

    /**
     * Lesson 24: {@link CustomStackArray} LIFO.
     *
     * <p><b>Demonstration:</b> push three values, peek, pop once, snapshot remainder.
     */
    public static void l24(StudyContext ctx) {
        CustomStackArray stack = new CustomStackArray(6);
        stack.push(11);
        stack.push(22);
        stack.push(33);
        ctx.log("peek before pop: " + stack.peek());
        ctx.log("pop: " + stack.pop());
        ctx.log("stack snapshot: " + stack.snapshot());
    }

    /**
     * Lesson 25: {@link CustomDynamicArray} growth.
     *
     * <p><b>Demonstration:</b> size vs capacity after repeated {@link CustomDynamicArray#add(int)}.
     */
    public static void l25(StudyContext ctx) {
        CustomDynamicArray arr = new CustomDynamicArray();
        for (int i = 1; i <= 10; i++) {
            arr.add(i * 3);
        }
        ctx.log("CustomDynamicArray size/capacity: " + arr.size() + "/" + arr.capacity());
        ctx.log("index 5 value: " + arr.get(5));
    }

    /**
     * Lesson 26: {@link CustomCircularQueue} bounded FIFO.
     *
     * <p><b>Demonstration:</b> wrap indices after {@link CustomCircularQueue#poll()}.
     */
    public static void l26(StudyContext ctx) {
        CustomCircularQueue q = new CustomCircularQueue(5);
        q.offer(5);
        q.offer(6);
        q.offer(7);
        q.poll();
        q.offer(8);
        q.offer(9);
        ctx.log("CustomCircularQueue snapshot: " + q.snapshot());
    }

    /**
     * Lesson 27: {@link CustomDequeDoubly} two-ended list.
     *
     * <p><b>Demonstration:</b> addFirst/addLast then removeLast.
     */
    public static void l27(StudyContext ctx) {
        CustomDequeDoubly dq = new CustomDequeDoubly();
        dq.addFirst(10);
        dq.addLast(20);
        dq.addFirst(5);
        dq.removeLast();
        ctx.log("CustomDequeDoubly snapshot: " + dq.snapshot());
    }

    /**
     * Lesson 28: {@link CustomMinHeap} extract order.
     *
     * <p><b>Demonstration:</b> repeated {@link CustomMinHeap#extractMin()} yields ascending sequence.
     */
    public static void l28(StudyContext ctx) {
        CustomMinHeap heap = new CustomMinHeap();
        for (int v : new int[] {9, 2, 8, 1, 6, 3}) {
            heap.add(v);
        }
        List<Integer> out = new ArrayList<>();
        while (!heap.isEmpty()) {
            out.add(heap.extractMin());
        }
        ctx.log("CustomMinHeap extraction order: " + out);
    }

    /**
     * Lesson 29: {@link CustomHashMapChaining} collisions in same bucket.
     *
     * <p><b>Demonstration:</b> keys 1, 9, 17 with table length 8 share bucket; update key 9.
     */
    public static void l29(StudyContext ctx) {
        CustomHashMapChaining map = new CustomHashMapChaining(8);
        map.put(1, 100);
        map.put(9, 900);
        map.put(17, 1700);
        map.put(9, 901);
        ctx.log("CustomHashMap get(9): " + map.get(9));
        map.remove(1);
        ctx.log("CustomHashMap contains key 1 after remove: " + map.containsKey(1));
    }

    /**
     * Lesson 30: {@link CustomHashSet} deduplication via map backend.
     */
    public static void l30(StudyContext ctx) {
        CustomHashSet set = new CustomHashSet(8);
        set.add(7);
        set.add(7);
        set.add(15);
        ctx.log("CustomHashSet contains 7: " + set.contains(7));
        ctx.log("CustomHashSet size: " + set.size());
    }

    /**
     * Lesson 31: {@link CustomBinarySearchTree} ordering and membership.
     */
    public static void l31(StudyContext ctx) {
        CustomBinarySearchTree bst = new CustomBinarySearchTree();
        for (int v : new int[] {8, 3, 10, 1, 6, 14, 4, 7, 13}) {
            bst.insert(v);
        }
        ctx.log("CustomBST inorder: " + bst.inorder());
        ctx.log("CustomBST contains 6: " + bst.contains(6));
    }

    /**
     * Lesson 32: {@link CustomTrie} prefix vs exact word.
     */
    public static void l32(StudyContext ctx) {
        CustomTrie trie = new CustomTrie();
        trie.insert("graph");
        trie.insert("grape");
        trie.insert("grow");
        ctx.log("CustomTrie contains 'graph': " + trie.contains("graph"));
        ctx.log("CustomTrie hasPrefix 'gra': " + trie.hasPrefix("gra"));
    }

    /**
     * Lesson 33: {@link CustomGraphAdjList} BFS shortest hop count.
     */
    public static void l33(StudyContext ctx) {
        CustomGraphAdjList g = new CustomGraphAdjList(6);
        g.addUndirectedEdge(0, 1);
        g.addUndirectedEdge(1, 2);
        g.addUndirectedEdge(2, 3);
        g.addUndirectedEdge(0, 4);
        g.addUndirectedEdge(4, 5);
        g.addUndirectedEdge(5, 3);
        ctx.log("CustomGraph shortest path 0->3: " + g.shortestPathLength(0, 3));
    }

    /**
     * Lesson 34: {@link CustomUnionFind} connectivity after unions.
     */
    public static void l34(StudyContext ctx) {
        CustomUnionFind uf = new CustomUnionFind(8);
        uf.union(1, 2);
        uf.union(2, 3);
        uf.union(5, 6);
        ctx.log("1 connected to 3: " + uf.connected(1, 3));
        ctx.log("1 connected to 6: " + uf.connected(1, 6));
    }

    /**
     * Lesson 35: {@link CustomLruCache} eviction of least-recently-used key.
     */
    public static void l35(StudyContext ctx) {
        CustomLruCache cache = new CustomLruCache(3);
        cache.put(1, 100);
        cache.put(2, 200);
        cache.put(3, 300);
        cache.get(1);
        cache.put(4, 400);
        ctx.log("CustomLRU contains 2 after eviction: " + (cache.get(2) != null));
        ctx.log("CustomLRU contains 1: " + (cache.get(1) != null));
    }

    /**
     * Lesson 36: {@link CustomSegmentTree} range sum vs point update.
     */
    public static void l36(StudyContext ctx) {
        int[] src = {2, 1, 5, 3, 4, 7, 6, 8};
        CustomSegmentTree st = new CustomSegmentTree(src);
        int before = st.rangeSum(2, 5);
        st.update(3, 10);
        int after = st.rangeSum(2, 5);
        ctx.log("CustomSegmentTree sum[2..5] before/after update: " + before + "/" + after);
    }

    /**
     * Lesson 37: {@link CustomFenwickTree} prefix and inclusive range sums.
     */
    public static void l37(StudyContext ctx) {
        CustomFenwickTree ft = new CustomFenwickTree(8);
        int[] vals = {2, 1, 5, 3, 4, 7, 6, 8};
        for (int i = 0; i < vals.length; i++) {
            ft.add(i + 1, vals[i]);
        }
        int prefix6 = ft.prefixSum(6);
        int range3to6 = ft.rangeSum(3, 6);
        ctx.log("CustomFenwick prefix(6): " + prefix6);
        ctx.log("CustomFenwick range(3,6): " + range3to6);
    }

    /**
     * Lesson 38: {@link CustomBloomFilter} possible false positive on non-members.
     */
    public static void l38(StudyContext ctx) {
        CustomBloomFilter bf = new CustomBloomFilter(128);
        bf.add("array");
        bf.add("graph");
        bf.add("queue");
        ctx.log("Bloom maybeContains('graph'): " + bf.maybeContains("graph"));
        ctx.log("Bloom maybeContains('stack'): " + bf.maybeContains("stack"));
    }

    /**
     * Lesson 39: {@link CustomOpenAddressMap} linear probing and tombstones after remove.
     */
    public static void l39(StudyContext ctx) {
        CustomOpenAddressMap map = new CustomOpenAddressMap(16);
        map.put(1, 10);
        map.put(17, 170);
        map.put(33, 330);
        ctx.log("OpenAddress get(17): " + map.get(17));
        map.remove(17);
        ctx.log("OpenAddress contains 17 after remove: " + map.containsKey(17));
    }

    /**
     * Lesson 40: {@link CustomPriorityQueueMaxHeap} top-k style polls.
     */
    public static void l40(StudyContext ctx) {
        CustomPriorityQueueMaxHeap pq = new CustomPriorityQueueMaxHeap();
        for (int v : new int[] {12, 7, 19, 3, 25, 4}) {
            pq.offer(v);
        }
        List<Integer> top = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            top.add(pq.poll());
        }
        ctx.log("CustomMaxHeap top-3 extraction: " + top);
    }
}
