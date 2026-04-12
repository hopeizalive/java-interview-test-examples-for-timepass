package com.example.springdata.interview.sddata.config;

import com.example.springdata.interview.sddata.StudyLessonFactory;
import com.example.springdata.interview.sddata.LessonRunnable;
import com.example.springdata.interview.sddata.l01.Sd01DemoService;
import com.example.springdata.interview.sddata.l02.Sd02Tag;
import com.example.springdata.interview.sddata.l02.Sd02TagRepository;
import com.example.springdata.interview.sddata.l04.Sd04Customer;
import com.example.springdata.interview.sddata.l04.Sd04CustomerRepository;
import com.example.springdata.interview.sddata.l05.Sd05Task;
import com.example.springdata.interview.sddata.l05.Sd05TaskRepository;
import com.example.springdata.interview.sddata.l06.Sd06Part;
import com.example.springdata.interview.sddata.l06.Sd06PartRepository;
import com.example.springdata.interview.sddata.l07.Sd07LogEntry;
import com.example.springdata.interview.sddata.l07.Sd07LogEntryRepository;
import com.example.springdata.interview.sddata.l08.Sd08Item;
import com.example.springdata.interview.sddata.l08.Sd08ItemRepository;
import com.example.springdata.interview.sddata.l09.Sd09Song;
import com.example.springdata.interview.sddata.l09.Sd09SongRepository;
import com.example.springdata.interview.sddata.l10.Sd10Person;
import com.example.springdata.interview.sddata.l10.Sd10PersonRepository;
import com.example.springdata.interview.sddata.l11.Sd11Account;
import com.example.springdata.interview.sddata.l11.Sd11AccountRepository;
import com.example.springdata.interview.sddata.l12.Sd12Novel;
import com.example.springdata.interview.sddata.l12.Sd12NovelRepository;
import com.example.springdata.interview.sddata.l13.Sd13Inventory;
import com.example.springdata.interview.sddata.l13.Sd13InventoryRepository;
import com.example.springdata.interview.sddata.l13.Sd13InventoryService;
import com.example.springdata.interview.sddata.l14.Sd14BlogPost;
import com.example.springdata.interview.sddata.l14.Sd14BlogPostRepository;
import com.example.springdata.interview.sddata.l14.Sd14BlogPostDemoService;
import com.example.springdata.interview.sddata.l14.Sd14Comment;
import com.example.springdata.interview.sddata.l15.Sd15LazyDemoService;
import com.example.springdata.interview.sddata.l15.Sd15Player;
import com.example.springdata.interview.sddata.l15.Sd15Team;
import com.example.springdata.interview.sddata.l15.Sd15TeamRepository;
import com.example.springdata.interview.sddata.l16.Sd16TransferService;
import com.example.springdata.interview.sddata.l16.Sd16Wallet;
import com.example.springdata.interview.sddata.l16.Sd16WalletRepository;
import com.example.springdata.interview.sddata.l17.Sd17Pet;
import com.example.springdata.interview.sddata.l17.Sd17PetRepository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.hibernate.LazyInitializationException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SpringDataLessons01To17Config {

    @Bean
    LessonRunnable lesson01(Sd01DemoService demo) {
        return StudyLessonFactory.lesson(1, (app, ctx) -> {
            ctx.log("Talking point: JpaRepository removes repetitive persist/find code vs EntityManager-only services.");
            long viaRepo = demo.saveViaRepository("via-JpaRepository");
            long viaEm = demo.saveViaEntityManager("via-EntityManager#persist");
            ctx.log("IDs: repository=" + viaRepo + ", entityManager=" + viaEm + ", count=" + demo.countRepository());
        });
    }

    @Bean
    LessonRunnable lesson02(Sd02TagRepository repo) {
        return StudyLessonFactory.lesson(2, (app, ctx) -> {
            ctx.log("Hierarchy: Repository → CrudRepository → PagingAndSortingRepository → JpaRepository (+ batch, flush, etc.).");
            Sd02Tag t = new Sd02Tag();
            t.setName("spring-data");
            repo.save(t);
            ctx.log("findById: " + repo.findById(t.getId()) + ", findAll size=" + repo.findAll().size());
        });
    }

    @Bean
    LessonRunnable lesson03(Sd02TagRepository repo) {
        return StudyLessonFactory.lesson(3, (app, ctx) -> {
            ctx.log("Spring Data creates a JDK proxy at runtime; calls delegate to SimpleJpaRepository (or custom fragments).");
            ctx.log("Repository proxy class: " + repo.getClass().getName());
        });
    }

    @Bean
    LessonRunnable lesson04(Sd04CustomerRepository repo) {
        return StudyLessonFactory.lesson(4, (app, ctx) -> {
            Sd04Customer c = new Sd04Customer();
            c.setLastName("Nguyen");
            c.setActive(true);
            repo.save(c);
            ctx.log("Derived query: findByLastNameIgnoreCaseAndActiveTrue → " + repo.findByLastNameIgnoreCaseAndActiveTrue("nguyen").size());
            ctx.log("Limitation: long method names become unreadable; switch to @Query or Specifications for complex logic.");
        });
    }

    @Bean
    LessonRunnable lesson05(Sd05TaskRepository repo) {
        return StudyLessonFactory.lesson(5, (app, ctx) -> {
            Sd05Task t = new Sd05Task();
            t.setTitle("Ship order");
            t.setAssignee("alex");
            repo.save(t);
            ctx.log("Explicit JPQL @Query when derivation is awkward: " + repo.findAssignedJpql("alex"));
        });
    }

    @Bean
    LessonRunnable lesson06(Sd06PartRepository repo) {
        return StudyLessonFactory.lesson(6, (app, ctx) -> {
            for (int i = 0; i < 5; i++) {
                Sd06Part p = new Sd06Part();
                p.setCode("P-" + i);
                repo.save(p);
            }
            var page = repo.findAllNative(PageRequest.of(0, 3));
            ctx.log("Native page totalElements=" + page.getTotalElements() + " (watch dialect-specific SQL and count queries).");
        });
    }

    @Bean
    LessonRunnable lesson07(Sd07LogEntryRepository repo) {
        return StudyLessonFactory.lesson(7, (app, ctx) -> {
            for (int i = 0; i < 4; i++) {
                Sd07LogEntry e = new Sd07LogEntry();
                e.setMessage("m" + i);
                repo.save(e);
            }
            var page = repo.pageAll(PageRequest.of(0, 2));
            var slice = repo.sliceAll(PageRequest.of(0, 2));
            ctx.log("Page has total=" + page.getTotalElements() + " (extra COUNT); Slice hasNext=" + slice.hasNext() + " (no total).");
        });
    }

    @Bean
    LessonRunnable lesson08(Sd08ItemRepository repo, RestTemplateBuilder restTemplateBuilder) {
        return StudyLessonFactory.lesson(8, (app, ctx) -> {
            if (!(app instanceof ServletWebServerApplicationContext web)) {
                throw new IllegalStateException("Lesson 8 expects a servlet web context");
            }
            repo.save(item("alpha"));
            repo.save(item("beta"));
            int port = web.getWebServer().getPort();
            RestTemplate rt = restTemplateBuilder.build();
            String url = "http://127.0.0.1:" + port + "/api/sd08/items?page=0&size=1&sort=name,asc";
            String json = rt.getForObject(url, String.class);
            ctx.log("MVC binds page, size, sort query params to Pageable → sample JSON: " + json);
        });
    }

    private static Sd08Item item(String name) {
        Sd08Item i = new Sd08Item();
        i.setName(name);
        return i;
    }

    @Bean
    LessonRunnable lesson09(Sd09SongRepository repo) {
        return StudyLessonFactory.lesson(9, (app, ctx) -> {
            saveSong(repo, "Zebra", "A");
            saveSong(repo, "Apple", "B");
            var sorted = repo.findAll(Sort.by(Sort.Direction.ASC, "title"));
            ctx.log("Dynamic Sort.by(...) without encoding fields in the method name: " + sorted.getFirst().getTitle());
        });
    }

    private static void saveSong(Sd09SongRepository repo, String title, String artist) {
        Sd09Song s = new Sd09Song();
        s.setTitle(title);
        s.setArtist(artist);
        repo.save(s);
    }

    @Bean
    LessonRunnable lesson10(Sd10PersonRepository repo) {
        return StudyLessonFactory.lesson(10, (app, ctx) -> {
            Sd10Person p = new Sd10Person();
            p.setFirstName("Ann");
            p.setLastName("Doe");
            p.setAge(30);
            repo.save(p);
            Sd10Person probe = new Sd10Person();
            probe.setLastName("doe");
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues();
            var matches = repo.findAll(Example.of(probe, matcher));
            ctx.log("Query by Example matches=" + matches.size() + " (weak for OR-heavy searches and arbitrary predicates).");
        });
    }

    @Bean
    LessonRunnable lesson11(Sd11AccountRepository repo) {
        return StudyLessonFactory.lesson(11, (app, ctx) -> {
            Sd11Account a = new Sd11Account();
            a.setUsername("u1");
            a.setActive(true);
            repo.save(a);
            var rows = repo.findByActiveTrue();
            ctx.log("Interface projection usernames only: " + rows.getFirst().getUsername());
        });
    }

    @Bean
    LessonRunnable lesson12(Sd12NovelRepository repo) {
        return StudyLessonFactory.lesson(12, (app, ctx) -> {
            Sd12Novel n = new Sd12Novel();
            n.setTitle("Study Guide");
            n.setAuthor("Ada");
            repo.save(n);
            ctx.log("JPQL constructor expression DTOs: " + repo.findDtosByAuthor("Ada"));
        });
    }

    @Bean
    LessonRunnable lesson13(Sd13InventoryRepository invRepo, Sd13InventoryService svc) {
        return StudyLessonFactory.lesson(13, (app, ctx) -> {
            Sd13Inventory i = new Sd13Inventory();
            i.setProductCode("SKU-1");
            i.setQuantity(10);
            invRepo.save(i);
            int updated = svc.adjust("SKU-1", 3);
            ctx.log("@Modifying JPQL rows updated=" + updated + ", new qty=" + invRepo.findAll().getFirst().getQuantity());
            ctx.log("Use @Transactional on the service; clearAutomatically refreshes persistence context after bulk JPQL.");
        });
    }

    @Bean
    LessonRunnable lesson14(Sd14BlogPostRepository repo, Sd14BlogPostDemoService demo) {
        return StudyLessonFactory.lesson(14, (app, ctx) -> {
            Sd14BlogPost post = new Sd14BlogPost();
            post.setTitle("Graph demo");
            Sd14Comment c = new Sd14Comment();
            c.setBody("hi");
            post.addComment(c);
            repo.save(post);
            int plainCount = demo.commentCountWithoutGraph("Graph demo");
            int graphCount = demo.commentCountWithEntityGraph("Graph demo");
            ctx.log("Inside @Transactional: without @EntityGraph, comments load lazily on access (count=" + plainCount + ").");
            ctx.log("With @EntityGraph, association fetched with the root (count=" + graphCount + "); fewer round-trips for graphs.");
        });
    }

    @Bean
    LessonRunnable lesson15(Sd15TeamRepository teamRepo, Sd15LazyDemoService lazy) {
        return StudyLessonFactory.lesson(15, (app, ctx) -> {
            Sd15Team team = new Sd15Team();
            team.setName("Tigers");
            Sd15Player p = new Sd15Player();
            p.setNickname("ace");
            team.addPlayer(p);
            Long id = teamRepo.save(team).getId();
            try {
                lazy.countPlayersOutsideTx(id);
                ctx.log("Unexpected: lazy access succeeded outside session.");
            } catch (LazyInitializationException ex) {
                ctx.log("Expected LazyInitializationException when touching lazy collection outside @Transactional: "
                        + ex.getClass().getSimpleName());
            }
            ctx.log("Fix: widen @Transactional or fetch join / @EntityGraph; inside tx count=" + lazy.countPlayersInsideTx(id));
        });
    }

    @Bean
    LessonRunnable lesson16(Sd16WalletRepository wallets, Sd16TransferService transfer) {
        return StudyLessonFactory.lesson(16, (app, ctx) -> {
            Sd16Wallet a = new Sd16Wallet();
            a.setBalanceCents(100);
            Sd16Wallet b = new Sd16Wallet();
            b.setBalanceCents(0);
            wallets.save(a);
            wallets.save(b);
            transfer.transfer(a.getId(), b.getId(), 40);
            ctx.log("Prefer @Transactional on services orchestrating multiple repositories (atomic business operation).");
        });
    }

    @Bean
    LessonRunnable lesson17(Sd17PetRepository repo, EntityManager em, PlatformTransactionManager ptm) {
        return StudyLessonFactory.lesson(17, (app, ctx) -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(ptm);
            transactionTemplate.executeWithoutResult(status -> {
                Sd17Pet pet = new Sd17Pet();
                pet.setName("new");
                Sd17Pet saved = repo.save(pet);
                ctx.log("New entity (null id) → persist path; id=" + saved.getId());
                em.flush();
                em.clear();
                saved.setName("detached-merge");
                Sd17Pet merged = repo.save(saved);
                ctx.log("After clear(), save(detached) follows merge semantics; merged id=" + merged.getId());
            });
        });
    }
}
