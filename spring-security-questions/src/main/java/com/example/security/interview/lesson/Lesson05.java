package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Multiple {@code SecurityFilterChain} beans—{@code securityMatcher} order and different rules per path.
 */
public final class Lesson05 extends AbstractLesson {

    public Lesson05() {
        super(5, "Multiple SecurityFilterChain beans—/api/** open, /** authenticated.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/api/open")).andExpect(status().isOk()).andExpect(content().string("api"));
            mvc.perform(get("/app/closed")).andExpect(status().isUnauthorized());
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Web {
        @Bean
        @Order(1)
        SecurityFilterChain api(HttpSecurity http) throws Exception {
            return http.securityMatcher("/api/**")
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                    .build();
        }

        @Bean
        @Order(2)
        SecurityFilterChain app(HttpSecurity http) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(a -> a.anyRequest().authenticated())
                    .httpBasic(Customizer.withDefaults())
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("u").password("{noop}p").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/api/open")
        String api() {
            return "api";
        }

        @GetMapping("/app/closed")
        String app() {
            return "app";
        }
    }
}
