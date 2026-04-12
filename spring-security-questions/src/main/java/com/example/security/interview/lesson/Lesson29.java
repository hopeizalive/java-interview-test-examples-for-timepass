package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.Serializable;


/** Custom {@link PermissionEvaluator} with {@code hasPermission} SpEL. */
public final class Lesson29 extends AbstractLesson {

    public Lesson29() {
        super(29, "PermissionEvaluator—hasPermission('doc1','read') gates documentRead.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            Web.Docs d = h.context().getBean(Web.Docs.class);
            var auth = new UsernamePasswordAuthenticationToken(
                    "u", "n/a", AuthorityUtils.createAuthorityList("ROLE_USER"));
            SecurityContextHolder.getContext().setAuthentication(auth);
            try {
                if (!"ok-doc1".equals(d.documentRead("doc1"))) {
                    throw new IllegalStateException("doc1 should be permitted");
                }
                try {
                    d.documentRead("doc2");
                    throw new IllegalStateException("expected deny");
                } catch (AccessDeniedException ok) {
                    System.out.println("doc2 denied: " + ok.getMessage());
                }
            } finally {
                SecurityContextHolder.clearContext();
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
        static PermissionEvaluator permissionEvaluator() {
            return new PermissionEvaluator() {
                @Override
                public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
                    return "doc1".equals(targetDomainObject) && "read".equals(String.valueOf(permission));
                }

                @Override
                public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                    return false;
                }
            };
        }

        @Bean
        Docs docs() {
            return new Docs();
        }

        static class Docs {
            @PreAuthorize("hasPermission(#id, 'read')")
            public String documentRead(String id) {
                return "ok-" + id;
            }
        }
    }
}
