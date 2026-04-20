package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Lesson 48 demonstrates real-database integration in containerized tests.
 *
 * <p>It uses PostgreSQL Testcontainer to validate SQL behavior closer to production runtime.
 */
public final class Lesson48 extends AbstractMicroLesson {

    public Lesson48() {
        super(43, "Testcontainers multi-service style: app logic (here JDBC) + real PostgreSQL engine.");
    }

    /**
     * Lesson 43/48: multi-component integration check.
     *
     * <p><b>Purpose:</b> Show confidence gain from testing against real engine behavior.
     * <p><b>Role:</b> Encourages production-like CI testing for data paths.
     * <p><b>Demonstration:</b> Creates table, inserts row, reads value back from Postgres container.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story boundary: skip when Docker is not present in runtime environment.
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
