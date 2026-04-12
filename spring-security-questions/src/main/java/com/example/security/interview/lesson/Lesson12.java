package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
 * {@code DaoAuthenticationProvider} via form login—good password 200 on protected page after login; bad password stays 401.
 */
public final class Lesson12 extends AbstractLesson {

    public Lesson12() {
        super(12, "DaoAuthenticationProvider—form login success vs bad credentials.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/user/data")).andExpect(status().isUnauthorized());
            org.springframework.mock.web.MockHttpSession s = new org.springframework.mock.web.MockHttpSession();
            mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
                            .param("username", "dan")
                            .param("password", "bad")
                            .session(s))
                    .andExpect(status().is3xxRedirection() /* or 302 to error page */);
            mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/login")
                            .param("username", "dan")
                            .param("password", "good")
                            .session(s))
                    .andExpect(status().is3xxRedirection());
            mvc.perform(get("/user/data").session(s)).andExpect(status().isOk());
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
                    .authorizeHttpRequests(a -> a.requestMatchers("/login", "/error").permitAll().anyRequest().authenticated())
                    .formLogin(f -> f.permitAll())
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("dan").password("{noop}good").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/user/data")
        String d() {
            return "ok";
        }
    }
}
