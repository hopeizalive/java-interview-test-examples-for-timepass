package com.example.springdata.interview;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Single Boot application for every lesson. Lessons start a <strong>new</strong> context with a
 * fresh in-memory H2 URL so data stays isolated without maintaining dozens of {@code @SpringBootApplication} classes.
 */
@SpringBootApplication(
        scanBasePackages = "com.example.springdata.interview",
        exclude = {SecurityAutoConfiguration.class}
)
@EnableJpaAuditing
public class SpringDataStudyApplication {}
