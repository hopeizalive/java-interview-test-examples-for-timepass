package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterChainProxy;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

/** Reactive {@link SecurityWebFilterChain}—unauthenticated /api/** gets 401; {@code mockUser} passes security. */
public final class Lesson50 extends AbstractLesson {

    public Lesson50() {
        super(49, "WebFlux SecurityWebFilterChain—/api/** authenticated; WebTestClient + mockUser.");
    }

    @Override
    public void run(SecurityStudyContext ctx) {
        RouterFunction<ServerResponse> router = RouterFunctions.route(
                GET("/api/ping"),
                req -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue("pong"));

        SecurityWebFilterChain security = ServerHttpSecurity.http()
                .authorizeExchange(ex -> ex.pathMatchers("/api/**").authenticated().anyExchange().permitAll())
                .build();

        WebTestClient client = WebTestClient.bindToRouterFunction(router)
                .apply(springSecurity())
                .webFilter(new WebFilterChainProxy(security))
                .build();

        client.get().uri("/api/ping").exchange().expectStatus().isUnauthorized();

        client.mutateWith(mockUser("reactive").roles("USER"))
                .get()
                .uri("/api/ping")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("pong");
    }
}
