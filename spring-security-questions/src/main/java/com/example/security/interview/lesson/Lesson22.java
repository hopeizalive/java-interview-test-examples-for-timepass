package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * fullyAuthenticated vs remember-me-only—programmatic {@link RememberMeAuthenticationToken} denied for /strict by
 * {@code fullyAuthenticated()}.
 */
public final class Lesson22 extends AbstractLesson {

    public Lesson22() {
        super(22, "fullyAuthenticated() rejects RememberMeAuthenticationToken for /strict/**.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            RememberMeAuthenticationToken remember = new RememberMeAuthenticationToken(
                    "key", User.withUsername("m").password("n/a").roles("USER").build(),
                    AuthorityUtils.createAuthorityList("ROLE_USER"));
            mvc.perform(get("/strict/x").with(authentication(remember))).andExpect(status().isUnauthorized());
            mvc.perform(get("/strict/x").header("Authorization", "Basic " +
                            java.util.Base64.getEncoder().encodeToString("m:p".getBytes(StandardCharsets.UTF_8))))
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
                    .authorizeHttpRequests(a -> a.requestMatchers("/strict/**").fullyAuthenticated().anyRequest().permitAll())
                    .httpBasic(org.springframework.security.config.Customizer.withDefaults())
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(User.withUsername("m").password("{noop}p").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/strict/x")
        String s() {
            return "s";
        }
    }
}
