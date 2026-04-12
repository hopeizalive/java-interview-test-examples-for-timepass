package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MicroBoot;
import com.example.microservices.interview.msdata.l11.L11Application;
import com.example.microservices.interview.msdata.l11.L11UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/** Testcontainers + Spring Data vs in-memory H2 fallback. */
public final class Lesson20 extends AbstractMicroLesson {

    public Lesson20() {
        super(20, "Testcontainers PostgreSQL when Docker is available; otherwise H2 MicroBoot sanity check.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        if (ctx.dockerAvailable()) {
            try (PostgreSQLContainer<?> pg = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))) {
                pg.start();
                var ds = new DriverManagerDataSource();
                ds.setUrl(pg.getJdbcUrl());
                ds.setUsername(pg.getUsername());
                ds.setPassword(pg.getPassword());
                JdbcTemplate jdbc = new JdbcTemplate(ds);
                jdbc.execute("create table demo (id serial primary key, name text)");
                jdbc.update("insert into demo(name) values (?)", "from-testcontainers");
                ctx.log("Rows in demo via JDBC/Testcontainers: " + jdbc.queryForObject("select count(*) from demo", Integer.class));
            } catch (Throwable ex) {
                ctx.log("Testcontainers PostgreSQL failed: " + ex.getMessage());
                runH2Fallback(ctx);
            }
        } else {
            ctx.log("Docker unavailable — running H2-backed Spring Data smoke instead.");
            runH2Fallback(ctx);
        }
        ctx.log("Talking point: integration tests hit real engines (Postgres quirks) closer to prod than H2 alone.");
    }

    private static void runH2Fallback(MicroservicesStudyContext ctx) {
        try (var c = MicroBoot.start(L11Application.class, "ms20")) {
            ctx.log("H2 user count=" + c.getBean(L11UserRepository.class).count());
        }
    }
}
