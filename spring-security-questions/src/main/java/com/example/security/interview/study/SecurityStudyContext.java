package com.example.security.interview.study;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Shared resources reused across lessons in a CLI run (e.g. run-all). */
public final class SecurityStudyContext {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }
}
