package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.SimpleWebHarness;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** API gateway role (minimal): edge filter adds routing/auth-style metadata headers. */
public final class Lesson27 extends AbstractMicroLesson {

    public Lesson27() {
        super(27, "API Gateway pattern: edge OncePerRequestFilter stamps X-Gateway-Route before controllers.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        try (SimpleWebHarness h = new SimpleWebHarness(WebConfig.class)) {
            h.mockMvc().perform(get("/upstream/ping"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("X-Gateway-Route", "demo-route"));
        }
        ctx.log("Talking point: Spring Cloud Gateway does this on Netty; same ideas apply at the edge.");
    }

    @Configuration
    @EnableWebMvc
    static class WebConfig {
        @Bean
        GatewayStubFilter gatewayStubFilter() {
            return new GatewayStubFilter();
        }

        @Bean
        UpstreamApi upstreamApi() {
            return new UpstreamApi();
        }
    }

    static class GatewayStubFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            response.setHeader("X-Gateway-Route", "demo-route");
            filterChain.doFilter(request, response);
        }
    }

    @RestController
    static class UpstreamApi {
        @GetMapping("/upstream/ping")
        String ping() {
            return "pong";
        }
    }
}
