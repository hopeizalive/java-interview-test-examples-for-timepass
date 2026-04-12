package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MinimalJacksonWebConfig;
import com.example.microservices.interview.support.SimpleWebHarness;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** DTOs + jakarta.validation for request contracts. */
public final class Lesson06 extends AbstractMicroLesson {

    public Lesson06() {
        super(6, "DTOs + Bean Validation: invalid JSON body yields 400 via @Valid.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        try (SimpleWebHarness h = new SimpleWebHarness(MinimalJacksonWebConfig.class, WebConfig.class)) {
            var mvc = h.mockMvc();
            mvc.perform(post("/register").contentType("application/json").content("{\"email\":\"\"}"))
                    .andExpect(status().isBadRequest());
            mvc.perform(post("/register").contentType("application/json").content("{\"email\":\"ok@x\"}"))
                    .andExpect(status().isOk());
        }
        ctx.log("Talking point: validate at the edge; keep domain invariants inside the service as well.");
    }

    @Configuration
    @EnableWebMvc
    static class WebConfig {
        @Bean
        static MethodValidationPostProcessor mvp() {
            return new MethodValidationPostProcessor();
        }

        @Bean
        LocalValidatorFactoryBean validator() {
            return new LocalValidatorFactoryBean();
        }

        @Bean
        RegisterApi api() {
            return new RegisterApi();
        }

        @Bean
        ValidationErrors errors() {
            return new ValidationErrors();
        }
    }

    record RegisterRequest(@NotBlank String email) {}

    @RestController
    static class RegisterApi {
        @PostMapping("/register")
        String register(@Valid @RequestBody RegisterRequest body) {
            return "ok:" + body.email();
        }
    }

    @RestControllerAdvice
    static class ValidationErrors {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        org.springframework.http.ResponseEntity<String> handle(MethodArgumentNotValidException ex) {
            return org.springframework.http.ResponseEntity.badRequest().body("invalid");
        }
    }
}
