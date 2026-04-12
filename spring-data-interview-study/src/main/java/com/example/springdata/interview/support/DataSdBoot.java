package com.example.springdata.interview.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Starts the shared {@link com.example.springdata.interview.SpringDataStudyApplication} with a
 * dedicated in-memory H2 name per lesson run (schema isolation, no per-lesson Application class).
 */
public final class DataSdBoot {

    private DataSdBoot() {}

    /** Preferred entry: one shared study app, unique H2 mem DB per run. */
    public static ConfigurableApplicationContext startStudy(String h2MemName) {
        return start(com.example.springdata.interview.SpringDataStudyApplication.class, h2MemName);
    }

    public static ConfigurableApplicationContext startStudy(String h2MemName, WebApplicationType webType) {
        return start(com.example.springdata.interview.SpringDataStudyApplication.class, h2MemName, webType);
    }

    public static ConfigurableApplicationContext start(Class<?> appClass, String h2MemName) {
        return start(appClass, h2MemName, WebApplicationType.NONE);
    }

    public static ConfigurableApplicationContext start(Class<?> appClass, String h2MemName, WebApplicationType webType) {
        SpringApplication app = new SpringApplication(appClass);
        app.setWebApplicationType(webType);
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("spring.main.banner-mode", "off");
        defaults.put("spring.datasource.url", "jdbc:h2:mem:" + h2MemName + ";DB_CLOSE_DELAY=-1");
        defaults.put("spring.datasource.driver-class-name", "org.h2.Driver");
        defaults.put("spring.jpa.hibernate.ddl-auto", "create-drop");
        defaults.put("spring.jmx.enabled", "false");
        defaults.put("logging.level.root", "warn");
        defaults.put("logging.level.org.hibernate.SQL", "off");
        defaults.put("spring.jpa.open-in-view", "false");
        app.setDefaultProperties(defaults);
        return app.run();
    }
}
