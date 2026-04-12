package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Filter order—custom {@link OncePerRequestFilter} before and after {@link UsernamePasswordAuthenticationFilter}.
 */
public final class Lesson08 extends AbstractLesson {

    public Lesson08() {
        super(8, "Filter order—OncePerRequestFilter before and after UsernamePasswordAuthenticationFilter.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        OrderedFilters.events.clear();
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            h.mockMvc().perform(get("/ping").header("Authorization", "Basic " +
                    java.util.Base64.getEncoder().encodeToString("u:p".getBytes()))).andExpect(status().isOk());
            List<String> ev = List.copyOf(OrderedFilters.events);
            if (ev.size() < 3 || !ev.get(0).equals("before-auth")
                    || !ev.get(ev.size() - 1).equals("after-auth")) {
                throw new IllegalStateException("unexpected filter order: " + ev);
            }
            System.out.println("Filter order trace: " + ev);
        }
    }

    static final class OrderedFilters {
        static final List<String> events = new ArrayList<>();
    }

    static class BeforeAuthFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            OrderedFilters.events.add("before-auth");
            filterChain.doFilter(request, response);
        }
    }

    static class AfterAuthFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            filterChain.doFilter(request, response);
            OrderedFilters.events.add("after-auth");
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
                    .addFilterBefore(new BeforeAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(new AfterAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeHttpRequests(a -> a.anyRequest().authenticated())
                    .httpBasic(Customizer.withDefaults())
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("u").password("{noop}p").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/ping")
        String ping() {
            return "pong";
        }
    }
}
