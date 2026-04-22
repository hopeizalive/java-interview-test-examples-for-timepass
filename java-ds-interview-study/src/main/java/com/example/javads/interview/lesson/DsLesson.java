package com.example.javads.interview.lesson;

import com.example.javads.interview.demos.*;
import com.example.javads.interview.study.StudyContext;
import com.example.javads.interview.study.StudyLesson;

/** Forty Java collections / DSA comparison lessons; runnable from {@link com.example.javads.interview.cli.DsStudyCli}. */
public enum DsLesson implements StudyLesson {

    L01(1, "ArrayList vs LinkedList: tail append, prepend, random get."),
    L02(2, "HashMap vs LinkedHashMap vs TreeMap: order and null keys."),
    L03(3, "HashSet vs LinkedHashSet vs TreeSet; equals/hashCode pitfall."),
    L04(4, "ArrayDeque vs LinkedList as Deque (mixed addLast/pollFirst)."),
    L05(5, "PriorityQueue vs TreeSet: duplicates and poll order."),
    L06(6, "ConcurrentHashMap vs synchronized HashMap: concurrent reads."),
    L07(7, "List.of vs unmodifiableList vs defensive ArrayList copy."),
    L08(8, "HashMap presized constructor vs default (resize churn)."),
    L09(9, "IdentityHashMap vs HashMap (== vs equals keys)."),
    L10(10, "EnumMap vs HashMap<enum>."),
    L11(11, "Arrays.binarySearch vs linear scan (sorted precondition)."),
    L12(12, "Arrays.sort vs Arrays.parallelSort."),
    L13(13, "String += in loop vs StringBuilder."),
    L14(14, "Collections.binarySearch on ArrayList vs LinkedList."),
    L15(15, "Iterator.remove vs structural remove during for-each."),
    L16(16, "ArrayDeque stack vs java.util.Stack."),
    L17(17, "TreeSet ceiling vs scanning sorted ArrayList."),
    L18(18, "CopyOnWriteArrayList vs synchronized ArrayList (read-heavy)."),
    L19(19, "ArrayBlockingQueue vs LinkedBlockingQueue (bounded)."),
    L20(20, "BitSet vs boolean[] (cardinality / scan)."),
    L21(21, "CustomQuickSort: in-place partition quick sort."),
    L22(22, "CustomMergeSort: stable divide-and-conquer sort."),
    L23(23, "CustomSinglyLinkedList: add, prepend, delete."),
    L24(24, "CustomStackArray: push/pop with fixed-capacity array."),
    L25(25, "CustomDynamicArray: amortized growth and indexed access."),
    L26(26, "CustomCircularQueue: FIFO with ring-buffer pointers."),
    L27(27, "CustomDequeDoubly: add/remove from both ends."),
    L28(28, "CustomMinHeap: insert + extractMin ordering."),
    L29(29, "CustomHashMapChaining: put/get/remove and collisions."),
    L30(30, "CustomHashSet: uniqueness via CustomHashMapChaining."),
    L31(31, "CustomBinarySearchTree: insert/search/inorder traversal."),
    L32(32, "CustomTrie: prefix search and full-word checks."),
    L33(33, "CustomGraphAdjList BFS shortest unweighted path."),
    L34(34, "CustomUnionFind: connectivity + path compression."),
    L35(35, "CustomLruCache: hash map + doubly linked list."),
    L36(36, "CustomSegmentTree: range sum query + point update."),
    L37(37, "CustomFenwickTree: prefix sums and updates."),
    L38(38, "CustomBloomFilter: probabilistic membership checks."),
    L39(39, "CustomOpenAddressMap: linear probing behavior."),
    L40(40, "CustomPriorityQueueMaxHeap: top-k extraction pattern.");

    public static final int EXPECTED_LESSON_COUNT = 40;

    private final int number;
    private final String title;

    DsLesson(int number, String title) {
        this.number = number;
        this.title = title;
    }

    @Override
    public int number() {
        return number;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public void run(StudyContext ctx) throws Exception {
        switch (this) {
            case L01 -> DemoListsAndDeques.l01(ctx);
            case L02 -> DemoMapsCore.l02(ctx);
            case L03 -> DemoSetsPriority.l03(ctx);
            case L04 -> DemoListsAndDeques.l04(ctx);
            case L05 -> DemoSetsPriority.l05(ctx);
            case L06 -> DemoConcurrencyMaps.l06(ctx);
            case L07 -> DemoImmutableLists.l07(ctx);
            case L08 -> DemoMapsCore.l08(ctx);
            case L09 -> DemoMapsCore.l09(ctx);
            case L10 -> DemoMapsEnum.l10(ctx);
            case L11 -> DemoArraysAlgorithms.l11(ctx);
            case L12 -> DemoArraysAlgorithms.l12(ctx);
            case L13 -> DemoArraysAlgorithms.l13(ctx);
            case L14 -> DemoSearchLists.l14(ctx);
            case L15 -> DemoIterationRemoval.l15(ctx);
            case L16 -> DemoStackLegacy.l16(ctx);
            case L17 -> DemoNavigableTree.l17(ctx);
            case L18 -> DemoCopyOnWriteCompare.l18(ctx);
            case L19 -> DemoBlockingQueuesCompare.l19(ctx);
            case L20 -> DemoBitSetCompare.l20(ctx);
            case L21 -> DemoCustomStructures.l21(ctx);
            case L22 -> DemoCustomStructures.l22(ctx);
            case L23 -> DemoCustomStructures.l23(ctx);
            case L24 -> DemoCustomStructures.l24(ctx);
            case L25 -> DemoCustomStructures.l25(ctx);
            case L26 -> DemoCustomStructures.l26(ctx);
            case L27 -> DemoCustomStructures.l27(ctx);
            case L28 -> DemoCustomStructures.l28(ctx);
            case L29 -> DemoCustomStructures.l29(ctx);
            case L30 -> DemoCustomStructures.l30(ctx);
            case L31 -> DemoCustomStructures.l31(ctx);
            case L32 -> DemoCustomStructures.l32(ctx);
            case L33 -> DemoCustomStructures.l33(ctx);
            case L34 -> DemoCustomStructures.l34(ctx);
            case L35 -> DemoCustomStructures.l35(ctx);
            case L36 -> DemoCustomStructures.l36(ctx);
            case L37 -> DemoCustomStructures.l37(ctx);
            case L38 -> DemoCustomStructures.l38(ctx);
            case L39 -> DemoCustomStructures.l39(ctx);
            case L40 -> DemoCustomStructures.l40(ctx);
        }
    }
}
