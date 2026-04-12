package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Opaque token—custom {@link OpaqueTokenIntrospector} maps token string to principal. */
public final class Lesson37 extends AbstractLesson {

    public Lesson37() {
        super(36, "Opaque token—introspector accepts token opaque-ok and maps scopes.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/rs/op").header("Authorization", "Bearer opaque-ok")).andExpect(status().isOk());
            mvc.perform(get("/rs/op").header("Authorization", "Bearer bad")).andExpect(status().isUnauthorized());
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
                    .authorizeHttpRequests(a -> a.anyRequest().hasAuthority("SCOPE_read"))
                    .oauth2ResourceServer(o -> o.opaqueToken(op -> op.introspector(introspector())))
                    .build();
        }

        @Bean
        OpaqueTokenIntrospector introspector() {
            return token -> {
                if (!"opaque-ok".equals(token)) {
                    throw new org.springframework.security.oauth2.server.resource.InvalidBearerTokenException("invalid");
                }
                return new DefaultOAuth2AuthenticatedPrincipal("sub-opaque",
                        Map.of("scope", "read"),
                        AuthorityUtils.createAuthorityList("SCOPE_read"));
            };
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/rs/op")
        String op() {
            return "op";
        }
    }
}
