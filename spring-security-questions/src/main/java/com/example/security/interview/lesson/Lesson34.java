package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.JwtLessonSupport;
import com.example.security.interview.support.WebLessonHarness;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** JwtAuthenticationConverter maps {@code roles} claim to {@code GrantedAuthority}. */
public final class Lesson34 extends AbstractLesson {

    public Lesson34() {
        super(33, "JwtAuthenticationConverter—roles claim becomes ROLE_* authorities.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        var enc = JwtLessonSupport.jwtEncoder();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("https://lesson.local")
                .subject("admin-user")
                .claim("roles", "ADMIN USER")
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        var jwt = enc.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims));
        try (WebLessonHarness h = new WebLessonHarness(Web.class)) {
            var mvc = h.mockMvc();
            mvc.perform(get("/admin/jwt").header("Authorization", "Bearer " + jwt.getTokenValue()))
                    .andExpect(status().isOk());
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
                    .authorizeHttpRequests(a -> a.requestMatchers("/admin/**").hasRole("ADMIN"))
                    .oauth2ResourceServer(o -> o.jwt(j -> j.jwtAuthenticationConverter(converter())))
                    .build();
        }

        @Bean
        JwtDecoder jwtDecoder() {
            return JwtLessonSupport.jwtDecoder();
        }

        static JwtAuthenticationConverter converter() {
            JwtAuthenticationConverter c = new JwtAuthenticationConverter();
            c.setJwtGrantedAuthoritiesConverter(jwt -> rolesFromClaim(jwt));
            return c;
        }

        private static Collection<GrantedAuthority> rolesFromClaim(org.springframework.security.oauth2.jwt.Jwt jwt) {
            String r = jwt.getClaimAsString("roles");
            if (r == null) {
                return List.of();
            }
            return Stream.of(r.split("\\s+")).map(s -> new SimpleGrantedAuthority("ROLE_" + s)).collect(Collectors.toList());
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/admin/jwt")
        String a() {
            return "a";
        }
    }
}
