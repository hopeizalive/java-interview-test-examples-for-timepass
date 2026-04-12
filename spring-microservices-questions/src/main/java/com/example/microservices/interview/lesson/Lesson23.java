package com.example.microservices.interview.lesson;

import com.example.microservices.interview.msfeign.l23.L23Application;
import com.example.microservices.interview.msfeign.l23.L23Client;
import com.example.microservices.interview.study.MicroservicesStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

/** OpenFeign declarative HTTP client. */
public final class Lesson23 extends AbstractMicroLesson {

    public Lesson23() {
        super(23, "OpenFeign: @FeignClient interface backed by MockWebServer URL property.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody("feign-ok"));
            server.start();
            SpringApplication app = new SpringApplication(L23Application.class);
            app.setWebApplicationType(WebApplicationType.NONE);
            app.setDefaultProperties(java.util.Map.of(
                    "spring.main.banner-mode", "off",
                    "spring.cloud.discovery.enabled", "false",
                    "spring.cloud.openfeign.httpclient.hc5.enabled", "false",
                    "peer.base-url", "http://127.0.0.1:" + server.getPort(),
                    "spring.autoconfigure.exclude",
                    "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
            ));
            try (ConfigurableApplicationContext c = app.run()) {
                String body = c.getBean(L23Client.class).hi();
                ctx.log("Feign response=" + body);
            }
        }
        ctx.log("Talking point: Feign centralizes retries/circuit breaking via Spring Cloud + Resilience4j when enabled.");
    }
}
