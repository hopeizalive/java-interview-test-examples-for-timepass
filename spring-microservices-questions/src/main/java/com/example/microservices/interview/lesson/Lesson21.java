package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

/** RestClient with connect/read timeouts. */
public final class Lesson21 extends AbstractMicroLesson {

    public Lesson21() {
        super(21, "RestClient: JDK ClientHttpRequestFactory enforces read timeout on slow peers.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody("fast"));
            server.start();
            String base = "http://127.0.0.1:" + server.getPort();
            JdkClientHttpRequestFactory fastRf = new JdkClientHttpRequestFactory(HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(200))
                    .build());
            fastRf.setReadTimeout(Duration.ofMillis(200));
            RestClient fast = RestClient.builder().baseUrl(base).requestFactory(fastRf).build();
            ctx.log("Fast response: " + fast.get().retrieve().body(String.class));
        }
        try (MockWebServer slow = new MockWebServer()) {
            slow.enqueue(new MockResponse().setBody("late").setBodyDelay(2, java.util.concurrent.TimeUnit.SECONDS));
            slow.start();
            String base = "http://127.0.0.1:" + slow.getPort();
            JdkClientHttpRequestFactory slowRf = new JdkClientHttpRequestFactory(HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(200))
                    .build());
            slowRf.setReadTimeout(Duration.ofMillis(200));
            RestClient impatient = RestClient.builder().baseUrl(base).requestFactory(slowRf).build();
            try {
                impatient.get().retrieve().body(String.class);
                ctx.log("Unexpected: slow server responded in time");
            } catch (ResourceAccessException ex) {
                Throwable cause = ex.getCause();
                ctx.log("Timed out as expected: " + (cause instanceof HttpTimeoutException ? "http timeout" : cause));
            }
        }
        ctx.log("Talking point: always set timeouts on outbound HTTP in microservices.");
    }
}
