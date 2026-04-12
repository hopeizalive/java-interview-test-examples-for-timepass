package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/** WebClient reactive client with bounded response timeout. */
public final class Lesson22 extends AbstractMicroLesson {

    public Lesson22() {
        super(22, "WebClient: retrieve().bodyToMono() with response timeout via Reactor Netty.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody("{\"msg\":\"hello\"}"));
            server.start();
            HttpClient netty = HttpClient.create().responseTimeout(Duration.ofMillis(300));
            WebClient wc = WebClient.builder()
                    .baseUrl("http://127.0.0.1:" + server.getPort())
                    .clientConnector(new ReactorClientHttpConnector(netty))
                    .build();
            String body = wc.get().uri("/api").retrieve().bodyToMono(String.class).block(Duration.ofSeconds(2));
            ctx.log("Body=" + body);
        }
        ctx.log("Talking point: propagate tracing context through WebClient filters in reactive chains.");
    }
}
