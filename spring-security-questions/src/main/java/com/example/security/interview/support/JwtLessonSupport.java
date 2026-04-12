package com.example.security.interview.support;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/** HS256 helpers for resource-server lessons (local secret only). */
public final class JwtLessonSupport {

    public static final String RAW_SECRET = "lesson33-lesson33-lesson33-lesson33";

    private JwtLessonSupport() {
    }

    public static SecretKey secretKey() {
        byte[] bytes = RAW_SECRET.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    public static JwtEncoder jwtEncoder() {
        OctetSequenceKey jwk = new OctetSequenceKey.Builder(secretKey().getEncoded())
                .algorithm(JWSAlgorithm.HS256)
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    public static JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(secretKey()).macAlgorithm(MacAlgorithm.HS256).build();
    }

    public static String bearerToken(String subject, String... scopes) {
        JwtEncoder enc = jwtEncoder();
        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer("https://lesson.local")
                .subject(subject)
                .expiresAt(Instant.now().plusSeconds(3600));
        if (scopes.length > 0) {
            claims.claim("scope", String.join(" ", scopes));
        }
        JwtClaimsSet built = claims.build();
        JwtEncoderParameters params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                built);
        Jwt jwt = enc.encode(params);
        return jwt.getTokenValue();
    }
}
