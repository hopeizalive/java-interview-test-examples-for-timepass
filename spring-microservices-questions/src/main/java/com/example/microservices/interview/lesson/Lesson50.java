package com.example.microservices.interview.lesson;

import com.example.microservices.interview.ms50a.Ms50SvcAApplication;
import com.example.microservices.interview.ms50b.Ms50SvcBApplication;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.BootLessonAutoConfigExcludes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestClient;

/**
 * Lesson 45/50 runs an end-to-end two-service hop in one JVM.
 *
 * <p>Service B is started first, service A receives B's URL, and a request to A verifies downstream
 * delegation to B.
 */
public final class Lesson50 extends AbstractMicroLesson {

    public Lesson50() {
        super(45, "End-to-end smoke: two Spring Boot apps (ms50b then ms50a) with RestClient hop.");
    }

    /**
     * Lesson 45: end-to-end inter-service HTTP call.
     *
     * <p><b>Purpose:</b> Demonstrate service-to-service communication and startup dependency wiring.
     * <p><b>Role:</b> Capstone runtime smoke test for the microservice lesson set.
     * <p><b>Demonstration:</b> Boots two apps, calls A `/via-b`, and logs combined response.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: start downstream service B first and capture random port.
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
            // Story action: start service A with B endpoint injected as peer URL.
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
                // Story observation: calling A proves the A -> B hop is functional.
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
