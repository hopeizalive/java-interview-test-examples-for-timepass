package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

/**
 * Lesson 40 demonstrates OAuth2 client-credentials token retrieval.
 *
 * <p>A stub auth server returns token JSON and the lesson extracts `access_token`.
 */
public final class Lesson40 extends AbstractMicroLesson {

    public Lesson40() {
        super(36, "OAuth2 client: stub token endpoint JSON; RestClient retrieves access_token for service calls.");
    }

    /**
     * Lesson 36/40: service token acquisition flow.
     *
     * <p><b>Purpose:</b> Show machine-to-machine token exchange pattern.
     * <p><b>Role:</b> Complements resource-server validation with client-side auth flow.
     * <p><b>Demonstration:</b> Posts grant request and parses returned token JSON.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        // Story setup: stub authorization endpoint with deterministic token payload.
        try (MockWebServer auth = new MockWebServer()) {
            auth.enqueue(new MockResponse()
                    .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .setBody("{\"access_token\":\"client-secret-token\",\"token_type\":\"Bearer\",\"expires_in\":300}"));
            auth.start();
            String body = RestClient.create().post()
                    .uri(auth.url("/oauth/token").toString())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body("grant_type=client_credentials")
                    .retrieve()
                    .body(String.class);
            JsonNode node = new ObjectMapper().readTree(body);
            ctx.log("access_token prefix: " + node.get("access_token").asText().substring(0, 8) + "...");
        }
        ctx.log("Talking point: Spring OAuth2AuthorizedClientManager caches tokens; use client_credentials for service-to-service.");
    }
}
