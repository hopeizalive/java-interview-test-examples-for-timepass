package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

/**
 * OAuth2 client credentials—token endpoint served by {@link MockWebServer}; exchange yields access token.
 */
public final class Lesson35 extends AbstractLesson {

    public Lesson35() {
        super(35, "OAuth2 client credentials—ClientRegistration + token JSON from MockWebServer.");
    }

    @Override
    public void run(SecurityStudyContext ctx) throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.start();
            server.enqueue(new MockResponse()
                    .setBody("{\"access_token\":\"lesson35-token\",\"token_type\":\"Bearer\",\"expires_in\":3600}")
                    .addHeader("Content-Type", "application/json"));

            String tokenUri = server.url("/oauth2/token").toString();

            ClientRegistration reg = ClientRegistration.withRegistrationId("lesson35")
                    .clientId("cid")
                    .clientSecret("sec")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .tokenUri(tokenUri)
                    .build();

            var client = new DefaultClientCredentialsTokenResponseClient();
            OAuth2AccessTokenResponse response = client.getTokenResponse(new OAuth2ClientCredentialsGrantRequest(reg));
            if (!"lesson35-token".equals(response.getAccessToken().getTokenValue())) {
                throw new IllegalStateException("unexpected token");
            }
            var rec = server.takeRequest();
            if (!rec.getPath().startsWith("/oauth2/token")) {
                throw new IllegalStateException("unexpected path " + rec.getPath());
            }
            System.out.println("Token endpoint called, access token obtained.");
        }
    }
}
