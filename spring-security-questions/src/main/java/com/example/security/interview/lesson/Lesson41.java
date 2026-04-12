package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** AuthenticationEntryPoint (401) vs AccessDeniedHandler (403). */
public final class Lesson41 extends AbstractLesson {

    public Lesson41() {
        super(41, "AuthenticationEntryPoint vs AccessDeniedHandler—401 unauthenticated, 403 wrong role.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/admin/secret")).andExpect(status().isUnauthorized()).andExpect(content().string("unauth"));
            mvc.perform(get("/admin/secret").header("Authorization", "Basic " +
                            java.util.Base64.getEncoder().encodeToString("user:pw".getBytes())))
                    .andExpect(status().isForbidden())
                    .andExpect(content().string("denied"));
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
                    .authorizeHttpRequests(a -> a.requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().permitAll())
                    .httpBasic(Customizer.withDefaults())
                    .exceptionHandling(e -> e
                            .authenticationEntryPoint((req, res, ex) -> {
                                res.setStatus(401);
                                res.setContentType("text/plain;charset=UTF-8");
                                res.getWriter().write("unauth");
                            })
                            .accessDeniedHandler((req, res, ex) -> {
                                res.setStatus(403);
                                res.setContentType("text/plain;charset=UTF-8");
                                res.getWriter().write("denied");
                            }))
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("user").password("{noop}pw").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/admin/secret")
        String s() {
            return "s";
        }
    }
}
