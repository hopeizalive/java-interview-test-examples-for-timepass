package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/** @PreAuthorize / @PostAuthorize—post condition fails on return value. */
public final class Lesson26 extends AbstractLesson {

    public Lesson26() {
        super(26, "@PostAuthorize—return value must equal 'ok' for USER.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            Gate g = h.context().getBean(Gate.class);
            try {
                g.badReturn();
                throw new IllegalStateException("expected AccessDeniedException");
            } catch (AccessDeniedException e) {
                System.out.println("Post-authorize blocked bad return: " + e.getMessage());
            }
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    @EnableMethodSecurity
    static class Web {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> a.anyRequest().permitAll()).build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(User.withUsername("u").password("{noop}p").roles("USER").build());
        }

        @Bean
        Gate gate() {
            return new Gate();
        }
    }

    static class Gate {
        @PreAuthorize("hasRole('USER')")
        @org.springframework.security.access.prepost.PostAuthorize("returnObject == 'ok'")
        public String badReturn() {
            return "blocked";
        }
    }
}
