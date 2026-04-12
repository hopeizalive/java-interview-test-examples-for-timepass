package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import com.example.security.interview.support.JwtLessonSupport;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.time.Instant;
import java.util.List;

/**
 * OIDC-style ID token—locally signed JWT; decode and read profile claims with {@link JwtDecoder}.
 */
public final class Lesson36 extends AbstractLesson {

    public Lesson36() {
        super(35, "OIDC ID token shape—iss, aud, email claims decoded locally.");
    }

    @Override
    public void run(SecurityStudyContext ctx) {
        var enc = JwtLessonSupport.jwtEncoder();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("https://lesson-oidc")
                .subject("user-42")
                .audience(List.of("lesson-client"))
                .claim("email", "user@lesson.local")
                .expiresAt(Instant.now().plusSeconds(300))
                .build();
        var jwt = enc.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims));

        JwtDecoder decoder = NimbusJwtDecoder.withSecretKey(JwtLessonSupport.secretKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        Jwt parsed = decoder.decode(jwt.getTokenValue());
        if (!"user@lesson.local".equals(parsed.getClaimAsString("email"))) {
            throw new IllegalStateException("email claim");
        }
        if (!parsed.getAudience().contains("lesson-client")) {
            throw new IllegalStateException("audience");
        }
        System.out.println("ID token subject=" + parsed.getSubject() + " email=" + parsed.getClaimAsString("email"));
    }
}
