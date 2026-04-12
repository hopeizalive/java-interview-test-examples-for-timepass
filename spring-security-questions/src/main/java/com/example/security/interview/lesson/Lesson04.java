package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

/**
 * {@code ProviderManager} / multiple {@code AuthenticationProvider}—first successful provider wins.
 */
public final class Lesson04 extends AbstractLesson {

    public Lesson04() {
        super(4, "ProviderManager with two AuthenticationProviders—custom token vs DaoAuthenticationProvider.");
    }

    @Override
    public void run(SecurityStudyContext ctx) {
        PasswordEncoder enc = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetailsService uds = new InMemoryUserDetailsManager(
                User.withUsername("db").password("{noop}pw").roles("USER").build());
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(uds);
        dao.setPasswordEncoder(enc);

        AuthenticationProvider magic = new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) {
                if (!(authentication instanceof UsernamePasswordAuthenticationToken tok)) {
                    return null;
                }
                if ("magic".equals(tok.getCredentials())) {
                    return UsernamePasswordAuthenticationToken.authenticated("magic-user", null,
                            AuthorityUtils.createAuthorityList("ROLE_MAGIC"));
                }
                return null;
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };

        AuthenticationManager manager = new ProviderManager(List.of(magic, dao));

        Authentication magicResult = manager.authenticate(
                new UsernamePasswordAuthenticationToken("x", "magic"));
        if (!magicResult.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MAGIC"))) {
            throw new IllegalStateException("magic provider should win");
        }

        Authentication daoResult = manager.authenticate(
                new UsernamePasswordAuthenticationToken("db", "pw"));
        if (!"db".equals(daoResult.getName())) {
            throw new IllegalStateException("dao provider should authenticate");
        }
        System.out.println("magic auth=" + magicResult.getName() + " dao auth=" + daoResult.getName());
    }
}
