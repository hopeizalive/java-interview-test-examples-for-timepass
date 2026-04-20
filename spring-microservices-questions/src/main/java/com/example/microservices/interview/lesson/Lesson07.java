package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.SimpleWebHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Lesson 7 demonstrates API versioning with parallel URL namespaces.
 *
 * <p>The example keeps both versions active in one app so compatibility and migration concerns are
 * visible to students.
 */
public final class Lesson07 extends AbstractMicroLesson {

    public Lesson07() {
        super(7, "API versioning: parallel /v1 and /v2 controllers on the same servlet context.");
    }

    /**
     * Lesson 7: URL-path API versioning.
     *
     * <p><b>Purpose:</b> Show coexistence of v1 and v2 contracts.
     * <p><b>Role:</b> Introduces backward compatibility strategy for evolving APIs.
     * <p><b>Demonstration:</b> Calls both `/v1/greet` and `/v2/greet` and verifies separate outputs.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        // Story action: assert both versions are routable and return expected contract variants.
        try (SimpleWebHarness h = new SimpleWebHarness(WebConfig.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/v1/greet")).andExpect(status().isOk()).andExpect(content().string("hello-v1"));
            mvc.perform(get("/v2/greet")).andExpect(status().isOk()).andExpect(content().string("hello-v2"));
        }
        ctx.log("Talking point: URL vs Accept/Custom header versioning—Spring supports both via routing.");
    }

    @Configuration
    @EnableWebMvc
    static class WebConfig {
        @Bean
        V1 v1() {
            return new V1();
        }

        @Bean
        V2 v2() {
            return new V2();
        }
    }

    @RestController
    static class V1 {
        @GetMapping("/v1/greet")
        String greet() {
            return "hello-v1";
        }
    }

    @RestController
    static class V2 {
        @GetMapping("/v2/greet")
        String greet() {
            return "hello-v2";
        }
    }
}
