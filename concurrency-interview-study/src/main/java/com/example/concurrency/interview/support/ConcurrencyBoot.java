package com.example.concurrency.interview.support;

import com.example.concurrency.interview.ConcurrencyStudyApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.LinkedHashMap;
import java.util.Map;

/** Starts {@link ConcurrencyStudyApplication} with study-friendly defaults (no web by default). */
public final class ConcurrencyBoot {

    private ConcurrencyBoot() {}

    public static ConfigurableApplicationContext startStudy() {
        return startStudy(Map.of());
    }

    public static ConfigurableApplicationContext startStudy(Map<String, Object> extraDefaults) {
        return startStudy(WebApplicationType.NONE, extraDefaults);
    }

    public static ConfigurableApplicationContext startStudy(WebApplicationType webType, Map<String, Object> extraDefaults) {
        SpringApplication app = new SpringApplication(ConcurrencyStudyApplication.class);
        app.setWebApplicationType(webType);
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("spring.main.banner-mode", "off");
        defaults.put("spring.jmx.enabled", "false");
        defaults.put("logging.level.root", "warn");
        defaults.putAll(extraDefaults);
        app.setDefaultProperties(defaults);
        return app.run();
    }
}
