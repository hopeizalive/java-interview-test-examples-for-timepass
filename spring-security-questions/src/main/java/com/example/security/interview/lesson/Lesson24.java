package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Path matcher pitfall—{@code /api/demo} alone does not match subpaths; {@code /api/demo/**} fixes nested routes.
 */
public final class Lesson24 extends AbstractLesson {

    public Lesson24() {
        super(24, "requestMatchers /api/demo vs /api/demo/**—child path needs **.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        String auth = "Basic " + java.util.Base64.getEncoder().encodeToString("u:p".getBytes());
        try (WebLessonHarness h = new WebLessonHarness(Narrow.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/api/demo").header("Authorization", auth)).andExpect(status().isOk());
            mvc.perform(get("/api/demo/child").header("Authorization", auth)).andExpect(status().isForbidden());
        }
        try (WebLessonHarness h2 = new WebLessonHarness(Fixed.class)) {
            var mvc = h2.mockMvc();
            mvc.perform(get("/api/demo/child").header("Authorization", auth)).andExpect(status().isOk());
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Narrow {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(a -> a.requestMatchers("/api/demo").authenticated().anyRequest().denyAll())
                    .httpBasic(Customizer.withDefaults()).build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(User.withUsername("u").password("{noop}p").roles("USER").build());
        }

        @Bean
        ApiN api() {
            return new ApiN();
        }
    }

    @RestController
    static class ApiN {
        @GetMapping("/api/demo")
        String d() {
            return "d";
        }

        @GetMapping("/api/demo/child")
        String c() {
            return "c";
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Fixed {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(a -> a.requestMatchers("/api/demo", "/api/demo/**").authenticated().anyRequest().denyAll())
                    .httpBasic(Customizer.withDefaults()).build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(User.withUsername("u").password("{noop}p").roles("USER").build());
        }

        @Bean
        ApiF api() {
            return new ApiF();
        }
    }

    @RestController
    static class ApiF {
        @GetMapping("/api/demo/child")
        String c() {
            return "c";
        }
    }
}
