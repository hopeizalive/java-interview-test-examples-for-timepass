package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.StandardEnvironment;

/**
 * Lesson 44 demonstrates profile-driven configuration selection.
 *
 * <p>It activates one region profile to show environment-controlled bean wiring.
 */
public final class Lesson44 extends AbstractMicroLesson {

    public Lesson44() {
        super(39, "Centralized config: profiles activate feature beans; Spring Cloud Config serves Git-backed layers.");
    }

    /**
     * Lesson 39/44: centralized config concepts with profiles.
     *
     * <p><b>Purpose:</b> Show runtime configuration differences without code changes.
     * <p><b>Role:</b> Config governance topic for multi-environment microservice fleets.
     * <p><b>Demonstration:</b> Activates `eu` profile and logs selected config bean.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: activate target deployment profile before context refresh.
        var env = new StandardEnvironment();
        env.setActiveProfiles("eu");
        try (var c = new AnnotationConfigApplicationContext()) {
            c.setEnvironment(env);
            c.register(ProfileBeans.class);
            c.refresh();
            ctx.log(c.getBean(String.class));
        }
        ctx.log("Talking point: never commit secrets—use Vault / Azure Key Vault / Kubernetes secrets with Spring config import.");
    }

    @Configuration
    static class ProfileBeans {
        @Bean
        @Profile("eu")
        String region() {
            return "config: region=EU datasource policy";
        }

        @Bean
        @Profile("us")
        String regionUs() {
            return "config: region=US datasource policy";
        }
    }
}
