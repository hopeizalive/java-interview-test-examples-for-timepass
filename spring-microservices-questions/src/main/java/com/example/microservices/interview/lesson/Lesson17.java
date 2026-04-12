package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/** Database-per-service vs shared database anti-pattern. */
public final class Lesson17 extends AbstractMicroLesson {

    public Lesson17() {
        super(17, "Database-per-service: separate DataSource beans model independent schemas.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        try (var c = new AnnotationConfigApplicationContext(TwoDbConfig.class)) {
            DataSource orders = c.getBean("ordersDataSource", DataSource.class);
            DataSource billing = c.getBean("billingDataSource", DataSource.class);
            try (var co = orders.getConnection(); var cb = billing.getConnection()) {
                ctx.log("Orders URL: " + co.getMetaData().getURL());
                ctx.log("Billing URL: " + cb.getMetaData().getURL());
            }
        }
        ctx.log("Talking point: shared DB couples deployments; sagas compensate across DB boundaries.");
    }

    @Configuration
    static class TwoDbConfig {
        @Bean
        DataSource ordersDataSource() {
            return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).setName("ordersdb").build();
        }

        @Bean
        DataSource billingDataSource() {
            return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).setName("billingdb").build();
        }
    }
}
