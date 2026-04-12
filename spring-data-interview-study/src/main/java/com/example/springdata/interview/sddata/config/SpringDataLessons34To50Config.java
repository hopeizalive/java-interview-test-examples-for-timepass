package com.example.springdata.interview.sddata.config;

import com.example.springdata.interview.sddata.LessonRunnable;
import com.example.springdata.interview.sddata.StudyLessonFactory;
import com.example.springdata.interview.sddata.l44.Sd44Snip;
import com.example.springdata.interview.sddata.l44.Sd44SnipRepository;
import com.example.springdata.interview.sddata.l45.Sd45EventListener;
import com.example.springdata.interview.sddata.l45.Sd45Order;
import com.example.springdata.interview.sddata.l45.Sd45OrderRepository;
import com.example.springdata.interview.sddata.l46.Sd46BrowseService;
import com.example.springdata.interview.sddata.l46.Sd46Child;
import com.example.springdata.interview.sddata.l46.Sd46Parent;
import com.example.springdata.interview.sddata.l46.Sd46ParentRepository;
import com.example.springdata.interview.sddata.l48.Sd48Sku;
import com.example.springdata.interview.sddata.l48.Sd48SkuRepository;
import com.example.springdata.interview.sddata.l49.Sd49Login;
import com.example.springdata.interview.sddata.l49.Sd49LoginRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;

@Configuration
public class SpringDataLessons34To50Config {

    @Bean
    LessonRunnable lesson34() {
        return StudyLessonFactory.lesson(34, (app, ctx) -> {
            ctx.log("Redis repositories map keys/fields to objects; RedisTemplate is lower-level (ops per type).");
        });
    }

    @Bean
    LessonRunnable lesson35() {
        return StudyLessonFactory.lesson(35, (app, ctx) -> {
            ctx.log("Serialization (JSON/Kryo) drives compatibility; plan migrations and avoid storing opaque blobs without version.");
        });
    }

    @Bean
    LessonRunnable lesson36() {
        return StudyLessonFactory.lesson(36, (app, ctx) -> {
            ctx.log("MongoDB favors embedded documents vs joins; repositories feel similar but consistency and query shapes differ.");
        });
    }

    @Bean
    LessonRunnable lesson37() {
        return StudyLessonFactory.lesson(37, (app, ctx) -> {
            ctx.log("Spring Data REST exposes repositories over HTTP—tighten security, projections, and exposure defaults.");
        });
    }

    @Bean
    LessonRunnable lesson38() {
        return StudyLessonFactory.lesson(38, (app, ctx) -> {
            ctx.log("Elasticsearch indexes serve search/relevance; keep OLTP in the primary store and sync/index as needed.");
        });
    }

    @Bean
    LessonRunnable lesson39() {
        return StudyLessonFactory.lesson(39, (app, ctx) -> {
            ctx.log("Cassandra optimizes wide partitions and write throughput; queries are constrained by partition key design.");
        });
    }

    @Bean
    LessonRunnable lesson40() {
        return StudyLessonFactory.lesson(40, (app, ctx) -> {
            ctx.log("Spring Data LDAP models directory entries (users/groups) with repository-style accessors for SSO-heavy apps.");
        });
    }

    @Bean
    LessonRunnable lesson41() {
        return StudyLessonFactory.lesson(41, (app, ctx) -> {
            ctx.log("Neo4j Spring Data targets relationship-heavy graphs where SQL joins become expensive to reason about.");
        });
    }

    @Bean
    LessonRunnable lesson42() {
        return StudyLessonFactory.lesson(42, (app, ctx) -> {
            ctx.log("Spring Boot BOM (release train) aligns Spring Data modules—avoid ad-hoc version pins that drift from Boot.");
        });
    }

    @Bean
    LessonRunnable lesson43() {
        return StudyLessonFactory.lesson(43, (app, ctx) -> {
            ctx.log("CalVer cadence: plan upgrades regularly; read release notes for breaking repository/query changes.");
        });
    }

    @Bean
    LessonRunnable lesson44(Sd44SnipRepository repo) {
        return StudyLessonFactory.lesson(44, (app, ctx) -> {
            Sd44Snip a = new Sd44Snip();
            a.setTitle("alpha-demo");
            repo.save(a);
            var hits = repo.searchByTitleSpel("demo");
            ctx.log("SpEL in @Query (#{#…}) can inject dynamic fragments; it complicates static analysis and vendor tooling.");
            ctx.log("Matches: " + hits.size());
        });
    }

    @Bean
    LessonRunnable lesson45(Sd45OrderRepository orders, Sd45EventListener listener) {
        return StudyLessonFactory.lesson(45, (app, ctx) -> {
            Sd45Order o = new Sd45Order();
            o.setState("NEW");
            o = orders.save(o);
            o.markPlaced();
            orders.save(o);
            ctx.log("AbstractAggregateRoot registers domain events cleared after publish; listener saw: " + listener.getLast());
        });
    }

    @Bean
    LessonRunnable lesson46(Sd46ParentRepository parents, Sd46BrowseService browse) {
        return StudyLessonFactory.lesson(46, (app, ctx) -> {
            Sd46Parent p = new Sd46Parent();
            p.setName("root");
            Sd46Child c1 = new Sd46Child();
            c1.setTag("a");
            Sd46Child c2 = new Sd46Child();
            c2.setTag("bb");
            p.addChild(c1);
            p.addChild(c2);
            parents.save(p);
            int naive = browse.childTagsWithoutGraph();
            int batched = browse.childTagsWithGraph("r");
            ctx.log("N+1 risk when lazy collections load per parent row; @EntityGraph batch path sum lengths naive=" + naive
                    + ", graph=" + batched + " (inspect SQL with logging to compare).");
        });
    }

    @Bean
    LessonRunnable lesson47() {
        return StudyLessonFactory.lesson(47, (app, ctx) -> {
            ctx.log("open-in-view=false (set in DataSdBoot) avoids hidden session spans in APIs; fetch explicitly in services.");
        });
    }

    @Bean
    LessonRunnable lesson48(Sd48SkuRepository repo) {
        return StudyLessonFactory.lesson(48, (app, ctx) -> {
            Sd48Sku s = new Sd48Sku();
            s.setProductLine("books");
            s.setSkuCode("B-100");
            repo.save(s);
            ctx.log("@Table(indexes=…) documents intent; verify with EXPLAIN that findBySkuCode aligns with planner use.");
            ctx.log("Loaded: " + repo.findBySkuCode("B-100").orElseThrow().getSkuCode());
        });
    }

    @Bean
    LessonRunnable lesson49(Sd49LoginRepository repo) {
        return StudyLessonFactory.lesson(49, (app, ctx) -> {
            Sd49Login first = new Sd49Login();
            first.setUserId("pat");
            repo.save(first);
            Sd49Login dup = new Sd49Login();
            dup.setUserId("pat");
            try {
                repo.saveAndFlush(dup);
                ctx.log("Unexpected: duplicate accepted");
            } catch (DataIntegrityViolationException ex) {
                ctx.log("Unique constraint → DataIntegrityViolationException for idempotent/duplicate handling: "
                        + ex.getClass().getSimpleName());
            }
        });
    }

    @Bean
    LessonRunnable lesson50() {
        return StudyLessonFactory.lesson(50, (app, ctx) -> {
            ctx.log("Capstone: normalized OLTP + rich domain → JPA; heavy reporting/tight SQL → JDBC; document-shaped reads → Mongo;");
            ctx.log("cache → Redis; search → Elasticsearch; each lesson package (sddata.l*) shows the API you rehearse.");
        });
    }
}
