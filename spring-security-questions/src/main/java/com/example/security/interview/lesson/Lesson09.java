package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;

/**
 * {@code PasswordEncoder} (BCrypt via context)—encode, {@code matches}, wrong password fails.
 */
public final class Lesson09 extends AbstractLesson {

    public Lesson09() {
        super(9, "PasswordEncoder (BCrypt)—encode, matches, mismatch fails.");
    }

    @Override
    public void run(SecurityStudyContext ctx) {
        var enc = ctx.passwordEncoder();
        String raw = "CorrectHorseBatteryStaple";
        String hashed = enc.encode(raw);
        if (!enc.matches(raw, hashed)) {
            throw new IllegalStateException("should match");
        }
        if (enc.matches("wrong", hashed)) {
            throw new IllegalStateException("should not match");
        }
        System.out.println("BCrypt hash prefix=" + hashed.substring(0, 4));
    }
}
