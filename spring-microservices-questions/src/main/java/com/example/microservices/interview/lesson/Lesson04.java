package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.MinimalJacksonWebConfig;
import com.example.microservices.interview.support.SimpleWebHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Layering: controller → service → repository; expose DTO not JPA entity. */
public final class Lesson04 extends AbstractMicroLesson {

    public Lesson04() {
        super(4, "Layering: REST returns a record DTO; domain entity stays inside the service layer.");
    }

    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        try (SimpleWebHarness h = new SimpleWebHarness(MinimalJacksonWebConfig.class, WebConfig.class)) {
            h.mockMvc().perform(get("/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("alice"));
        }
        ctx.log("Talking point: do not leak JPA entities over HTTP—versioning, lazy fields, and API stability suffer.");
    }

    @Configuration
    @EnableWebMvc
    static class WebConfig {
        @Bean
        UserController userController(UserService svc) {
            return new UserController(svc);
        }

        @Bean
        UserService userService(UserRepository repo) {
            return new UserService(repo);
        }

        @Bean
        UserRepository userRepository() {
            return new UserRepository();
        }
    }

    static class UserEntity {
        final long id;
        final String name;
        final String internalSecret;

        UserEntity(long id, String name, String internalSecret) {
            this.id = id;
            this.name = name;
            this.internalSecret = internalSecret;
        }
    }

    record UserResponse(long id, String name) {}

    static class UserRepository {
        UserEntity findById(long id) {
            return new UserEntity(id, "alice", "secret");
        }
    }

    static class UserService {
        private final UserRepository repo;

        UserService(UserRepository repo) {
            this.repo = repo;
        }

        UserResponse asDto(long id) {
            UserEntity e = repo.findById(id);
            return new UserResponse(e.id, e.name);
        }
    }

    @RestController
    static class UserController {
        private final UserService userService;

        UserController(UserService userService) {
            this.userService = userService;
        }

        @GetMapping("/users/{id}")
        UserResponse get(@PathVariable("id") long id) {
            return userService.asDto(id);
        }
    }
}
