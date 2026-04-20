package com.example.microservices.interview.lesson;

import com.example.microservices.interview.study.MicroservicesStudyContext;
import com.example.microservices.interview.support.SecurityWebHarness;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Lesson 39 demonstrates OAuth2 resource-server JWT validation.
 *
 * <p>A signed token is minted and verified through Spring Security resource-server flow.
 */
public final class Lesson39 extends AbstractMicroLesson {

    private static final String SECRET = "0123456789abcdef0123456789abcdef";

    public Lesson39() {
        super(35, "OAuth2 resource server: JWT Bearer token validated with JwtDecoder + oauth2ResourceServer().");
    }

    /**
     * Lesson 35/39: Bearer token validation flow.
     *
     * <p><b>Purpose:</b> Show protected endpoint access with valid JWT.
     * <p><b>Role:</b> Security baseline for service-to-service and client-to-service calls.
     * <p><b>Demonstration:</b> Sends Bearer token to secured endpoint and asserts success.
     */
    @Override
    public void run(MicroservicesStudyContext ctx) throws Exception {
        // Story setup: mint short-lived demo token matching decoder settings.
        String token = mintHs256Jwt();
        try (SecurityWebHarness h = new SecurityWebHarness(WebConfig.class)) {
            h.mockMvc().perform(get("/api/who").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(content().string("jwt-ok"));
        }
        ctx.log("Talking point: in production use RS256 + JWKS; validate iss/aud/exp per your IdP contract.");
    }

    private static String mintHs256Jwt() throws Exception {
        byte[] secret = SECRET.getBytes(StandardCharsets.UTF_8);
        JWSSigner signer = new MACSigner(secret);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject("svc-caller")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 60_000))
                .claim("scope", "read")
                .build();
        SignedJWT sj = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        sj.sign(signer);
        return sj.serialize();
    }

    @Configuration
    @EnableWebSecurity
    @EnableWebMvc
    static class WebConfig {
        @Bean
        SecurityFilterChain chain(HttpSecurity http) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(a -> a.anyRequest().authenticated())
                    .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()))
                    .build();
        }

        @Bean
        JwtDecoder jwtDecoder() {
            return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"))
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }

        @Bean
        Api api() {
            return new Api();
        }
    }

    @RestController
    static class Api {
        @GetMapping("/api/who")
        String who() {
            return "jwt-ok";
        }
    }
}
