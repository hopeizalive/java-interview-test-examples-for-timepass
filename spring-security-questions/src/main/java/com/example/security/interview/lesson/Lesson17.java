package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import jakarta.servlet.http.Cookie;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Session fixation protection—session id changes after authentication. */
public final class Lesson17 extends AbstractLesson {

    public Lesson17() {
        super(17, "Session fixation—JSESSIONID value changes after form login.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            MockHttpSession s = new MockHttpSession();
            MvcResult pre = mvc.perform(get("/warm").session(s)).andExpect(status().isOk()).andReturn();
            String idBefore = sessionId(pre);
            mvc.perform(post("/login").param("username", "h").param("password", "p").session(s))
                    .andExpect(status().is3xxRedirection());
            MvcResult postAuth = mvc.perform(get("/warm").session(s)).andExpect(status().isOk()).andReturn();
            String idAfter = sessionId(postAuth);
            if (idBefore.equals(idAfter)) {
                throw new IllegalStateException("expected new session id after login; before=%s after=%s".formatted(idBefore, idAfter));
            }
            System.out.println("Session id rotated after login.");
        }
    }

    private static String sessionId(MvcResult r) {
        for (Cookie c : r.getResponse().getCookies()) {
            if ("JSESSIONID".equals(c.getName())) {
                return c.getValue();
            }
        }
        return r.getRequest().getSession(false).getId();
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Web {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(s -> s.sessionFixation().changeSessionId())
                    .authorizeHttpRequests(a -> a.requestMatchers("/login", "/error", "/warm").permitAll().anyRequest().authenticated())
                    .formLogin(f -> f.permitAll())
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(User.withUsername("h").password("{noop}p").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/warm")
        String w() {
            return "w";
        }
    }
}
