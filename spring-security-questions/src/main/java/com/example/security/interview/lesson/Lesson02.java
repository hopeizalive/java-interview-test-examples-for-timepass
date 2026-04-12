package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * {@code SecurityContext} and {@code SecurityContextHolder}—set/clear/read around a simulated request scope.
 */
public final class Lesson02 extends AbstractLesson {

    public Lesson02() {
        super(2, "SecurityContext and SecurityContextHolder—set/clear/read programmatically.");
    }

    @Override
    public void run(SecurityStudyContext ctx) {
        try {
            SecurityContextHolder.clearContext();
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                throw new IllegalStateException("expected empty context");
            }
            UsernamePasswordAuthenticationToken auth =
                    UsernamePasswordAuthenticationToken.authenticated("bob", "n/a",
                            AuthorityUtils.createAuthorityList("ROLE_USER"));
            SecurityContext sc = SecurityContextHolder.createEmptyContext();
            sc.setAuthentication(auth);
            SecurityContextHolder.setContext(sc);
            if (!"bob".equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                throw new IllegalStateException("read back name");
            }
            System.out.println("Held authentication=" + SecurityContextHolder.getContext().getAuthentication());
        } finally {
            SecurityContextHolder.clearContext();
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                throw new IllegalStateException("clear failed");
            }
        }
    }
}
