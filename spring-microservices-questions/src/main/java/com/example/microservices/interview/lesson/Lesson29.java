package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdoc.L29Application;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.BootLessonAutoConfigExcludes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestClient;

/**
 * Lesson 29 demonstrates OpenAPI contract publication with springdoc.
 *
 * <p>It fetches `/v3/api-docs` from a running app to show machine-readable API contracts.
 */
public final class Lesson29 extends AbstractMicroLesson {

    public Lesson29() {
        super(29, "OpenAPI: springdoc serves /v3/api-docs from a minimal Boot web app.");
    }

    /**
     * Lesson 29: OpenAPI endpoint exposure.
     *
     * <p><b>Purpose:</b> Show contract discovery for consumers and tooling.
     * <p><b>Role:</b> Supports contract-first and consumer-driven testing practices.
     * <p><b>Demonstration:</b> Boots app on random port and reads `/v3/api-docs` JSON.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: start minimal web app with springdoc enabled.
        SpringApplication app = new SpringApplication(L29Application.class);
        app.setDefaultProperties(java.util.Map.of(
                "server.port", "0",
                "spring.main.banner-mode", "off",
                "springdoc.swagger-ui.enabled", "false",
                "spring.autoconfigure.exclude",
                BootLessonAutoConfigExcludes.NO_SERVLET_SECURITY
        ));
        try (ConfigurableApplicationContext c = app.run()) {
            int port = ((ServletWebServerApplicationContext) c).getWebServer().getPort();
            String json = RestClient.create().get()
                    .uri("http://127.0.0.1:" + port + "/v3/api-docs")
                    .retrieve()
                    .body(String.class);
            ctx.log("OpenAPI fragment (first 120 chars): " + json.substring(0, Math.min(120, json.length())) + "...");
        }
        ctx.log("Talking point: contract-first vs code-first; publish OpenAPI for consumer-driven contracts.");
    }
}
