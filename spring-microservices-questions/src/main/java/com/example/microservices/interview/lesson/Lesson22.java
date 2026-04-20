package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * Lesson 22 introduces reactive outbound calls with WebClient timeouts.
 *
 * <p>The lesson keeps one simple request flow to show response-time bounding in reactive clients.
 */
public final class Lesson22 extends AbstractMicroLesson {

    public Lesson22() {
        super(22, "WebClient: retrieve().bodyToMono() with response timeout via Reactor Netty.");
    }

    /**
     * Lesson 22: WebClient with response timeout.
     *
     * <p><b>Purpose:</b> Demonstrate non-blocking client setup with timeout guardrails.
     * <p><b>Role:</b> Reactive counterpart to Lesson 21's RestClient timeout pattern.
     * <p><b>Demonstration:</b> Calls mock endpoint and logs returned body under configured timeout.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        // Story setup: configure Reactor Netty timeout before issuing request.
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
