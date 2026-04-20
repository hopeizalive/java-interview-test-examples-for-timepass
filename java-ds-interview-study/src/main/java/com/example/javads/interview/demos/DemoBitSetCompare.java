package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.BitSet;

/**
 * Packed {@link BitSet} vs plain {@code boolean[]} for dense boolean data.
 *
 * <p><b>Lessons:</b> 20.
 */
public final class DemoBitSetCompare {

    private DemoBitSetCompare() {}

    /**
     * Sets many bits and counts true bits using {@link BitSet} vs {@code boolean[]}.
     *
     * <p><b>Purpose:</b> show memory-oriented primitive for bit vectors (interviews mention “packed bits”).
     *
     * <p><b>Role:</b> same logical size; compare {@link BitSet#cardinality} vs manual loop.
     *
     * <p><b>Demonstration:</b> {@link BitSet} stores bits packed in long words; {@code boolean[]} is one byte per slot
     * (JVM dependent but conceptually sparser than one bit per flag in BitSet).
     */
    public static void l20(StudyContext ctx) {
        ctx.log("Interview question: BitSet vs boolean[] — when does BitSet win?");
        final int n = 100_000;
        BitSet bits = new BitSet(n);
        boolean[] flags = new boolean[n];
        for (int i = 0; i < n; i += 7) {
            bits.set(i);
            flags[i] = true;
        }
        /*
         * Timed sections: keep a trivial dependency on the result so the JVM does not delete the whole call as dead
         * code in aggressive micro-benchmarks (still not JMH).
         */
        int[] checksum = new int[1];
        long countBits = DemoSupport.nanos(() -> checksum[0] ^= bits.cardinality());
        long countArray = DemoSupport.nanos(() -> {
            int c = 0;
            for (boolean f : flags) {
                if (f) {
                    c++;
                }
            }
            checksum[0] ^= c;
        });
        ctx.log("Cardinality / count result (same data): BitSet=" + bits.cardinality());
        ctx.log("Count set bits among " + n + " slots (sparse pattern every 7th index):");
        DemoSupport.logNanos(ctx, "BitSet.cardinality()", countBits);
        DemoSupport.logNanos(ctx, "boolean[] linear scan", countArray);
        ctx.log("BitSet logical size (length-1 highest set): " + bits.length());
        ctx.log("boolean[] length field: " + flags.length);
    }
}
