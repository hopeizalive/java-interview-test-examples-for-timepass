package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Custom {@link SecurityContext} via {@code securityContext} request post-processor. */
public final class Lesson48 extends AbstractLesson {

    public Lesson48() {
        super(47, "securityContext() post-processor—custom Authentication in SecurityContext.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        SecurityContext sc = SecurityContextHolder.createEmptyContext();
        sc.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(
                "ctx-user", "n/a", AuthorityUtils.createAuthorityList("ROLE_USER")));
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            h.mockMvc().perform(get("/name").with(securityContext(sc)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("ctx-user"));
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Web {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> a.anyRequest().authenticated()).build();
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/name")
        String n(org.springframework.security.core.Authentication a) {
            return a.getName();
        }
    }
}
