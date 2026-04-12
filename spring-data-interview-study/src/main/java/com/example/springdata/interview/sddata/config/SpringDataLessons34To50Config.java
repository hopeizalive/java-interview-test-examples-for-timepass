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

/** Lessons 30–34 (renumbered; former narrative-only slots removed). */
@Configuration
public class SpringDataLessons34To50Config {

    @Bean
    LessonRunnable lesson30(Sd44SnipRepository repo) {
        return StudyLessonFactory.lesson(30, (app, ctx) -> {
            Sd44Snip a = new Sd44Snip();
            a.setTitle("alpha-demo");
            repo.save(a);
            var hits = repo.searchByTitleSpel("demo");
            ctx.log("SpEL in @Query (#{#…}) can inject dynamic fragments; it complicates static analysis and vendor tooling.");
            ctx.log("Matches: " + hits.size());
        });
    }

    @Bean
    LessonRunnable lesson31(Sd45OrderRepository orders, Sd45EventListener listener) {
        return StudyLessonFactory.lesson(31, (app, ctx) -> {
            Sd45Order o = new Sd45Order();
            o.setState("NEW");
            o = orders.save(o);
            o.markPlaced();
            orders.save(o);
            ctx.log("AbstractAggregateRoot registers domain events cleared after publish; listener saw: " + listener.getLast());
        });
    }

    @Bean
    LessonRunnable lesson32(Sd46ParentRepository parents, Sd46BrowseService browse) {
        return StudyLessonFactory.lesson(32, (app, ctx) -> {
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
    LessonRunnable lesson33(Sd48SkuRepository repo) {
        return StudyLessonFactory.lesson(33, (app, ctx) -> {
            Sd48Sku s = new Sd48Sku();
            s.setProductLine("books");
            s.setSkuCode("B-100");
            repo.save(s);
            ctx.log("@Table(indexes=…) documents intent; verify with EXPLAIN that findBySkuCode aligns with planner use.");
            ctx.log("Loaded: " + repo.findBySkuCode("B-100").orElseThrow().getSkuCode());
        });
    }

    @Bean
    LessonRunnable lesson34(Sd49LoginRepository repo) {
        return StudyLessonFactory.lesson(34, (app, ctx) -> {
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
}
