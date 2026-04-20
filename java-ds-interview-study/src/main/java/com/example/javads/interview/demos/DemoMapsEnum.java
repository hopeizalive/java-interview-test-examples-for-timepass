package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link EnumMap} vs generic {@link HashMap} with enum keys.
 *
 * <p><b>Lessons:</b> 10.
 */
public final class DemoMapsEnum {

    /** Example enum keys for {@link EnumMap} demos. */
    private enum Tier {
        FREE,
        PRO,
        ENTERPRISE
    }

    private DemoMapsEnum() {}

    /**
     * Contrasts {@link EnumMap} with {@link HashMap} when keys are always enum constants.
     *
     * <p><b>Purpose:</b> show the specialized map the JDK offers for enum-keyed tables.
     *
     * <p><b>Role:</b> same logical operations on two map types; emphasize API and ordering guarantees.
     *
     * <p><b>Demonstration:</b> {@link EnumMap} iterates in enum declaration order, rejects null keys, and (internally)
     * uses a compact array indexed by ordinal—interview answer: faster and more memory-efficient than a generic hash
     * table for dense enum key sets.
     */
    public static void l10(StudyContext ctx) {
        ctx.log("Interview question: EnumMap vs HashMap<YourEnum, ?> — why prefer EnumMap?");
        Map<Tier, String> hash = new HashMap<>();
        hash.put(Tier.PRO, "mid");
        hash.put(Tier.FREE, "low");
        hash.put(Tier.ENTERPRISE, "high");
        Map<Tier, String> en = new EnumMap<>(Tier.class);
        en.put(Tier.PRO, "mid");
        en.put(Tier.FREE, "low");
        en.put(Tier.ENTERPRISE, "high");
        ctx.log("HashMap entry iteration order (undefined):");
        hash.forEach((k, v) -> ctx.log("  " + k + " -> " + v));
        ctx.log("EnumMap entry iteration order (enum declaration order):");
        en.forEach((k, v) -> ctx.log("  " + k + " -> " + v));
        try {
            en.put(null, "x");
            ctx.log("Unexpected: EnumMap accepted null key");
        } catch (NullPointerException ex) {
            ctx.log("EnumMap rejects null keys (NullPointerException).");
        }
        ctx.log("Takeaway: if every key is an enum, EnumMap is the type-safe, fast default.");
    }
}
