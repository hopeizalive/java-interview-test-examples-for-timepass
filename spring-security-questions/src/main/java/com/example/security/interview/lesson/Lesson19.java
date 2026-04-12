package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Remember-me—cookie allows access without full interactive login when session is new. */
public final class Lesson19 extends AbstractLesson {

    public Lesson19() {
        super(19, "Remember-me—new session + remember-me cookie still reaches /rm.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            MockHttpSession s = new MockHttpSession();
            var login = mvc.perform(post("/login").param("username", "k").param("password", "p")
                            .param("remember-me", "true")
                            .session(s))
                    .andExpect(status().is3xxRedirection())
                    .andReturn();
            String remember = null;
            for (var c : login.getResponse().getCookies()) {
                if ("remember-me".equals(c.getName())) {
                    remember = c.getValue();
                }
            }
            if (remember == null) {
                throw new IllegalStateException("remember-me cookie missing");
            }
            MockHttpSession fresh = new MockHttpSession();
            mvc.perform(get("/rm").session(fresh).cookie(new jakarta.servlet.http.Cookie("remember-me", remember)))
                    .andExpect(status().isOk());
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Web {
        @Bean
        SecurityFilterChain chain(HttpSecurity http, UserDetailsService users) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .rememberMe(r -> r.key("lesson19-key").userDetailsService(users))
                    .authorizeHttpRequests(a -> a.requestMatchers("/login", "/error").permitAll().anyRequest().authenticated())
                    .formLogin(f -> f.permitAll())
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(User.withUsername("k").password("{noop}p").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/rm")
        String rm() {
            return "rm";
        }
    }
}
