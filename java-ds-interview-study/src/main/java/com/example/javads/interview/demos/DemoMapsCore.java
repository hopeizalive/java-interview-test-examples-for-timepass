package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * {@link Map} implementations: hash tables, insertion-ordered linked hash, sorted tree, identity keys, sizing.
 *
 * <p><b>Lessons:</b> 2, 8, 9.
 */
public final class DemoMapsCore {

    private DemoMapsCore() {}

    /**
     * Prints iteration order for {@link HashMap}, {@link LinkedHashMap}, and {@link TreeMap} holding the same keys.
     *
     * <p><b>Purpose:</b> connect “which map preserves what order?” to interview answers.
     *
     * <p><b>Role:</b> purely behavioral (no timing); order is the observable.
     *
     * <p><b>Demonstration:</b> {@link HashMap} iteration order is undefined; {@link LinkedHashMap} follows insertion;
     * {@link TreeMap} follows key sort order; {@link TreeMap} rejects {@code null} keys under natural ordering.
     */
    public static void l02(StudyContext ctx) {
        ctx.log("Interview question: HashMap vs LinkedHashMap vs TreeMap — ordering and null keys?");
        Map<String, Integer> hash = new HashMap<>();
        Map<String, Integer> linked = new LinkedHashMap<>();
        Map<String, Integer> tree = new TreeMap<>();
        /*
         * Insert in a deliberate order that differs from lexicographic order so the three maps diverge visually.
         */
        String[] keys = {"gamma", "alpha", "beta"};
        for (String k : keys) {
            hash.put(k, 1);
            linked.put(k, 1);
            tree.put(k, 1);
        }
        ctx.log("Insertion order was: gamma, alpha, beta");
        ctx.log("HashMap key iteration (undefined order): " + String.join(", ", hash.keySet()));
        ctx.log("LinkedHashMap key iteration (insertion order): " + String.join(", ", linked.keySet()));
        ctx.log("TreeMap key iteration (sorted): " + String.join(", ", tree.keySet()));
        hash.put(null, 0);
        linked.put(null, 0);
        ctx.log("HashMap allows one null key: size=" + hash.size());
        ctx.log("LinkedHashMap allows one null key: size=" + linked.size());
        try {
            tree.put(null, 0);
            ctx.log("Unexpected: TreeMap accepted null key under natural ordering");
        } catch (NullPointerException ex) {
            ctx.log("TreeMap with natural ordering rejects null keys (NullPointerException).");
        }
    }

    /**
     * Contrasts default-sized {@link HashMap} vs a presized {@link HashMap} for a large known load.
     *
     * <p><b>Purpose:</b> show why {@code new HashMap<>(expectedSize)} appears in performance-sensitive code.
     *
     * <p><b>Role:</b> timed build of many entries twice (different initial capacities).
     *
     * <p><b>Demonstration:</b> resizing/rehashing has cost; presizing reduces resize events (effect varies with JVM and
     * load factor; still a common interview talking point).
     */
    public static void l08(StudyContext ctx) {
        ctx.log("Interview question: Why would you set initial capacity on a HashMap?");
        final int entries = 500_000;
        /*
         * Default constructor starts small and grows by doubling buckets as load crosses thresholds.
         */
        long defaultCap = DemoSupport.nanos(() -> {
            Map<Integer, Integer> m = new HashMap<>();
            for (int i = 0; i < entries; i++) {
                m.put(i, i);
            }
        });
        /*
         * Passing expected size lets the implementation choose bucket count up front (still subject to load factor).
         */
        long presized = DemoSupport.nanos(() -> {
            Map<Integer, Integer> m = new HashMap<>(entries);
            for (int i = 0; i < entries; i++) {
                m.put(i, i);
            }
        });
        ctx.log("Insert " + entries + " distinct Integer keys (same JVM run; timings noisy):");
        DemoSupport.logNanos(ctx, "new HashMap<>()", defaultCap);
        DemoSupport.logNanos(ctx, "new HashMap<>(expectedSize)", presized);
        ctx.log("Takeaway: presizing can reduce resize churn when you know approximate final size.");
    }

    /**
     * Compares {@link IdentityHashMap} (reference identity) with {@link HashMap} ({@code equals}/{@code hashCode}).
     *
     * <p><b>Purpose:</b> disambiguate “key equality” questions that trip candidates up.
     *
     * <p><b>Role:</b> two paths with equal {@link String} content but different object identities.
     *
     * <p><b>Demonstration:</b> {@link IdentityHashMap} uses {@code ==}; {@link HashMap} merges keys that are {@code
     * equals}.
     */
    public static void l09(StudyContext ctx) {
        ctx.log("Interview question: When does IdentityHashMap differ from HashMap?");
        String a = new String("key");
        String b = new String("key");
        ctx.log("a.equals(b) == " + a.equals(b) + " but (a == b) == " + (a == b));
        Map<String, Integer> hash = new HashMap<>();
        hash.put(a, 1);
        hash.put(b, 2);
        ctx.log("HashMap size after put(a) then put(b): " + hash.size() + " value=" + hash.get(a));
        Map<String, Integer> id = new IdentityHashMap<>();
        id.put(a, 1);
        id.put(b, 2);
        ctx.log("IdentityHashMap size after put(a) then put(b): " + id.size());
        ctx.log("Takeaway: IdentityHashMap is for reference-sensitive graphs/symbol tables, not normal business keys.");
    }
}
