package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Custom {@link UserDetails} principal resolved with {@link AuthenticationPrincipal}. */
public final class Lesson28 extends AbstractLesson {

    public Lesson28() {
        super(28, "@AuthenticationPrincipal CustomUserDetails exposes custom label field.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            h.mockMvc().perform(get("/whoami").header("Authorization", "Basic " +
                            java.util.Base64.getEncoder().encodeToString("pat:pw".getBytes())))
                    .andExpect(status().isOk())
                    .andExpect(content().string("pat-custom"));
        }
    }

    static final class CustomUserDetails extends org.springframework.security.core.userdetails.User {
        CustomUserDetails(String username) {
            super(username, "{noop}pw", AuthorityUtils.createAuthorityList("ROLE_USER"));
        }

        String label() {
            return "custom";
        }
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class Web {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> a.anyRequest().authenticated())
                    .httpBasic(Customizer.withDefaults()).build();
        }

        @Bean
        UserDetailsService users() {
            return username -> {
                if ("pat".equals(username)) {
                    return new CustomUserDetails("pat");
                }
                throw new UsernameNotFoundException(username);
            };
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/whoami")
        String who(@AuthenticationPrincipal CustomUserDetails u) {
            return u.getUsername() + "-" + u.label();
        }
    }
}
