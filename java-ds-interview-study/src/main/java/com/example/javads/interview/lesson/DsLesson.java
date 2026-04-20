package com.example.javads.interview.lesson;

import com.example.javads.interview.demos.DemoArraysAlgorithms;
import com.example.javads.interview.demos.DemoBitSetCompare;
import com.example.javads.interview.demos.DemoBlockingQueuesCompare;
import com.example.javads.interview.demos.DemoConcurrencyMaps;
import com.example.javads.interview.demos.DemoCopyOnWriteCompare;
import com.example.javads.interview.demos.DemoImmutableLists;
import com.example.javads.interview.demos.DemoIterationRemoval;
import com.example.javads.interview.demos.DemoListsAndDeques;
import com.example.javads.interview.demos.DemoMapsCore;
import com.example.javads.interview.demos.DemoMapsEnum;
import com.example.javads.interview.demos.DemoNavigableTree;
import com.example.javads.interview.demos.DemoSearchLists;
import com.example.javads.interview.demos.DemoSetsPriority;
import com.example.javads.interview.demos.DemoStackLegacy;
import com.example.javads.interview.study.StudyContext;
import com.example.javads.interview.study.StudyLesson;

/** Twenty Java collections / DSA comparison lessons; runnable from {@link com.example.javads.interview.cli.DsStudyCli}. */
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
    L20(20, "BitSet vs boolean[] (cardinality / scan).");

    public static final int EXPECTED_LESSON_COUNT = 20;

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
        }
    }
}
