package com.example.javadp.interview.lesson.patterns.modern;

import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.List;

/**
 * CQRS: segregate command (write) models from query (read) models so each side can scale and evolve independently.
 */
public final class CqrsPatternLesson {

    private CqrsPatternLesson() {}

    public static void run(StudyContext ctx) {
        PatternLessonHeader.printRealWorldExample(
                ctx,
                1,
                "Event-sourced read models",
                "Commands append facts; projectors maintain denormalized views optimized for dashboards and search.",
                "Classic CQRS + event streaming (Kafka, Pulsar) in large systems.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                2,
                "Separate OLTP vs OLAP stores",
                "Operational writes stay normalized; analytics replicas/cubes serve heavy reporting without locking OLTP.",
                "Separation of concerns at persistence tier.");
        PatternLessonHeader.printRealWorldExample(
                ctx,
                3,
                "GraphQL + command APIs",
                "Mutations hit write services; read resolvers fan out to cached BFFs tailored for UI needs.",
                "API shape reflects CQRS split even if one database backs both early on.");

        ctx.log("--- Java core tie-in ---");
        java.util.concurrent.LinkedBlockingQueue<String> writeModel = new java.util.concurrent.LinkedBlockingQueue<>();
        java.util.concurrent.ConcurrentLinkedQueue<String> readModel = new java.util.concurrent.ConcurrentLinkedQueue<>();
        writeModel.offer("cmd:place");
        String cmd = writeModel.poll();
        readModel.add("view:" + cmd);
        ctx.log("Separate JDK queues mimic write vs read paths: " + readModel.poll());

        PatternLessonHeader.print(
                ctx,
                "CQRS",
                "Modern / distributed read models",
                "Segregate commands (mutations) from queries (reads) so each side can scale/evolve independently.",
                "OrderCommandService mutates write-model; OrderQueryService reads from a projection fed by those writes.");
        OrderCommandService write = new OrderCommandService();
        OrderQueryService read = new OrderQueryService(write.projection());
        write.placeOrder("A", 10);
        write.placeOrder("B", 5);
        ctx.log("Read-side snapshot: " + read.openOrders());
    }

    private static final class OrderCommandService {
        private final List<Order> orders = new ArrayList<>();
        private final OrderProjection projection = new OrderProjection();

        OrderProjection projection() {
            return projection;
        }

        void placeOrder(String id, int qty) {
            Order o = new Order(id, qty);
            orders.add(o);
            projection.apply(o);
        }
    }

    private record Order(String id, int qty) {}

    private static final class OrderProjection {
        private final List<String> lines = new ArrayList<>();

        void apply(Order o) {
            lines.add("{id:%s,qty:%d}".formatted(o.id(), o.qty()));
        }

        String snapshot() {
            return "[" + String.join(",", lines) + "]";
        }
    }

    private static final class OrderQueryService {
        private final OrderProjection projection;

        OrderQueryService(OrderProjection projection) {
            this.projection = projection;
        }

        String openOrders() {
            return projection.snapshot() + " (read model)";
        }
    }
}
