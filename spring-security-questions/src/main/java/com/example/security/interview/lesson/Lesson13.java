package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * In-memory {@code UserDetailsService} vs custom bean returning extra authorities—different chains, observable roles.
 */
public final class Lesson13 extends AbstractLesson {

    public Lesson13() {
        super(13, "InMemoryUserDetailsManager vs custom UserDetailsService—ROLE_EXTRA only on custom.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h1 = new WebLessonHarness(InMemoryCfg.class)) {
            h1.mockMvc().perform(get("/who").header("Authorization", "Basic " +
                            java.util.Base64.getEncoder().encodeToString("e:p".getBytes())))
                    .andExpect(status().isForbidden());
        }
        try (WebLessonHarness h2 = new WebLessonHarness(CustomCfg.class)) {
            h2.mockMvc().perform(get("/who").header("Authorization", "Basic " +
                            java.util.Base64.getEncoder().encodeToString("e:p".getBytes())))
                    .andExpect(status().isOk());
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class InMemoryCfg {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> a.anyRequest().hasRole("EXTRA")).httpBasic(Customizer.withDefaults()).build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("e").password("{noop}p").roles("USER").build());
        }

        @Bean
        Api1 api() {
            return new Api1();
        }
    }

    @RestController
    static class Api1 {
        @GetMapping("/who")
        String w() {
            return "inmem";
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class CustomCfg {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> a.anyRequest().hasRole("EXTRA")).httpBasic(Customizer.withDefaults()).build();
        }

        @Bean
        UserDetailsService users() {
            return username -> User.withUsername(username).password("{noop}p")
                    .authorities(AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_EXTRA")).build();
        }

        @Bean
        Api2 api() {
            return new Api2();
        }
    }

    @RestController
    static class Api2 {
        @GetMapping("/who")
        String w() {
            return "custom";
        }
    }
}
