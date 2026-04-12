package com.example.security.interview.lesson;

import com.example.security.interview.study.SecurityStudyContext;
import org.springframework.security.crypto.keygen.KeyGenerators;

/** spring-security-crypto key generators—string and secure-random byte keys. */
public final class Lesson46 extends AbstractLesson {

    public Lesson46() {
        super(44, "KeyGenerators.string() and secureRandom() produce non-empty key material.");
    }

    @Override
    public void run(SecurityStudyContext ctx) {
        String s = KeyGenerators.string().generateKey();
        byte[] bytes = KeyGenerators.secureRandom().generateKey();
        if (s.isEmpty() || bytes.length < 8) {
            throw new IllegalStateException("unexpected key sizes");
        }
        System.out.println("Generated string key length=" + s.length() + " random bytes=" + bytes.length);
    }
}
