package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import jakarta.annotation.security.RolesAllowed;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/** @Secured vs JSR-250 @RolesAllowed—same bean, different annotations. */
public final class Lesson27 extends AbstractLesson {

    public Lesson27() {
        super(27, "@Secured and @RolesAllowed both require ROLE_ADMIN for paired methods.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            Dual d = h.context().getBean(Dual.class);
            try {
                d.secured();
                throw new IllegalStateException("expected deny on @Secured");
            } catch (AccessDeniedException | AuthenticationCredentialsNotFoundException ignored) {
            }
            try {
                d.rolesAllowed();
                throw new IllegalStateException("expected deny on @RolesAllowed");
            } catch (AccessDeniedException | AuthenticationCredentialsNotFoundException ignored) {
            }
            System.out.println("Both JSR-250 and @Secured denied without context authorities.");
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    @EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
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
        Dual dual() {
            return new Dual();
        }
    }

    static class Dual {
        @Secured("ROLE_ADMIN")
        public String secured() {
            return "a";
        }

        @RolesAllowed("ROLE_ADMIN")
        public String rolesAllowed() {
            return "b";
        }
    }
}
