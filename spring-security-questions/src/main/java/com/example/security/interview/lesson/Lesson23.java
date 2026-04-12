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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** hasAuthority / hasAnyRole—SCOPE_read vs ROLE_ADMIN. */
public final class Lesson23 extends AbstractLesson {

    public Lesson23() {
        super(23, "hasAuthority SCOPE_read vs hasAnyRole('ADMIN','USER').");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        String basicScope = java.util.Base64.getEncoder().encodeToString("scopeuser:p".getBytes());
        String basicAdmin = java.util.Base64.getEncoder().encodeToString("admin:p".getBytes());
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/scoped").header("Authorization", "Basic " + basicScope)).andExpect(status().isOk());
            mvc.perform(get("/either").header("Authorization", "Basic " + basicAdmin)).andExpect(status().isOk());
            mvc.perform(get("/either").header("Authorization", "Basic " + basicScope)).andExpect(status().isOk());
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
                    .authorizeHttpRequests(a -> a.requestMatchers("/scoped").hasAuthority("SCOPE_read")
                            .requestMatchers("/either").hasAnyRole("ADMIN", "USER")
                            .anyRequest().denyAll())
                    .httpBasic(Customizer.withDefaults())
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("scopeuser").password("{noop}p").authorities("SCOPE_read").build(),
                    User.withUsername("admin").password("{noop}p").roles("ADMIN").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/scoped")
        String s() {
            return "s";
        }

        @GetMapping("/either")
        String e() {
            return "e";
        }
    }
}
