package com.example.javadp.interview.lesson.blocks;

import com.example.javadp.interview.lesson.DesignPatternsLesson;
import com.example.javadp.interview.lesson.PatternLessonHeader;
import com.example.javadp.interview.study.StudyContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * <h2>Block 4 — Lessons 24–30: widely used <em>modern</em> patterns (not all GoF)</h2>
 *
 * <p>These names appear constantly in Java microservices, DDD-style domains, and resilience libraries. They complement
 * GoF: repositories hide persistence mechanics, specifications compose business rules, CQRS separates read/write models,
 * circuit breakers protect callers, pools amortize expensive resources, null objects remove {@code null} checks, and
 * dependency injection inverts control for testability.
 *
 * <p>Contemporary catalogs (for example <a href="https://java-design-patterns.com/patterns">java-design-patterns.com</a>)
 * document many of these alongside GoF; interviews often expect you to connect both worlds.
 */
public final class DesignPatternsBlock04ModernArchitectural {

    private DesignPatternsBlock04ModernArchitectural() {}

    public static void run(DesignPatternsLesson lesson, StudyContext ctx) {
        switch (lesson) {
            case L24 -> lesson24Repository(ctx);
            case L25 -> lesson25Specification(ctx);
            case L26 -> lesson26Cqrs(ctx);
            case L27 -> lesson27CircuitBreaker(ctx);
            case L28 -> lesson28ObjectPool(ctx);
            case L29 -> lesson29NullObject(ctx);
            case L30 -> lesson30DependencyInjection(ctx);
            default -> throw new IllegalStateException("Block 4 received " + lesson);
        }
    }

