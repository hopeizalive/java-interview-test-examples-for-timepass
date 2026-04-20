package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.StandardEnvironment;

/**
 * Lesson 3 explains 12-factor style configuration using Spring profiles.
 *
 * <p>It demonstrates environment-driven wiring so deployment-stage differences are externalized
 * instead of hardcoded.
 */
public final class Lesson03 extends AbstractMicroLesson {

    public Lesson03() {
        super(3, "12-factor config: StandardEnvironment + active profiles drive bean wiring.");
    }

    /**
     * Lesson 3: externalized configuration and profile-based wiring.
     *
     * <p><b>Purpose:</b> Show how runtime environment selects infrastructure beans.
     * <p><b>Role:</b> Introduces deployment portability fundamentals for microservices.
     * <p><b>Demonstration:</b> Activates staging profile and logs selected datasource URL.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: emulate deployment environment by activating a profile.
        var env = new StandardEnvironment();
        env.setActiveProfiles("staging");
        try (var c = new AnnotationConfigApplicationContext()) {
            c.setEnvironment(env);
            c.register(AppConfig.class);
            c.refresh();
            ctx.log("Active profiles: " + String.join(",", c.getEnvironment().getActiveProfiles()));
            ctx.log("Datasource URL from env: " + c.getBean(DatasourceUrl.class).value());
        }
        // Story takeaway: image stays same; environment controls behavior per stage.
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
