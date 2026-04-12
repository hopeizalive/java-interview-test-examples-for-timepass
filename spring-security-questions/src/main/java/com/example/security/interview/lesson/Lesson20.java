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

/** authorizeHttpRequests + requestMatchers—role-based URL access. */
public final class Lesson20 extends AbstractLesson {

    public Lesson20() {
        super(20, "authorizeHttpRequests + requestMatchers—hasRole USER on /u/**.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/u/1").header("Authorization", "Basic " +
                            java.util.Base64.getEncoder().encodeToString("r:pw".getBytes())))
                    .andExpect(status().isOk());
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Web {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> a.requestMatchers("/u/**").hasRole("USER").anyRequest().denyAll())
                    .httpBasic(Customizer.withDefaults()).build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(User.withUsername("r").password("{noop}pw").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/u/1")
        String u() {
            return "u";
        }
    }
}
