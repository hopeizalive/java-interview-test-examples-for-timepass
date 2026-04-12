package com.example.microservices.interview.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.LinkedHashMap;
import java.util.Map;

/** Starts a minimal Spring Boot app (JPA + H2) for data-focused lessons. */
public final class MicroBoot {

    private MicroBoot() {}

    public static ConfigurableApplicationContext start(Class<?> appClass, String h2MemName) {
        SpringApplication app = new SpringApplication(appClass);
        app.setWebApplicationType(WebApplicationType.NONE);
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("spring.main.banner-mode", "off");
        defaults.put("spring.datasource.url", "jdbc:h2:mem:" + h2MemName + ";DB_CLOSE_DELAY=-1");
        defaults.put("spring.datasource.driver-class-name", "org.h2.Driver");
        defaults.put("spring.jpa.hibernate.ddl-auto", "create-drop");
        defaults.put("spring.jmx.enabled", "false");
        defaults.put("logging.level.root", "warn");
        defaults.put("logging.level.org.hibernate.SQL", "off");
        app.setDefaultProperties(defaults);
        return app.run();
    }
}
