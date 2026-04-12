package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;

/**
 * SAML2 SP metadata—{@link RelyingPartyRegistration} built from classpath IdP metadata XML.
 */
public final class Lesson38 extends AbstractLesson {

    public Lesson38() {
        super(38, "SAML2—RelyingPartyRegistrations.fromMetadataLocation reads asserting party SSO URL.");
    }

    @Override
    public void run(SecurityStudyContext ctx) {
        RelyingPartyRegistration rp = RelyingPartyRegistrations
                .fromMetadataLocation("classpath:saml/idp-metadata.xml")
                .registrationId("lesson38")
                .entityId("https://sp.lesson.local")
                .build();
        String entityId = rp.getAssertingPartyDetails().getEntityId();
        if (!entityId.contains("lesson-idp")) {
            throw new IllegalStateException("unexpected IdP entityId: " + entityId);
        }
        System.out.println("Loaded SAML registrationId=" + rp.getRegistrationId() + " assertingPartyEntityId=" + entityId);
    }
}
