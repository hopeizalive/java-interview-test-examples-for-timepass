package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.web.client.RestClient;

/**
 * Lesson 49 demonstrates a consumer-side contract testing sketch.
 *
 * <p>Request count on a stubbed downstream is used as a simple behavioral contract assertion.
 */
public final class Lesson49 extends AbstractMicroLesson {

    public Lesson49() {
        super(44, "Contract testing sketch: MockWebServer records request count for consumer calls.");
    }

    /**
     * Lesson 44/49: contract-verification style stub assertions.
     *
     * <p><b>Purpose:</b> Show deterministic verification of outbound consumer behavior.
     * <p><b>Role:</b> Bridges simple stubs to formal contract-testing tooling.
     * <p><b>Demonstration:</b> Issues two calls and verifies stub request count.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        // Story setup: enqueue two downstream responses and track consumed requests.
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody("9.99"));
            server.enqueue(new MockResponse().setBody("9.99"));
            server.start();
            String base = "http://127.0.0.1:" + server.getPort();
            RestClient.create().get().uri(base + "/contracts/price").retrieve().body(String.class);
            RestClient.create().get().uri(base + "/contracts/price").retrieve().body(String.class);
            ctx.log("Recorded request count on stub: " + server.getRequestCount());
        }
        ctx.log("Talking point: Spring Cloud Contract generates stubs from producer tests for consumers.");
    }
}
