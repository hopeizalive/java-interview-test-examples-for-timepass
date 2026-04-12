package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * {@code DelegatingPasswordEncoder} / {@code {id}} prefix—multiple encodings in one store.
 */
public final class Lesson10 extends AbstractLesson {

    public Lesson10() {
        super(10, "DelegatingPasswordEncoder with {bcrypt} and {noop} stored passwords.");
    }

    @Override
    public void run(SecurityStudyContext ctx) {
        PasswordEncoder enc = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String hashed = enc.encode("hello");
        if (!enc.matches("hello", hashed)) {
            throw new IllegalStateException("bcrypt delegation should verify");
        }
        if (!enc.matches("x", "{noop}x")) {
            throw new IllegalStateException("noop prefix should verify");
        }
        System.out.println("bcrypt stored prefix: " + hashed.substring(0, 15) + "...");
    }
}