    /**
     * Repository: mediates between the domain and data mapping layers using a collection-like interface for domain
     * objects (Evans-style DDD).
     */
    private static void lesson24Repository(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Repository",
                "Modern / DDD persistence",
                "Hide storage details behind save/find/delete that speak the domain language (User, not JDBC row).",
                "InMemoryUserRepository stores entities in a map; client code never touches SQL.");
        UserRepository users = new InMemoryUserRepository();
        users.save(new User(1, "ada"));
        Optional<User> found = users.findById(1);
        ctx.log("Loaded user: " + found.map(User::name).orElse("missing"));
    }

    private record User(long id, String name) {}

    private interface UserRepository {
        void save(User u);

        Optional<User> findById(long id);
    }

    private static final class InMemoryUserRepository implements UserRepository {
        private final Map<Long, User> data = new HashMap<>();

        @Override
        public void save(User u) {
            data.put(u.id(), u);
        }

        @Override
        public Optional<User> findById(long id) {
            return Optional.ofNullable(data.get(id));
        }
    }

    /**
     * Specification: encapsulate reusable predicate logic (often composable with AND/OR) for validation or querying.
     */
    private static void lesson25Specification(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Specification",
                "Modern / DDD rules",
                "Business rules become small objects; combine them instead of scattering if-chains.",
                "IsInStock AND IsPremium filters a list of Product records.");
        List<Product> catalog =
                List.of(new Product("a", true, true), new Product("b", true, false), new Product("c", false, true));
        Specification<Product> spec = new AndSpec<>(new InStockSpec(), new PremiumSpec());
        List<String> names = new ArrayList<>();
        for (Product p : catalog) {
            if (spec.isSatisfiedBy(p)) {
                names.add(p.name());
            }
        }
        ctx.log("Matching products: " + names);
    }

    private record Product(String name, boolean inStock, boolean premium) {}

    private interface Specification<T> {
        boolean isSatisfiedBy(T candidate);
    }

    private record AndSpec<T>(Specification<T> left, Specification<T> right) implements Specification<T> {
        private AndSpec {
            Objects.requireNonNull(left);
            Objects.requireNonNull(right);
        }

        @Override
        public boolean isSatisfiedBy(T candidate) {
            return left.isSatisfiedBy(candidate) && right.isSatisfiedBy(candidate);
        }
    }

    private static final class InStockSpec implements Specification<Product> {
        @Override
        public boolean isSatisfiedBy(Product candidate) {
            return candidate.inStock();
        }
    }

    private static final class PremiumSpec implements Specification<Product> {
        @Override
        public boolean isSatisfiedBy(Product candidate) {
            return candidate.premium();
        }
    }

    /** CQRS: Command Query Responsibility Segregation — different models/services for reads vs writes. */
    private static void lesson26Cqrs(StudyContext ctx) {
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

    /** Simplified projection: in real CQRS this would be async event consumers updating a read store. */
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

    /** Circuit Breaker: fail fast while unhealthy; allow trial traffic after cooldown (half-open). */
    private static void lesson27CircuitBreaker(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Circuit Breaker",
                "Modern / resilience (Release It!, Microservices Patterns)",
                "Track failures; open the circuit to skip expensive calls; later probe with limited traffic.",
                "After two failures the breaker opens; the next call probes half-open, then the dependency succeeds once healthy.");
        DemoCircuitBreaker cb = new DemoCircuitBreaker(2);
        RemoteClient flaky = new RemoteClient();
        for (int i = 0; i < 6; i++) {
            ctx.log("call " + i + " => " + cb.invoke(flaky::call));
        }
    }

    private static final class RemoteClient {
        private int attempts;

        String call() {
            attempts++;
            if (attempts < 4) {
                throw new IllegalStateException("dependency down");
            }
            return "ok";
        }
    }

    private enum CbState {
        CLOSED,
        OPEN,
        HALF_OPEN
    }

    private static final class DemoCircuitBreaker {
        private final int failureThreshold;
        private CbState state = CbState.CLOSED;
        private int consecutiveFailures;

        DemoCircuitBreaker(int failureThreshold) {
            this.failureThreshold = failureThreshold;
        }

        String invoke(ThrowingSupplier supplier) {
            if (state == CbState.OPEN) {
                state = CbState.HALF_OPEN;
                return trial(supplier);
            }
            return trial(supplier);
        }

        private String trial(ThrowingSupplier supplier) {
            try {
                String ok = supplier.get();
                state = CbState.CLOSED;
                consecutiveFailures = 0;
                return ok;
            } catch (RuntimeException ex) {
                if (state == CbState.HALF_OPEN) {
                    state = CbState.OPEN;
                }
                consecutiveFailures++;
                if (consecutiveFailures >= failureThreshold) {
                    state = CbState.OPEN;
                }
                return "error:" + ex.getMessage();
            }
        }
    }

    @FunctionalInterface
    private interface ThrowingSupplier {
        String get();
    }

    /** Object Pool: reuse expensive objects (connections, parsers) instead of allocating per use. */
    private static void lesson28ObjectPool(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Object Pool",
                "Modern / performance",
                "Bound concurrency and reuse heavyweight instances; callers borrow and return instead of new().",
                "ConnectionPool hands out at most two Connection stubs; third borrow blocks until a return.");
        ConnectionPool pool = new ConnectionPool(2);
        Connection c1;
        Connection c2;
        try {
            c1 = pool.borrowConnection();
            c2 = pool.borrowConnection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ctx.log("Interrupted borrowing from pool");
            return;
        }
        ctx.log("Borrowed two connections");
        Thread t = new Thread(
                () -> {
                    try {
                        Connection c3 = pool.borrowConnection();
                        ctx.log("Late borrower got " + c3.id());
                        pool.returnConnection(c3);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
        t.start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ctx.log("Interrupted while demoing pool wait");
            return;
        }
        pool.returnConnection(c1);
        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        pool.returnConnection(c2);
        ctx.log("Pool settled; all returned.");
    }

    private record Connection(String id) {}

    private static final class ConnectionPool {
        private final ArrayBlockingQueue<Connection> free;

        ConnectionPool(int size) {
            this.free = new ArrayBlockingQueue<>(size);
            for (int i = 0; i < size; i++) {
                free.offer(new Connection("conn-" + i));
            }
        }

        Connection borrowConnection() throws InterruptedException {
            return free.take();
        }

        void returnConnection(Connection c) {
            free.offer(c);
        }
    }

    /** Null Object: use a stand-in object with safe no-op behavior instead of null references. */
    private static void lesson29NullObject(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Null Object",
                "Modern / robustness",
                "Avoid null checks everywhere by injecting a logger that either prints or intentionally does nothing.",
                "ApplicationService logs with RealLogger vs NullLogger; both satisfy the same Logger interface.");
        new ApplicationService(new RealLogger(ctx)).run(ctx);
        new ApplicationService(NullLogger.INSTANCE).run(ctx);
    }

    private interface Logger {
        void info(String msg);
    }

    private static final class RealLogger implements Logger {
        private final StudyContext ctx;

        RealLogger(StudyContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void info(String msg) {
            ctx.log("[INFO] " + msg);
        }
    }

    private enum NullLogger implements Logger {
        INSTANCE;

        @Override
        public void info(String msg) {
            // intentional no-op — replaces null checks at call sites
        }
    }

    private static final class ApplicationService {
        private final Logger log;

        ApplicationService(Logger log) {
            this.log = log;
        }

        void run(StudyContext ctx) {
            log.info("starting job");
            ctx.log("job finished");
        }
    }

    /** Dependency Injection: depend on abstractions; collaborators supplied externally (constructor injection). */
    private static void lesson30DependencyInjection(StudyContext ctx) {
        PatternLessonHeader.print(
                ctx,
                "Dependency Injection",
                "Modern / wiring (Martin Fowler) — not a GoF book pattern but universal in Java apps",
                "Invert control: the service never chooses its clock; tests inject a fake time source.",
                "ReportService receives Clock via constructor; production uses system clock, test uses fixed clock.");
        Clock fixed = () -> 1_700_000_000_000L;
        new ReportService(fixed).publish(ctx);
        new ReportService(System::currentTimeMillis).publish(ctx);
    }

    private interface Clock {
        long millis();
    }

    private static final class ReportService {
        private final Clock clock;

        ReportService(Clock clock) {
            this.clock = clock;
        }

        void publish(StudyContext ctx) {
            ctx.log("Report generated at epochMillis=" + clock.millis());
        }
    }
}
