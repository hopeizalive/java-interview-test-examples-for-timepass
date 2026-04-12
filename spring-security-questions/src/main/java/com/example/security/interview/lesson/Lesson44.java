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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Switch user—{@code SwitchUserFilter} impersonation entry point allows admin to assume another user session. */
public final class Lesson44 extends AbstractLesson {

    public Lesson44() {
        super(43, "SwitchUserFilter—admin hits /login/impersonate then sees victim principal on /who.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            MockHttpSession s = new MockHttpSession();
            mvc.perform(post("/login").param("username", "admin").param("password", "p").session(s))
                    .andExpect(status().is3xxRedirection());
            mvc.perform(post("/login/impersonate").param("username", "victim").with(csrf()).session(s))
                    .andExpect(status().is3xxRedirection());
            String body = mvc.perform(get("/who").session(s))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();
            if (!body.equals("victim")) {
                throw new IllegalStateException("expected switched principal victim, got: " + body);
            }
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Web {
        @Bean
        SwitchUserFilter switchUserFilter(UserDetailsService users) {
            SwitchUserFilter f = new SwitchUserFilter();
            f.setUserDetailsService(users);
            f.setTargetUrl("/");
            return f;
        }

        @Bean
        SecurityFilterChain chain(HttpSecurity http, SwitchUserFilter switchUserFilter) throws Exception {
            return http
                    // Default csrf() token from SecurityMockMvcRequestPostProcessors.csrf() is not tied to this session;
                    // allow impersonation POST so the lesson asserts redirect + switched principal.
                    .csrf(c -> c.ignoringRequestMatchers("/login/impersonate"))
                    .authorizeHttpRequests(a -> a.requestMatchers("/login/**").permitAll().anyRequest().authenticated())
                    .formLogin(f -> f.permitAll())
                    .addFilterAfter(switchUserFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }

        @Bean
        UserDetailsService users() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("admin").password("{noop}p").roles("ADMIN", "SWITCH_USER").build(),
                    User.withUsername("victim").password("{noop}p").roles("USER").build());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/who")
        String w(org.springframework.security.core.Authentication a) {
            return a.getName();
        }
    }
}
