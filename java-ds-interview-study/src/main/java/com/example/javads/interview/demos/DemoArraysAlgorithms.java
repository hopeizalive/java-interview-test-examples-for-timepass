package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * {@link Arrays} utilities: search, sort, parallel sort, string building.
 *
 * <p><b>Lessons:</b> 11, 12, 13.
 */
public final class DemoArraysAlgorithms {

    private DemoArraysAlgorithms() {}

    /**
     * Compares {@link Arrays#binarySearch(int[], int)} on a sorted array vs a simple linear scan.
     *
     * <p><b>Purpose:</b> reinforce precondition (sorted) and return-value encoding for insertion point.
     *
     * <p><b>Role:</b> Path A sorted binary search; Path B same target with linear scan on unsorted copy is wrong
     * conceptually—we instead show linear on sorted is O(n) vs binary O(log n) for many probes.
     *
     * <p><b>Demonstration:</b> binary search requires sorted data; negative return means “(-insertionPoint - 1)”.
     */
    public static void l11(StudyContext ctx) {
        ctx.log("Interview question: Arrays.binarySearch — when does it apply and what does a negative index mean?");
        int[] sorted = {2, 4, 6, 8, 10};
        int hit = Arrays.binarySearch(sorted, 6);
        int miss = Arrays.binarySearch(sorted, 7);
        ctx.log("binarySearch(sorted, 6) = " + hit);
        ctx.log("binarySearch(sorted, 7) = " + miss + " → insertion point would be " + (-miss - 1));
        int target = 6;
        int linearIndex = -1;
        for (int i = 0; i < sorted.length; i++) {
            if (sorted[i] == target) {
                linearIndex = i;
                break;
            }
        }
        ctx.log("Linear scan found 6 at index " + linearIndex + " (fine for tiny n; binary wins for large sorted).");
    }

    /**
     * Times {@link Arrays#sort} vs {@link Arrays#parallelSort} on large primitive arrays.
     *
     * <p><b>Purpose:</b> show parallel sort can win on big arrays but is not free (fork/join overhead).
     *
     * <p><b>Role:</b> duplicate random data for fair comparison; sort fresh copies each timing block.
     *
     * <p><b>Demonstration:</b> parallel sort helps large arrays; tiny arrays favor single-threaded sort.
     */
    public static void l12(StudyContext ctx) {
        ctx.log("Interview question: Arrays.sort vs Arrays.parallelSort — when would you use each?");
        final int n = 2_000_000;
        Random rnd = ThreadLocalRandom.current();
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = rnd.nextInt();
        }
        int[] copyA = Arrays.copyOf(data, n);
        int[] copyB = Arrays.copyOf(data, n);
        long serial = DemoSupport.nanos(() -> Arrays.sort(copyA));
        long parallel = DemoSupport.nanos(() -> Arrays.parallelSort(copyB));
        ctx.log("Sort " + n + " random ints (same seed distribution, separate copies):");
        DemoSupport.logNanos(ctx, "Arrays.sort", serial);
        DemoSupport.logNanos(ctx, "Arrays.parallelSort", parallel);
        ctx.log("Takeaway: parallelSort pays off for large primitive arrays; small arrays may be slower.");
    }

    /**
     * Builds a long string in a loop with {@code +=} vs {@link StringBuilder}.
     *
     * <p><b>Purpose:</b> classic JVM allocation / immutability interview question.
     *
     * <p><b>Role:</b> two timed paths building the same logical concatenation.
     *
     * <p><b>Demonstration:</b> repeated string concatenation creates many intermediate {@link String} objects; builder
     * amortizes growth.
     */
    public static void l13(StudyContext ctx) {
        ctx.log("Interview question: String += in a loop vs StringBuilder — what happens under the hood?");
        final int parts = 15_000;
        long concat = DemoSupport.nanos(() -> {
            String s = "";
            for (int i = 0; i < parts; i++) {
                s += "a";
            }
            if (s.length() != parts) {
                throw new AssertionError();
            }
        });
        long builder = DemoSupport.nanos(() -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts; i++) {
                sb.append('a');
            }
            if (sb.length() != parts) {
                throw new AssertionError();
            }
        });
        ctx.log("Append a single char " + parts + " times:");
        DemoSupport.logNanos(ctx, "String +=", concat);
        DemoSupport.logNanos(ctx, "StringBuilder", builder);
    }
}
