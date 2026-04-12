package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/** Multi-component integration: JDBC against real Postgres in Docker. */
public final class Lesson48 extends AbstractMicroLesson {

    public Lesson48() {
        super(48, "Testcontainers multi-service style: app logic (here JDBC) + real PostgreSQL engine.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        if (!ctx.dockerAvailable()) {
            ctx.log("Skip: Docker not available.");
            return;
        }
        try (PostgreSQLContainer<?> pg = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))) {
            pg.start();
            var ds = new DriverManagerDataSource();
            ds.setUrl(pg.getJdbcUrl());
            ds.setUsername(pg.getUsername());
            ds.setPassword(pg.getPassword());
            JdbcTemplate jdbc = new JdbcTemplate(ds);
            jdbc.execute("create table integration_demo(x int)");
            jdbc.update("insert into integration_demo values (?)", 48);
            Integer v = jdbc.queryForObject("select x from integration_demo", Integer.class);
            ctx.log("Read back x=" + v);
        } catch (Throwable t) {
            ctx.log("Integration container failed: " + t.getMessage());
        }
        ctx.log("Talking point: compose app + DB + broker containers for realistic CI.");
    }
}
