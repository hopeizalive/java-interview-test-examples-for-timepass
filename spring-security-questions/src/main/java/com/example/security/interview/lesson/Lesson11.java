package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * {@code UserDetailsService}—success vs {@link UsernameNotFoundException}.
 */
public final class Lesson11 extends AbstractLesson {

    public Lesson11() {
        super(11, "UserDetailsService—load user vs UsernameNotFoundException.");
    }

    @Override
    public void run(SecurityStudyContext ctx) {
        UserDetailsService ok = username -> User.withUsername("found").password("{noop}x").roles("USER").build();
        if (!"found".equals(ok.loadUserByUsername("found").getUsername())) {
            throw new IllegalStateException();
        }
        UserDetailsService missing = username -> {
            throw new UsernameNotFoundException("no " + username);
        };
        try {
            missing.loadUserByUsername("ghost");
            throw new IllegalStateException("expected UsernameNotFoundException");
        } catch (UsernameNotFoundException e) {
            System.out.println("Caught expected: " + e.getMessage());
        }
    }
}
