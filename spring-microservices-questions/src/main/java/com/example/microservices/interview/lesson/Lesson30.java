package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

/** Stub a downstream with MockWebServer (same role as WireMock in integration tests). */
public final class Lesson30 extends AbstractMicroLesson {

    public Lesson30() {
        super(30, "HTTP stub: MockWebServer returns JSON; RestClient exercises the fake downstream.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
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
