package com.example.springannotation.interview.lesson.fixtures;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Types for lessons 11–13: grouped {@code @ConfigurationProperties}, mutable vs immutable binding,
 * and {@code @EnableConfigurationProperties} registration.
 */
public final class ConfigurationPropertiesFixtures {

    private ConfigurationPropertiesFixtures() {}

    @ConfigurationProperties(prefix = "demo.db")
    public static class DbProps {
        private String url;
        private int poolSize;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPoolSize() {
            return poolSize;
        }

        public void setPoolSize(int poolSize) {
            this.poolSize = poolSize;
        }
    }

    public static class MutableProps {
        private String host;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

    public record ImmutableProps(String host, int port) {}

    @Configuration
    @EnableConfigurationProperties(DbProps.class)
    public static class EnablePropsConfig {}
}
