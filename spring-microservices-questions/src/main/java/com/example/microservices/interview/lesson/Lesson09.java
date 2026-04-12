package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.client.RestClient;

/** Static downstream URL from configuration vs dynamic discovery (concept). */
public final class Lesson09 extends AbstractMicroLesson {

    public Lesson09() {
        super(9, "Service discovery vs static URLs: RestClient baseUrl from Spring Environment property.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody("pong"));
            server.start();
            var env = new StandardEnvironment();
            MutablePropertySources ps = env.getPropertySources();
            ps.addFirst(new MapPropertySource("demo", java.util.Map.of("peer.url", "http://localhost:" + server.getPort())));
            try (var c = new AnnotationConfigApplicationContext()) {
                c.setEnvironment(env);
                c.register(ClientConfig.class);
                c.refresh();
                String body = c.getBean(RestClient.class).get().uri("/ping").retrieve().body(String.class);
                ctx.log("Downstream said: " + body);
            }
        }
        ctx.log("Talking point: discovery (Consul/Eureka/K8s DNS) resolves names; dev/test often use explicit URLs.");
    }

    @Configuration
    static class ClientConfig {
        @Bean
        RestClient downstream(StandardEnvironment env) {
            return RestClient.builder().baseUrl(env.getRequiredProperty("peer.url")).build();
        }
    }
}
