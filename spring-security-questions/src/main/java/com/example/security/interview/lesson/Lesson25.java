package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/** @EnableMethodSecurity—annotations enforced on a bean. */
public final class Lesson25 extends AbstractLesson {

    public Lesson25() {
        super(25, "@EnableMethodSecurity—@PreAuthorize blocks securedMethod without ROLE_ADMIN.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            SecuredService svc = h.context().getBean(SecuredService.class);
            try {
                svc.securedMethod();
                throw new IllegalStateException("expected AccessDeniedException");
            } catch (AccessDeniedException | AuthenticationCredentialsNotFoundException ok) {
                System.out.println("Denied without authority as expected");
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
        SecuredService securedService() {
            return new SecuredService();
        }
    }

    static class SecuredService {
        @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
        public String securedMethod() {
            return "secret";
        }
    }
}
