package com.example.springdata.interview.sddata.config;

import com.example.springdata.interview.sddata.LessonRunnable;
import com.example.springdata.interview.sddata.StudyLessonFactory;
import com.example.springdata.interview.sddata.l18.Sd18BatchRow;
import com.example.springdata.interview.sddata.l18.Sd18BatchRowRepository;
import com.example.springdata.interview.sddata.l19.Sd19FilterRow;
import com.example.springdata.interview.sddata.l19.Sd19FilterRowRepository;
import com.example.springdata.interview.sddata.l20.Sd20Ledger;
import com.example.springdata.interview.sddata.l20.Sd20LedgerRepository;
import com.example.springdata.interview.sddata.l21.Sd21FlushEntity;
import com.example.springdata.interview.sddata.l21.Sd21FlushEntityRepository;
import com.example.springdata.interview.sddata.l22.Sd22ReportRow;
import com.example.springdata.interview.sddata.l22.Sd22ReportRowRepository;
import com.example.springdata.interview.sddata.l22.Sd22ReportService;
import com.example.springdata.interview.sddata.l23.Sd23Ghost;
import com.example.springdata.interview.sddata.l23.Sd23GhostRepository;
import com.example.springdata.interview.sddata.l24.Sd24Cart;
import com.example.springdata.interview.sddata.l24.Sd24CartLine;
import com.example.springdata.interview.sddata.l24.Sd24CartRepository;
import com.example.springdata.interview.sddata.l25.Sd25Doc;
import com.example.springdata.interview.sddata.l25.Sd25DocRepository;
import com.example.springdata.interview.sddata.l26.Sd26Book;
import com.example.springdata.interview.sddata.l26.Sd26BookRepository;
import com.example.springdata.interview.sddata.l27.Sd27BulkRow;
import com.example.springdata.interview.sddata.l27.Sd27BulkRowRepository;
import com.example.springdata.interview.sddata.l27.Sd27BulkService;
import com.example.springdata.interview.sddata.l30.Sd30Author;
import com.example.springdata.interview.sddata.l30.Sd30AuthorRepository;
import com.example.springdata.interview.sddata.l30.Sd30Book;
import com.example.springdata.interview.sddata.l31.Sd31JdbcShipment;
import com.example.springdata.interview.sddata.l31.Sd31JdbcShipmentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Data grouped lesson runners for lessons 18-29.
 *
 * <p>Each bean encapsulates one lesson flow and logs the interview takeaway from real data access
 * behavior.
 */
@Configuration
public class SpringDataLessons18To33Config {

