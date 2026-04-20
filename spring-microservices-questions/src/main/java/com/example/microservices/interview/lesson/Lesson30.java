package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

/**
 * Lesson 30 demonstrates downstream stubbing with MockWebServer.
 *
 * <p>The service client is exercised against a fake HTTP peer for deterministic integration tests.
 */
public final class Lesson30 extends AbstractMicroLesson {

    public Lesson30() {
        super(30, "HTTP stub: MockWebServer returns JSON; RestClient exercises the fake downstream.");
    }

    /**
     * Lesson 30: HTTP stub testing pattern.
     *
     * <p><b>Purpose:</b> Show isolated client testing without real downstream dependency.
     * <p><b>Role:</b> Foundation for reliable consumer-side contract tests.
     * <p><b>Demonstration:</b> Returns stubbed JSON and logs body fetched by RestClient.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        // Story setup: enqueue deterministic JSON response before making client request.
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse()
                    .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .setBody("{\"id\":1,\"total\":42}"));
            server.start();
            String base = "http://127.0.0.1:" + server.getPort();
            String body = RestClient.create().get().uri(base + "/invoice/1").retrieve().body(String.class);
            ctx.log("Stubbed body=" + body);
        }
        ctx.log("Talking point: WireMock adds richer matchers; MockWebServer is lightweight and common in Spring tests.");
    }
}
