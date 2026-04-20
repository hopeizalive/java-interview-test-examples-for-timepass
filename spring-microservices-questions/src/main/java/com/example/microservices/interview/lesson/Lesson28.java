package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.SimpleWebHarness;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Lesson 28 demonstrates Backend-for-Frontend (BFF) response composition.
 *
 * <p>The BFF endpoint calls user and order services and returns one client-shaped payload.
 */
public final class Lesson28 extends AbstractMicroLesson {

    private static final String P_USERS = "lesson28.users";
    private static final String P_ORDERS = "lesson28.orders";

    public Lesson28() {
        super(28, "Backend-for-frontend: one controller composes two MockWebServer downstreams via RestClient.");
    }

    /**
     * Lesson 28: BFF aggregation flow.
     *
     * <p><b>Purpose:</b> Show client-specific aggregation from multiple downstream services.
     * <p><b>Role:</b> Distinguishes BFF from generic edge routing.
     * <p><b>Demonstration:</b> Stubs two services and verifies composed `/mobile/me-summary` response.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        // Story setup: prepare independent downstream stubs for user and order data.
        try (MockWebServer users = new MockWebServer(); MockWebServer orders = new MockWebServer()) {
            users.enqueue(new MockResponse().setBody("alice"));
            orders.enqueue(new MockResponse().setBody("ORD-9"));
            users.start();
            orders.start();
            System.setProperty(P_USERS, "http://127.0.0.1:" + users.getPort());
            System.setProperty(P_ORDERS, "http://127.0.0.1:" + orders.getPort());
            try (SimpleWebHarness h = new SimpleWebHarness(WebConfig.class)) {
                h.mockMvc().perform(get("/mobile/me-summary"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("user=alice;lastOrder=ORD-9"));
            } finally {
                System.clearProperty(P_USERS);
                System.clearProperty(P_ORDERS);
            }
        }
        ctx.log("Talking point: BFF shapes payloads for one client; generic gateways stay thin.");
    }

    @Configuration
    @EnableWebMvc
    static class WebConfig {
        @Bean
        BffController bffController() {
            String userBase = System.getProperty(P_USERS);
            String orderBase = System.getProperty(P_ORDERS);
            RestClient users = RestClient.builder().baseUrl(userBase).build();
            RestClient orders = RestClient.builder().baseUrl(orderBase).build();
            return new BffController(users, orders);
        }
    }

    @RestController
    static class BffController {
        private final RestClient users;
        private final RestClient orders;

        BffController(RestClient users, RestClient orders) {
            this.users = users;
            this.orders = orders;
        }

        @GetMapping("/mobile/me-summary")
        String summary() {
            String u = users.get().uri("/name").retrieve().body(String.class);
            String o = orders.get().uri("/last").retrieve().body(String.class);
            return "user=" + u + ";lastOrder=" + o;
        }
    }
}
