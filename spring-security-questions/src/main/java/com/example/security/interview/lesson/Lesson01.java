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
 * Authentication vs authorization—demonstrate both via one SecurityFilterChain (anonymous denied, authenticated allowed).
 */
public final class Lesson01 extends AbstractLesson {

    public Lesson01() {
        super(1, "Authentication vs authorization—demonstrate both via one SecurityFilterChain (MockMvc).");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/private/ping")).andExpect(status().isUnauthorized());
            mvc.perform(get("/private/ping").header("Authorization", "Basic " +
                    java.util.Base64.getEncoder().encodeToString("alice:x".getBytes())))
                    .andExpect(status().isOk());
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Web {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(a -> a.requestMatchers("/public/**").permitAll().anyRequest().authenticated())
                    .httpBasic(Customizer.withDefaults())
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("alice").password("{noop}x").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/public/ping")
        String pub() {
            return "public";
        }

        @GetMapping("/private/ping")
        String priv() {
            return "private";
        }
    }
}
