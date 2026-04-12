package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;

/** Run-as—{@link RunAsUserToken} augments authorities while retaining original authentication type marker. */
public final class Lesson43 extends AbstractLesson {

    public Lesson43() {
        super(41, "RunAsUserToken—RUN_AS authorities added for impersonation-style elevation.");
    }

    @Override
    public void run(SecurityStudyContext ctx) {
        UsernamePasswordAuthenticationToken parent = UsernamePasswordAuthenticationToken.authenticated(
                "owner", "n/a", AuthorityUtils.createAuthorityList("ROLE_USER"));
        RunAsUserToken runAs = new RunAsUserToken(
                "lesson43-key",
                parent.getPrincipal(),
                parent.getCredentials(),
                AuthorityUtils.createAuthorityList("ROLE_RUN_AS_SPECIAL"),
                UsernamePasswordAuthenticationToken.class);
        List<String> roles = runAs.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        if (!roles.contains("ROLE_RUN_AS_SPECIAL")) {
            throw new IllegalStateException("missing run-as authority: " + roles);
        }
        System.out.println("Run-as token authenticated=" + runAs.isAuthenticated() + " authorities=" + roles);
    }
}
