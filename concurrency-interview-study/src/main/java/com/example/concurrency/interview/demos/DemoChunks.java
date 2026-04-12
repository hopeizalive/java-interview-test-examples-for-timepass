package com.example.concurrency.interview.demos;

import com.example.concurrency.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public final class DemoChunks {

    private DemoChunks() {}

    /** Lesson 58: chunk merge + parallelStream notes (keeps lessons 59–60 free for Kafka). */
    public static void l58(StudyContext ctx) throws Exception {
        ctx.log("Parallel chunk merge: split in-memory 'file' lines across workers, combine results.");
        List<String> lines = IntStream.range(0, 800).mapToObj(i -> "line-" + i).toList();
        int chunkSize = 200;
        try (var pool = Executors.newFixedThreadPool(4)) {
            var futures = new ArrayList<java.util.concurrent.Future<Integer>>();
            for (int i = 0; i < lines.size(); i += chunkSize) {
                int start = i;
                int end = Math.min(i + chunkSize, lines.size());
                futures.add(pool.submit(() -> lines.subList(start, end).size()));
            }
            int total = 0;
            for (var f : futures) {
                total += f.get();
            }
            ctx.log("  lines processed: " + total);
        }
        ctx.log("parallelStream() shares ForkJoinPool.commonPool() — avoid blocking; mind side effects.");
        long sum = IntStream.range(0, 50_000).parallel().filter(n -> n % 3 == 0).asLongStream().sum();
        ctx.log("  demo sum (multiples of 3): " + sum);
        ctx.log("  prefer explicit ExecutorService when you need isolation or tuning.");
    }
}
