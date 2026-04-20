package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MinimalJacksonWebConfig;
import com.example.microservices.interview.support.SimpleWebHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Lesson 8 demonstrates standardized ProblemDetail error payloads.
 *
 * <p>It uses centralized exception mapping so services can return consistent machine-readable
 * errors across endpoints.
 */
public final class Lesson08 extends AbstractMicroLesson {

    public Lesson08() {
        super(8, "Error handling: ProblemDetail / ErrorResponseException for consistent service errors.");
    }

    /**
     * Lesson 8: consistent error contracts with ProblemDetail.
     *
     * <p><b>Purpose:</b> Show how to return structured HTTP errors for clients.
     * <p><b>Role:</b> Establishes cross-service error-shape consistency.
     * <p><b>Demonstration:</b> Triggers conflict endpoint and verifies ProblemDetail JSON fields.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        // Story action: provoke a known conflict and assert normalized problem payload.
        try (SimpleWebHarness h = new SimpleWebHarness(MinimalJacksonWebConfig.class, WebConfig.class)) {
            h.mockMvc().perform(get("/boom"))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.title").value("inventory-lock"))
                    .andExpect(jsonPath("$.status").value(409));
        }
        // Story takeaway: controller advice keeps error mapping centralized and reusable.
        ctx.log("Talking point: align error bodies across services; @ControllerAdvice centralizes mapping.");
    }

    @Configuration
    @EnableWebMvc
    static class WebConfig {
        @Bean
        DemoApi demoApi() {
            return new DemoApi();
        }

        /** Ensures {@link ErrorResponseException} / {@link ProblemDetail} serialize as JSON under plain WebMvc. */
        @Bean
        ProblemErrors problemErrors() {
            return new ProblemErrors();
        }

        @ControllerAdvice
        static class ProblemErrors extends ResponseEntityExceptionHandler {}
    }

    @RestController
    static class DemoApi {
        @GetMapping("/boom")
        ResponseEntity<Void> boom() {
            ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "sku reserved");
            pd.setTitle("inventory-lock");
            pd.setType(URI.create("https://example.com/problems/inventory-lock"));
            throw new ErrorResponseException(HttpStatus.CONFLICT, pd, null);
        }
    }
}
