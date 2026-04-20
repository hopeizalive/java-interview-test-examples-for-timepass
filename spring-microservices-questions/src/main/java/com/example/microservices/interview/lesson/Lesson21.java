package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.http.HttpClient;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

/**
 * Lesson 21 demonstrates outbound HTTP timeout configuration with RestClient.
 *
 * <p>It compares a fast peer and a delayed peer to show why microservices must bound wait time.
 */
public final class Lesson21 extends AbstractMicroLesson {

    public Lesson21() {
        super(21, "RestClient: JDK ClientHttpRequestFactory enforces read timeout on slow peers.");
    }

    /**
     * Lesson 21: connect/read timeout behavior.
     *
     * <p><b>Purpose:</b> Show fail-fast behavior for slow dependencies.
     * <p><b>Role:</b> Core resilience baseline before retries/circuit breakers.
     * <p><b>Demonstration:</b> Calls fast server successfully, then times out on delayed response.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        // Story phase A: fast server responds within configured timeout budget.
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody("fast").addHeader("Content-Type", "text/plain"));
            server.start();
            String base = "http://127.0.0.1:" + server.getPort();
            JdkClientHttpRequestFactory fastRf = new JdkClientHttpRequestFactory(HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(200))
                    .build());
            fastRf.setReadTimeout(Duration.ofMillis(200));
            RestClient fast = RestClient.builder().baseUrl(base).requestFactory(fastRf).build();
            ctx.log("Fast response: " + fast.get().retrieve().body(String.class));
        }
        // Story phase B: slow peer exceeds read timeout and triggers controlled failure.
        try (MockWebServer slow = new MockWebServer()) {
            slow.enqueue(new MockResponse().setBody("late")
                    .addHeader("Content-Type", "text/plain")
                    .setBodyDelay(2, java.util.concurrent.TimeUnit.SECONDS));
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
            } catch (RestClientException ex) {
                // JDK client may close the body stream on read timeout before message converters finish.
                ctx.log("Slow peer failed as expected: " + ex.getClass().getSimpleName()
                        + (ex.getCause() != null ? " (" + ex.getCause().getMessage() + ")" : ""));
            }
        }
        ctx.log("Talking point: always set timeouts on outbound HTTP in microservices.");
    }
}
