package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.web.client.RestClient;

/** Contract-style check: assert downstream received expected number of calls. */
public final class Lesson49 extends AbstractMicroLesson {

    public Lesson49() {
        super(49, "Contract testing sketch: MockWebServer records request count for consumer calls.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
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
