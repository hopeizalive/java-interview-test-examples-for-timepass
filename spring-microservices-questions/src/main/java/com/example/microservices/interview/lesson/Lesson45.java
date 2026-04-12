package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.SimpleWebHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** @WebMvcTest-style slice: only web layer + mocked collaborator (manual MockMvc harness). */
public final class Lesson45 extends AbstractMicroLesson {

    public Lesson45() {
        super(45, "@WebMvcTest analog: MockMvc with mocked OrderService bean (no full Spring Boot context).");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        try (SimpleWebHarness h = new SimpleWebHarness(WebConfig.class)) {
            h.mockMvc().perform(get("/orders/7")).andExpect(status().isOk()).andExpect(content().string("mock-7"));
        }
        ctx.log("Talking point: slice tests stay fast; validate JSON with jsonPath.");
    }

    @Configuration
    @EnableWebMvc
    static class WebConfig {
        @Bean
        OrderService orderService() {
            OrderService mock = mock(OrderService.class);
            when(mock.findLabel(7L)).thenReturn("mock-7");
            return mock;
        }

        @Bean
        OrderController orderController(OrderService svc) {
            return new OrderController(svc);
        }
    }

    interface OrderService {
        String findLabel(long id);
    }

    @RestController
    static class OrderController {
        private final OrderService orderService;

        OrderController(OrderService orderService) {
            this.orderService = orderService;
        }

        @GetMapping("/orders/{id}")
        String one(@PathVariable("id") long id) {
            return orderService.findLabel(id);
        }
    }
}
