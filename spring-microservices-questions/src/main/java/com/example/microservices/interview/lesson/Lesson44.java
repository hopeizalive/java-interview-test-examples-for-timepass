package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.StandardEnvironment;

/** Centralized config story: Spring profiles + Environment (Spring Cloud Config is the hosted variant). */
public final class Lesson44 extends AbstractMicroLesson {

    public Lesson44() {
        super(39, "Centralized config: profiles activate feature beans; Spring Cloud Config serves Git-backed layers.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
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
