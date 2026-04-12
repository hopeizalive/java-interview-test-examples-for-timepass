package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.StandardEnvironment;

/** 12-factor style config: externalized settings and Spring profiles. */
public final class Lesson03 extends AbstractMicroLesson {

    public Lesson03() {
        super(3, "12-factor config: StandardEnvironment + active profiles drive bean wiring.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        var env = new StandardEnvironment();
        env.setActiveProfiles("staging");
        try (var c = new AnnotationConfigApplicationContext()) {
            c.setEnvironment(env);
            c.register(AppConfig.class);
            c.refresh();
            ctx.log("Active profiles: " + String.join(",", c.getEnvironment().getActiveProfiles()));
            ctx.log("Datasource URL from env: " + c.getBean(DatasourceUrl.class).value());
        }
        ctx.log("Talking point: config in environment, not baked into images; Spring profiles for promotion stages.");
    }

    @Configuration
    static class AppConfig {
        @Bean
        @Profile("staging")
        DatasourceUrl stagingDs() {
            return new DatasourceUrl("jdbc:h2:mem:staging");
        }

        @Bean
        @Profile("prod")
        DatasourceUrl prodDs() {
            return new DatasourceUrl("jdbc:postgresql://db/prod");
        }

        @Bean
        @Profile("default")
        DatasourceUrl localDs() {
            return new DatasourceUrl("jdbc:h2:mem:local");
        }
    }

    record DatasourceUrl(String value) {}
}
