package com.example.javads.interview.demos;

import com.example.javads.interview.study.StudyContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Bounded {@link ArrayBlockingQueue} vs {@link LinkedBlockingQueue} (capacity semantics and backing storage).
 *
 * <p><b>Lessons:</b> 19.
 */
public final class DemoBlockingQueuesCompare {

    private DemoBlockingQueuesCompare() {}

    /**
     * Exercises two bounded {@link BlockingQueue} implementations with {@code offer} / {@code poll} timeouts.
     *
     * <p><b>Purpose:</b> connect interview answers about ring buffer (fixed array) vs linked nodes.
     *
     * <p><b>Role:</b> same sequence of operations on both queues with identical capacity.
     *
     * <p><b>Demonstration:</b> {@link ArrayBlockingQueue} is always bounded with one {@code capacity} parameter;
     * {@link LinkedBlockingQueue} can be bounded or optionally unbounded—here we use bounded for apples-to-apples.
     */
    public static void l19(StudyContext ctx) throws InterruptedException {
        ctx.log("Interview question: ArrayBlockingQueue vs LinkedBlockingQueue — what’s under the hood?");
        int capacity = 3;
        BlockingQueue<Integer> arrayQ = new ArrayBlockingQueue<>(capacity);
        BlockingQueue<Integer> linkedQ = new LinkedBlockingQueue<>(capacity);
        ctx.log("Both queues capacity=" + capacity + " (bounded).");
        /*
         * Fill to capacity: both should accept three elements without blocking.
         */
        for (int i = 1; i <= capacity; i++) {
            arrayQ.put(i);
            linkedQ.put(i);
        }
        ctx.log("ArrayBlockingQueue after filling: size=" + arrayQ.size());
        ctx.log("LinkedBlockingQueue after filling: size=" + linkedQ.size());
        /*
         * Fourth offer should fail immediately (non-blocking API) when queue is full.
         */
        boolean arrayOffer = arrayQ.offer(99, 50, TimeUnit.MILLISECONDS);
        boolean linkedOffer = linkedQ.offer(99, 50, TimeUnit.MILLISECONDS);
        ctx.log("offer(99) when full — ArrayBQ: " + arrayOffer + ", LinkedBQ: " + linkedOffer);
        ctx.log("Drain one element from each, then offer(99) succeeds on both:");
        ctx.log("poll ArrayBQ → " + arrayQ.poll(50, TimeUnit.MILLISECONDS));
        ctx.log("poll LinkedBQ → " + linkedQ.poll(50, TimeUnit.MILLISECONDS));
        ctx.log("offer(99) ArrayBQ → " + arrayQ.offer(99, 50, TimeUnit.MILLISECONDS));
        ctx.log("offer(99) LinkedBQ → " + linkedQ.offer(99, 50, TimeUnit.MILLISECONDS));
        ctx.log("Takeaway: ArrayBQ uses a single circular array; LinkedBQ uses linked nodes (optional unbounded variant is a separate interview trap).");
    }
}
