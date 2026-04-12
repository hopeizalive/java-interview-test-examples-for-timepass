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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Logout—session cleared; follow-up request is anonymous again. */
public final class Lesson16 extends AbstractLesson {

    public Lesson16() {
        super(16, "Logout—invalidate session so /me is unauthorized after logout.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            MockHttpSession s = new MockHttpSession();
            mvc.perform(post("/login").param("username", "g").param("password", "p").session(s))
                    .andExpect(status().is3xxRedirection());
            mvc.perform(get("/me").session(s)).andExpect(status().isOk());
            mvc.perform(post("/logout").with(csrf()).session(s)).andExpect(status().is3xxRedirection());
            mvc.perform(get("/me").session(s)).andExpect(status().isUnauthorized());
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Web {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http
                    .csrf(Customizer.withDefaults())
                    .authorizeHttpRequests(a -> a.requestMatchers("/login", "/error").permitAll().anyRequest().authenticated())
                    .formLogin(f -> f.permitAll())
                    .logout(Customizer.withDefaults())
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(User.withUsername("g").password("{noop}p").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/me")
        String me() {
            return "me";
        }
    }
}
