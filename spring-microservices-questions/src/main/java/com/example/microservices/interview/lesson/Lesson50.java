package com.example.microservices.interview.lesson;

import com.example.microservices.interview.ms50a.Ms50SvcAApplication;
import com.example.microservices.interview.ms50b.Ms50SvcBApplication;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.BootLessonAutoConfigExcludes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestClient;

/** Two Boot servlet apps in one JVM: service A calls service B over HTTP. */
public final class Lesson50 extends AbstractMicroLesson {

    public Lesson50() {
        super(45, "End-to-end smoke: two Spring Boot apps (ms50b then ms50a) with RestClient hop.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        SpringApplication bApp = new SpringApplication(Ms50SvcBApplication.class);
        bApp.setDefaultProperties(java.util.Map.of(
                "server.port", "0",
                "spring.main.banner-mode", "off",
                "spring.autoconfigure.exclude",
                BootLessonAutoConfigExcludes.NO_SERVLET_SECURITY
        ));
        ConfigurableApplicationContext b = bApp.run();
        try {
            int bPort = ((ServletWebServerApplicationContext) b).getWebServer().getPort();
            SpringApplication aApp = new SpringApplication(Ms50SvcAApplication.class);
            aApp.setDefaultProperties(java.util.Map.of(
                    "server.port", "0",
                    "spring.main.banner-mode", "off",
                    "ms50.peer", "http://127.0.0.1:" + bPort,
                    "spring.autoconfigure.exclude",
                    BootLessonAutoConfigExcludes.NO_SERVLET_SECURITY
            ));
            ConfigurableApplicationContext a = aApp.run();
            try {
                int aPort = ((ServletWebServerApplicationContext) a).getWebServer().getPort();
                String body = RestClient.create().get()
                        .uri("http://127.0.0.1:" + aPort + "/via-b")
                        .retrieve()
                        .body(String.class);
                ctx.log("Response through A→B: " + body);
            } finally {
                a.close();
            }
        } finally {
            b.close();
        }
        ctx.log("Talking point: in CI prefer Testcontainers Compose or Kubernetes for real network separation.");
    }
}
