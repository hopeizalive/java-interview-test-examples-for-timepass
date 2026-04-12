package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
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

/** Exception translation—{@link AccessDeniedException} yields custom body from access-denied handler. */
public final class Lesson42 extends AbstractLesson {

    public Lesson42() {
        super(41, "ExceptionTranslationFilter—AccessDeniedException mapped to custom 403 body.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/vip").header("Authorization", "Basic " +
                            java.util.Base64.getEncoder().encodeToString("guest:pw".getBytes())))
                    .andExpect(status().isForbidden())
                    .andExpect(content().string("access-denied-handled"));
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
                    .authorizeHttpRequests(a -> a.requestMatchers("/vip").hasRole("VIP").anyRequest().permitAll())
                    .httpBasic(Customizer.withDefaults())
                    .exceptionHandling(e -> e.accessDeniedHandler((req, res, ex) -> {
                        res.setStatus(403);
                        res.setContentType("text/plain;charset=UTF-8");
                        res.getWriter().write("access-denied-handled");
                    }))
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("guest").password("{noop}pw").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/vip")
        String v() {
            return "v";
        }
    }
}