    /**
     * Lesson 18: saveAll and batching.
     *
     * <p><b>Purpose:</b> Demonstrate bulk insert workflow with repository batching.
     * <p><b>Role:</b> Introduces throughput tuning for write-heavy paths.
     * <p><b>Demonstration:</b> Persists 20 rows via saveAll and logs batch-size guidance.
     */
    @Bean
    LessonRunnable lesson18(Sd18BatchRowRepository repo) {
        return StudyLessonFactory.lesson(18, (app, ctx) -> {
            // Story setup: build a batch payload before one repository call.
            List<Sd18BatchRow> rows = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                Sd18BatchRow r = new Sd18BatchRow();
                r.setPayload("row-" + i);
                rows.add(r);
            }
            repo.saveAll(rows);
            ctx.log("saveAll batches work units; tune spring.jpa.properties.hibernate.jdbc.batch_size for insert throughput.");
        });
    }

    /**
     * Lesson 19: dynamic filters with Specification.
     *
     * <p><b>Purpose:</b> Show composable predicate construction without string concatenation.
     * <p><b>Role:</b> Scales query complexity beyond method-name derivation.
     * <p><b>Demonstration:</b> Builds category+score spec and counts matching rows.
     */
    @Bean
    LessonRunnable lesson19(Sd19FilterRowRepository repo) {
        return StudyLessonFactory.lesson(19, (app, ctx) -> {
            seedFilter(repo, "A", 5);
            seedFilter(repo, "A", 9);
            seedFilter(repo, "B", 2);
            Specification<Sd19FilterRow> spec = (root, query, cb) -> cb.and(
                    cb.equal(root.get("category"), "A"), cb.greaterThanOrEqualTo(root.get("score"), 8));
            long count = repo.count(spec);
            ctx.log("Specifications build dynamic Criteria API predicates; count=" + count);
        });
    }

    private static void seedFilter(Sd19FilterRowRepository repo, String cat, int score) {
        Sd19FilterRow r = new Sd19FilterRow();
        r.setCategory(cat);
        r.setScore(score);
        repo.save(r);
    }

    /**
     * Lesson 20: optimistic locking conflict detection.
     *
     * <p><b>Purpose:</b> Demonstrate stale detached state failing against updated version.
     * <p><b>Role:</b> Reinforces concurrency-safe update strategy.
     * <p><b>Demonstration:</b> Updates fresh row, then attempts stale save and catches locking error.
     */
    @Bean
    LessonRunnable lesson20(Sd20LedgerRepository repo, PlatformTransactionManager ptm) {
        TransactionTemplate tt = new TransactionTemplate(ptm);
        return StudyLessonFactory.lesson(20, (app, ctx) -> {
            // Story setup: create baseline row and keep detached copy for stale-write attempt.
            Long id = tt.execute(status -> repo.save(ledger("main", 0)).getId());
            Sd20Ledger detached = tt.execute(status -> repo.findById(id).orElseThrow());
            tt.execute(status -> {
                Sd20Ledger fresh = repo.findById(id).orElseThrow();
                fresh.setAmount(fresh.getAmount() + 1);
                repo.save(fresh);
                return null;
            });
            try {
                detached.setAmount(42);
                repo.save(detached);
                ctx.log("Unexpected: stale write succeeded");
            } catch (OptimisticLockingFailureException ex) {
                // Story observation: @Version detects stale state and rejects update.
                ctx.log("@Version + stale state → " + ex.getClass().getSimpleName());
            }
        });
    }

    private static Sd20Ledger ledger(String label, int amount) {
        Sd20Ledger l = new Sd20Ledger();
        l.setLabel(label);
        l.setAmount(amount);
        return l;
    }

    /**
     * Lesson 21: explicit flush semantics.
     *
     * <p><b>Purpose:</b> Show SQL synchronization before transaction commit.
     * <p><b>Role:</b> Clarifies write timing inside one transactional boundary.
     * <p><b>Demonstration:</b> Calls flush after save and explains visibility implication.
     */
    @Bean
    LessonRunnable lesson21(Sd21FlushEntityRepository repo, EntityManager em, PlatformTransactionManager ptm) {
        TransactionTemplate tt = new TransactionTemplate(ptm);
        return StudyLessonFactory.lesson(21, (app, ctx) -> {
            tt.executeWithoutResult(status -> {
                Sd21FlushEntity e = new Sd21FlushEntity();
                e.setName("flush-me");
                repo.save(e);
                em.flush();
            });
            ctx.log("flush() pushes SQL to the DB within the same transaction; commit still defines visibility to other txs.");
        });
    }

    /**
     * Lesson 22: read-only transactions.
     *
     * <p><b>Purpose:</b> Explain provider optimization hint for read-heavy service methods.
     * <p><b>Role:</b> Adds transaction tuning beyond simple read/write split.
     * <p><b>Demonstration:</b> Persists one row and reads count through read-only service.
     */
    @Bean
    LessonRunnable lesson22(Sd22ReportRowRepository rows, Sd22ReportService report) {
        return StudyLessonFactory.lesson(22, (app, ctx) -> {
            rows.save(row("m1"));
            ctx.log("readOnly @Transactional on service hints the persistence provider to optimize (no dirty checks). count="
                    + report.countRows());
        });
    }

    private static Sd22ReportRow row(String m) {
        Sd22ReportRow r = new Sd22ReportRow();
        r.setMetric(m);
        return r;
    }

    /**
     * Lesson 23: getReferenceById versus findById.
     *
     * <p><b>Purpose:</b> Contrast lazy proxy retrieval with immediate Optional-based lookup.
     * <p><b>Role:</b> Helps pick repository APIs based on access pattern.
     * <p><b>Demonstration:</b> Loads proxy, triggers initialization, then compares findById.
     */
    @Bean
    LessonRunnable lesson23(Sd23GhostRepository repo, EntityManager em, PlatformTransactionManager ptm) {
        TransactionTemplate tt = new TransactionTemplate(ptm);
        return StudyLessonFactory.lesson(23, (app, ctx) -> {
            tt.executeWithoutResult(status -> {
                Sd23Ghost g = new Sd23Ghost();
                g.setLabel("proxy");
                Long id = repo.save(g).getId();
                em.flush();
                em.clear();
                // Story phase A: getReferenceById gives proxy and may defer SELECT.
                var ref = repo.getReferenceById(id);
                ctx.log("getReferenceById returns a lazy proxy (may not SELECT until use): " + ref.getClass().getSimpleName());
                // Story phase B: touching field forces row load.
                ctx.log("Touching proxy loads row: label=" + ref.getLabel());
                ctx.log("findById after clear: " + repo.findById(id));
            });
        });
    }

    /**
     * Lesson 24: cascade and orphan removal.
     *
     * <p><b>Purpose:</b> Show aggregate-root delete behavior for child entities.
     * <p><b>Role:</b> Reinforces lifecycle ownership rules in domain modeling.
     * <p><b>Demonstration:</b> Saves cart with line, deletes cart, and logs cascade outcome.
     */
    @Bean
    LessonRunnable lesson24(Sd24CartRepository carts) {
        return StudyLessonFactory.lesson(24, (app, ctx) -> {
            Sd24Cart cart = new Sd24Cart();
            cart.setOwner("sam");
            Sd24CartLine line = new Sd24CartLine();
            line.setSku("SKU-9");
            cart.addLine(line);
            Long id = carts.save(cart).getId();
            carts.deleteById(id);
            ctx.log("Cascade + orphanRemoval removed lines with the aggregate root delete.");
        });
    }

    /**
     * Lesson 25: JPA auditing fields.
     *
     * <p><b>Purpose:</b> Demonstrate auto-maintained create/update timestamps.
     * <p><b>Role:</b> Adds persistence metadata automation pattern.
     * <p><b>Demonstration:</b> Saves document and prints auditing fields.
     */
    @Bean
    LessonRunnable lesson25(Sd25DocRepository repo) {
        return StudyLessonFactory.lesson(25, (app, ctx) -> {
            Sd25Doc d = new Sd25Doc();
            d.setTitle("Audited");
            Sd25Doc saved = repo.save(d);
            ctx.log("JPA auditing timestamps: createdAt=" + saved.getCreatedAt() + ", updatedAt=" + saved.getUpdatedAt());
        });
    }

    /**
     * Lesson 26: custom repository fragment implementation.
     *
     * <p><b>Purpose:</b> Show extending repository API with custom behavior.
     * <p><b>Role:</b> Balances generated repository methods with hand-written domain queries.
     * <p><b>Demonstration:</b> Invokes custom bulk JPQL method from fragment implementation.
     */
    @Bean
    LessonRunnable lesson26(Sd26BookRepository books) {
        return StudyLessonFactory.lesson(26, (app, ctx) -> {
            Sd26Book b = new Sd26Book();
            b.setTitle("Patterns");
            b.setPageCount(100);
            books.save(b);
            int rows = books.bumpAllPageCounts(5);
            ctx.log("Custom fragment Sd26BookRepositoryImpl executed bulk JPQL; rows=" + rows + ", new pages="
                    + books.findAll().getFirst().getPageCount());
        });
    }

    /**
     * Lesson 27: set-based bulk update via EntityManager.
     *
     * <p><b>Purpose:</b> Demonstrate executeUpdate path for mass state changes.
     * <p><b>Role:</b> Complements repository CRUD with efficient bulk operations.
     * <p><b>Demonstration:</b> Flags all rows through service bulk JPQL call.
     */
    @Bean
    LessonRunnable lesson27(Sd27BulkRowRepository rows, Sd27BulkService bulk) {
        return StudyLessonFactory.lesson(27, (app, ctx) -> {
            for (int i = 0; i < 3; i++) {
                Sd27BulkRow r = new Sd27BulkRow();
                r.setFlagged(false);
                rows.save(r);
            }
            int updated = bulk.flagAllJpql();
            ctx.log("EntityManager.executeUpdate beside repositories for set-based changes; rows=" + updated);
        });
    }

    /**
     * Lesson 28: pagination with join-fetch caveats.
     *
     * <p><b>Purpose:</b> Show duplicate parent rows caused by join fetch collections.
     * <p><b>Role:</b> Prevents incorrect paging/count assumptions on graph queries.
     * <p><b>Demonstration:</b> Persists one author with two books and inspects joined result size.
     */
    @Bean
    LessonRunnable lesson28(Sd30AuthorRepository authors) {
        return StudyLessonFactory.lesson(28, (app, ctx) -> {
            Sd30Author a = new Sd30Author();
            a.setName("Jordan");
            Sd30Book b1 = new Sd30Book();
            b1.setTitle("One");
            Sd30Book b2 = new Sd30Book();
            b2.setTitle("Two");
            a.addBook(b1);
            a.addBook(b2);
            authors.save(a);
            // Story observation: one logical parent may appear multiple times without DISTINCT.
            List<Sd30Author> flat = authors.findAllJoinBooks();
            ctx.log("Join fetch without DISTINCT can duplicate parent rows in the List (size=" + flat.size()
                    + " for one logical author). Use DISTINCT / DTO projection / separate countQuery for Page APIs.");
        });
    }

    /**
     * Lesson 29: Spring Data JDBC positioning.
     *
     * <p><b>Purpose:</b> Highlight explicit aggregate persistence model without lazy navigation.
     * <p><b>Role:</b> Contrasts JDBC module philosophy with JPA ORM behavior.
     * <p><b>Demonstration:</b> Saves JDBC shipment aggregate and logs id.
     */
    @Bean
    LessonRunnable lesson29(Sd31JdbcShipmentRepository jdbcRepo) {
        return StudyLessonFactory.lesson(29, (app, ctx) -> {
            Sd31JdbcShipment s = new Sd31JdbcShipment();
            s.setTrackingCode("TRK-1");
            Sd31JdbcShipment saved = jdbcRepo.save(s);
            ctx.log("Spring Data JDBC maps aggregates to SQL explicitly (no lazy navigators); saved id=" + saved.getId());
        });
    }
}
