package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Concurrent session control—second login invalidates first session when maxSessions=1. */
public final class Lesson18 extends AbstractLesson {

    public Lesson18() {
        super(18, "Concurrent sessions—maxSessions=1; second form login expires first session.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            MockHttpSession s1 = new MockHttpSession();
            mvc.perform(post("/login").param("username", "j").param("password", "p").session(s1)).andExpect(status().is3xxRedirection());
            mvc.perform(get("/data").session(s1)).andExpect(status().isOk());
            MockHttpSession s2 = new MockHttpSession();
            mvc.perform(post("/login").param("username", "j").param("password", "p").session(s2)).andExpect(status().is3xxRedirection());
            mvc.perform(get("/data").session(s1)).andExpect(status().isUnauthorized());
            mvc.perform(get("/data").session(s2)).andExpect(status().isOk());
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Web {
        @Bean
        SecurityFilterChain chain(HttpSecurity http, SessionRegistry registry) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(s -> s.maximumSessions(1).maxSessionsPreventsLogin(false).sessionRegistry(registry))
                    .authorizeHttpRequests(a -> a.requestMatchers("/login", "/error").permitAll().anyRequest().authenticated())
                    .formLogin(f -> f.permitAll())
                    .build();
        }

        @Bean
        SessionRegistry sessionRegistry() {
            return new SessionRegistryImpl();
        }

        @Bean
        static HttpSessionEventPublisher httpSessionEventPublisher() {
            return new HttpSessionEventPublisher();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(User.withUsername("j").password("{noop}p").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/data")
        String d() {
            return "d";
        }
    }
}
