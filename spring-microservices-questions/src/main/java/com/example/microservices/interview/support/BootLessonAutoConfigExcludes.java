package com.example.microservices.interview.support;

/**
 * Lessons that exclude servlet {@code SecurityAutoConfiguration} must also exclude actuator
 * {@code ManagementWebSecurityAutoConfiguration}, otherwise Boot still wires a management filter chain
 * that requires an {@code HttpSecurity} bean.
 */
public final class BootLessonAutoConfigExcludes {

    /** Comma-separated list for {@code spring.autoconfigure.exclude}. */
    public static final String NO_SERVLET_SECURITY =
            "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,"
                    + "org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration";

    private BootLessonAutoConfigExcludes() {}
}
