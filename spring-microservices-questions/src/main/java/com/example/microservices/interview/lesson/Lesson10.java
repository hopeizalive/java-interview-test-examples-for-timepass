package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.SimpleWebHarness;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Lesson 10 introduces correlation-id propagation for distributed tracing.
 *
 * <p>A servlet filter stores request id in MDC and echoes it in response headers so logs from
 * multiple services can be stitched together.
 */
public final class Lesson10 extends AbstractMicroLesson {

    private static final Logger log = LoggerFactory.getLogger(Lesson10.class);

    public Lesson10() {
        super(10, "Correlation ID: OncePerRequestFilter sets MDC and echoes X-Correlation-Id on the response.");
    }

    /**
     * Lesson 10: correlation IDs across service hops.
     *
     * <p><b>Purpose:</b> Demonstrate request-scoped trace identity in logs and headers.
     * <p><b>Role:</b> Foundation for observability in multi-service request paths.
     * <p><b>Demonstration:</b> Sends request with `X-Correlation-Id` and verifies response echo.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        // Story action: call endpoint with client-supplied correlation id and verify propagation.
        try (SimpleWebHarness h = new SimpleWebHarness(WebConfig.class)) {
            h.mockMvc().perform(get("/trace-demo").header("X-Correlation-Id", "client-123"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("ok"))
                    .andExpect(header().string("X-Correlation-Id", "client-123"));
        }
        // Story takeaway: this id should be forwarded on outbound HTTP clients too.
        ctx.log("Talking point: propagate the same id on RestClient/WebClient headers for distributed tracing.");
    }

    @Configuration
    @EnableWebMvc
    static class WebConfig {
        @Bean
        CorrelationFilter correlationFilter() {
            return new CorrelationFilter();
        }

        @Bean
        TraceApi traceApi() {
            return new TraceApi();
        }
    }

    static class CorrelationFilter extends OncePerRequestFilter {
        public static final String CID = "X-Correlation-Id";

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String id = request.getHeader(CID);
            if (id == null || id.isBlank()) {
                id = UUID.randomUUID().toString();
            }
            MDC.put("correlationId", id);
            response.setHeader(CID, id);
            try {
                filterChain.doFilter(request, response);
            } finally {
                MDC.remove("correlationId");
            }
        }
    }

    @RestController
    static class TraceApi {
        @GetMapping("/trace-demo")
        String demo() {
            log.info("handled request correlationId={}", MDC.get("correlationId"));
            return "ok";
        }
    }
}
