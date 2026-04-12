package com.example.microservices.interview.lesson;

import com.example.microservices.interview.ms41.Ms41Application;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestClient;

/** Actuator health over HTTP on the same servlet port as the app. */
public final class Lesson41 extends AbstractMicroLesson {

    public Lesson41() {
        super(37, "Spring Boot Actuator: /actuator/health exposed on the embedded server (Kubernetes probes).");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        SpringApplication app = new SpringApplication(Ms41Application.class);
        app.setDefaultProperties(java.util.Map.of(
                "server.port", "0",
                "spring.main.banner-mode", "off",
                "management.endpoints.web.exposure.include", "health,info",
                "spring.autoconfigure.exclude",
                "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
        ));
        try (ConfigurableApplicationContext c = app.run()) {
            int port = ((ServletWebServerApplicationContext) c).getWebServer().getPort();
            String health = RestClient.create().get()
                    .uri("http://127.0.0.1:" + port + "/actuator/health")
                    .retrieve()
                    .body(String.class);
            ctx.log("Health JSON fragment: " + health.substring(0, Math.min(80, health.length())) + "...");
        }
        ctx.log("Talking point: liveness vs readiness; wire spring.boot.admin or Prometheus scrape separately.");
    }
}
