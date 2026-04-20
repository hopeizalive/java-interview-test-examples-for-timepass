package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msdoc.L29Application;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.BootLessonAutoConfigExcludes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestClient;

/**
 * Lesson 47 demonstrates full-stack integration testing on random port.
 *
 * <p>The lesson boots complete servlet context and validates endpoint behavior through HTTP call.
 */
public final class Lesson47 extends AbstractMicroLesson {

    public Lesson47() {
        super(42, "@SpringBootTest random port analog: Boot web app on port 0 + RestClient integration call.");
    }

    /**
     * Lesson 42/47: full application integration test pattern.
     *
     * <p><b>Purpose:</b> Show end-to-end verification with full Boot auto-configuration.
     * <p><b>Role:</b> Highest-confidence test layer after slices and unit tests.
     * <p><b>Demonstration:</b> Starts app on port 0 and calls `/hello` via RestClient.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) {
        // Story setup: start complete web app with random free port.
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
            String body = RestClient.create().get()
                    .uri("http://127.0.0.1:" + port + "/hello")
                    .retrieve()
                    .body(String.class);
            ctx.log("HTTP body=" + body);
        }
        ctx.log("Talking point: TestRestTemplate/WebTestClient are the usual test-side clients.");
    }
}
