package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdoc.L29Application;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestClient;

/** springdoc OpenAPI exposes machine-readable contract at /v3/api-docs. */
public final class Lesson29 extends AbstractMicroLesson {

    public Lesson29() {
        super(29, "OpenAPI: springdoc serves /v3/api-docs from a minimal Boot web app.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) {
        SpringApplication app = new SpringApplication(L29Application.class);
        app.setDefaultProperties(java.util.Map.of(
                "server.port", "0",
                "spring.main.banner-mode", "off",
                "springdoc.swagger-ui.enabled", "false",
                "spring.autoconfigure.exclude",
                "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
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
